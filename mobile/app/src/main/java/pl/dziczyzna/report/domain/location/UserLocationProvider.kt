package pl.dziczyzna.report.domain.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import io.reactivex.Single
import pl.dziczyzna.report.domain.error.LocationNotFoundException
import pl.dziczyzna.report.domain.model.UserLocation
import java.io.IOException

internal class UserLocationProvider(private val context: Context, private val locationManager: LocationManager) {

    fun getUserLocation(): Single<UserLocation> {
        return Single.create { emitter ->
            val location = getLastKnownGpsLocation() ?: getLastKnownGpsLocation()

            if (location == null) {
                emitter.onError(LocationNotFoundException())
            } else {
                try {
                    val addresses = Geocoder(context).getFromLocation(location.latitude, location.longitude, 1)
                    val city: String = addresses.firstOrNull()?.locality.orEmpty()
                    val state: String = addresses.firstOrNull()?.adminArea.orEmpty()
                    emitter.onSuccess(UserLocation(city, state, location.latitude, location.longitude))
                } catch (exception: IOException) {
                    emitter.onError(exception)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownGpsLocation(): Location? {
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnowNetworkLocation(): Location? {
        return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    }
}