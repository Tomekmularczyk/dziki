package pl.dziczyzna.login.domain.model

internal sealed class UploadImage {
    data class Progress(val progress: Int) : UploadImage()
    data class Success(val imageUrl: String) : UploadImage()
}