package pl.dziczyzna.report.domain.photo

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.provider.MediaStore
import io.reactivex.Single
import java.io.InputStream

internal class PhotoCapture(private val contentResolver: ContentResolver) {

    val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, getImageContentValues())!!

    fun createBitmap(): Single<Bitmap> {
        return Single.create { emitter ->

            try {
                val inputStream: InputStream = contentResolver.openInputStream(imageUri)!!
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val matrix = Matrix()
                matrix.postRotate(90F)
                val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                emitter.onSuccess(rotatedBitmap)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    private fun getImageContentValues(): ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, TITLE)
        values.put(MediaStore.Images.Media.MIME_TYPE, MIME_TYPE)

        return values
    }

    private companion object {
        private const val TITLE = "pigTmp"
        private const val MIME_TYPE = "image/png"
    }
}