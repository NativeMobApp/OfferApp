package com.example.OfferApp.data.repository

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.OfferApp.domain.entities.Post
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
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

            // Espera correctamente a que Firestore termine
            suspendCancellableCoroutine<Result<Unit>> { continuation ->
                firestore.collection("posts").add(post)
                    .addOnSuccessListener {
                        if (continuation.isActive) continuation.resume(Result.success(Unit))
                    }
                    .addOnFailureListener { e ->
                        if (continuation.isActive) continuation.resume(Result.failure(e))
                    }
            }
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