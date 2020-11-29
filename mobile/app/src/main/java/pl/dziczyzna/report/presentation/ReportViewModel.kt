package pl.dziczyzna.report.presentation

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import pl.dziczyzna.common.LocationPermissionCheck
import pl.dziczyzna.common.RxSchedulers
import pl.dziczyzna.common.SingleLiveData
import pl.dziczyzna.login.domain.model.UploadImage
import pl.dziczyzna.login.presentation.model.PhotoUploadUi
import pl.dziczyzna.report.domain.location.UserLocationProvider
import pl.dziczyzna.report.domain.model.PigCount
import pl.dziczyzna.report.domain.model.PigType
import pl.dziczyzna.report.domain.photo.PhotoCapture
import pl.dziczyzna.report.domain.photo.PhotoUpload
import pl.dziczyzna.report.domain.time.TimeProvider
import pl.dziczyzna.report.presentation.model.ReportStateUi
import pl.dziczyzna.report.presentation.model.UserLocationUi

internal class ReportViewMode(
    timeProvider: TimeProvider,
    private val locationPermissionCheck: LocationPermissionCheck,
    private val userLocationProvider: UserLocationProvider,
    private val photoCapture: PhotoCapture,
    private val photoUpload: PhotoUpload,
    private val schedulers: RxSchedulers
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val reportViewState =
        MutableLiveData(ReportStateUi(time = timeProvider.getCurrentTime(), date = timeProvider.getCurrentDate()))
    private val userLocationResult = MutableLiveData<UserLocationUi>()
    private val photoUploadResult = MutableLiveData<PhotoUploadUi>()
    private val grantLocationPermissionEvent = SingleLiveData<Unit>()
    private val captureImageEvent = SingleLiveData<Uri>()

    fun reportViewState(): LiveData<ReportStateUi> {
        return reportViewState
    }

    fun grantLocationPermissionEvent(): LiveData<Unit> {
        return grantLocationPermissionEvent
    }

    fun captureImageEvent(): LiveData<Uri> {
        return captureImageEvent
    }

    fun userLocationResult(): LiveData<UserLocationUi> {
        return userLocationResult
    }

    fun photoUploadResult(): LiveData<PhotoUploadUi> {
        return photoUploadResult
    }

    fun capturePhoto() {
        captureImageEvent.value = photoCapture.imageUri
    }

    fun loadPhoto() {
        executeLoadPhoto()
    }

    fun fetchUserLocation() {
        val userLocationResultValue = userLocationResult.value

        if (userLocationResultValue == null || userLocationResultValue is UserLocationUi.Error) {
            if (locationPermissionCheck.isLocationPermissionGranted()) {
                executeGetUserLocation()
            } else {
                grantLocationPermissionEvent.value = Unit
            }
        }
    }

    fun setPigType(type: PigType) {
        pushReportState(getCurrentReport().copy(type = type))
    }

    fun setPigCount(count: PigCount) {
        pushReportState(getCurrentReport().copy(count = count))
    }

    fun tryAgainUploadImage() {
        executeUploadPhoto(getCurrentReport().image!!)
    }

    private fun executeGetUserLocation() {
        userLocationProvider.getUserLocation()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.android())
            .doOnSubscribe { pushUserLocationResult(UserLocationUi.InProgress) }
            .subscribe({ userLocation ->
                pushReportState(getCurrentReport().copy(city = userLocation.city, state = userLocation.state))
                pushUserLocationResult(UserLocationUi.Success)
            }, { throwable ->
                pushUserLocationResult(UserLocationUi.Error(throwable))
            }).also {
                disposables.add(it)
            }
    }

    private fun executeLoadPhoto() {
        photoCapture.createBitmap()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.android())
            .subscribe({ image ->
                pushReportState(getCurrentReport().copy(image = image))
                executeUploadPhoto(image)
            }, {
                // nop
            }).also {
                disposables.add(it)
            }
    }

    private fun executeUploadPhoto(bitmap: Bitmap) {
        photoUpload.upload(bitmap)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.android())
            .startWith(UploadImage.Progress(0))
            .subscribe({ uploadImage ->
                when (uploadImage) {
                    is UploadImage.Progress -> {
                        pushPhotoUploadResult(PhotoUploadUi.Progress(uploadImage.progress))
                    }
                    is UploadImage.Success  -> {
                        pushPhotoUploadResult(PhotoUploadUi.Success)
                        pushReportState(getCurrentReport().copy(imageUrl = uploadImage.imageUrl))
                    }
                }
            }, { throwable ->
                pushPhotoUploadResult(PhotoUploadUi.Error(throwable))
            }).also {
                disposables.add(it)
            }
    }

    private fun pushReportState(result: ReportStateUi) {
        reportViewState.value = result
    }

    private fun pushUserLocationResult(result: UserLocationUi) {
        userLocationResult.value = result
    }

    private fun pushPhotoUploadResult(result: PhotoUploadUi) {
        photoUploadResult.value = result
    }

    private fun getCurrentReport(): ReportStateUi {
        return reportViewState.value!!
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}