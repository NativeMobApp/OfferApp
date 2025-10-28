package com.example.OfferApp.data.repository

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.OfferApp.domain.entities.Post
import com.example.OfferApp.domain.entities.Score
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class PostRepository {

    private val firestore = FirebaseFirestore.getInstance()

    private suspend fun uploadImageToCloudinary(imageUri: Uri): String {
        return suspendCancellableCoroutine { continuation ->
            MediaManager.get().upload(imageUri)
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {}
                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}

                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        val imageUrl = resultData["url"].toString()
                        if (continuation.isActive) {
                            continuation.resume(imageUrl)
                        }
                    }

                    override fun onError(requestId: String, error: ErrorInfo) {
                        if (continuation.isActive) {
                            continuation.resumeWithException(Exception(error.description))
                        }
                    }

                    override fun onReschedule(requestId: String, error: ErrorInfo) {}
                })
                .dispatch()
        }
    }

    suspend fun addPost(post: Post, imageUri: Uri): Result<Unit> {
        return try {
            val imageUrl = uploadImageToCloudinary(imageUri)
            post.imageUrl = imageUrl
            
            suspendCancellableCoroutine<Unit> { continuation ->
                firestore.collection("posts").add(post)
                    .addOnSuccessListener { 
                        if (continuation.isActive) continuation.resume(Unit) 
                    }
                    .addOnFailureListener { e -> 
                        if (continuation.isActive) continuation.resumeWithException(e) 
                    }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePostScore(postId: String, userId: String, value: Int): Result<Unit> {
        return try {
            val postRef = firestore.collection("posts").document(postId)
            firestore.runTransaction { transaction ->
                val post = transaction.get(postRef).toObject(Post::class.java)
                    ?: throw Exception("Post not found")

                val existingScoreIndex = post.scores.indexOfFirst { it.userId == userId }
                val newScores = post.scores.toMutableList()

                if (existingScoreIndex != -1) {
                    if (newScores[existingScoreIndex].value == value) {
                        newScores.removeAt(existingScoreIndex)
                    } else {
                        newScores[existingScoreIndex] = newScores[existingScoreIndex].copy(value = value)
                    }
                } else {
                    newScores.add(Score(userId, value))
                }
                transaction.update(postRef, "scores", newScores)
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getPosts(): Flow<List<Post>> {
        return callbackFlow {
            val listener = firestore.collection("posts")
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        close(e)
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val posts = snapshot.toObjects(Post::class.java)
                        trySend(posts).isSuccess
                    }
                }
            awaitClose { listener.remove() }
        }
    }
}