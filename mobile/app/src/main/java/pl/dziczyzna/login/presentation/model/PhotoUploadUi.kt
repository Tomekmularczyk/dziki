package pl.dziczyzna.login.presentation.model

internal sealed class PhotoUploadUi {
    object Success : PhotoUploadUi()
    data class Progress(val progress: Int) : PhotoUploadUi()
    data class Error(val throwable: Throwable) : PhotoUploadUi()
}