package pl.dziczyzna.report.domain.photo

import android.graphics.Bitmap
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Observable
import pl.dziczyzna.login.domain.model.UploadImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.UUID

internal class PhotoUpload {

    private val storageReference = FirebaseStorage.getInstance().reference

    fun upload(bitmap: Bitmap): Observable<UploadImage> {
        return Observable.create { emitter ->
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
            val data = baos.toByteArray()

            val fileName = UUID.randomUUID().toString()
            val child = storageReference.child(fileName)
            child.putBytes(data)
                .addOnSuccessListener { snapshot ->
                    child.downloadUrl.addOnSuccessListener { uri ->
                        emitter.onNext(UploadImage.Success(uri.toString()))
                        emitter.onComplete()
                    }
                }
                .addOnProgressListener { snapshot ->
                    val percent = (100.0 * snapshot.bytesTransferred / snapshot.totalByteCount).toInt()
                    emitter.onNext(UploadImage.Progress(percent))
                }
                .addOnCanceledListener {
                    emitter.onError(IOException("Failed to send image"))

                }.addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }
}