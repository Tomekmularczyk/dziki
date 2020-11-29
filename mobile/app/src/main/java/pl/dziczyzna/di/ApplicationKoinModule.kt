package pl.dziczyzna.di

import android.content.ContentResolver
import android.location.LocationManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.dziczyzna.common.LocationPermissionCheck
import pl.dziczyzna.common.RxSchedulers
import pl.dziczyzna.database.DatabaseProvider
import pl.dziczyzna.database.DatabaseProviderImpl
import pl.dziczyzna.login.domain.preferences.UserPreferences
import pl.dziczyzna.login.domain.repository.UserRepository
import pl.dziczyzna.login.presentation.UserViewModel
import pl.dziczyzna.login.presentation.mapper.CreateUserErrorMapper
import pl.dziczyzna.report.domain.location.UserLocationProvider
import pl.dziczyzna.report.domain.photo.PhotoCapture
import pl.dziczyzna.report.domain.photo.PhotoUpload
import pl.dziczyzna.report.domain.time.TimeProvider
import pl.dziczyzna.report.presentation.ReportViewMode

internal object ApplicationKoinModule {

    fun create() = module {

        viewModel {
            UserViewModel(
                userRepository = get(),
                errorMapper = get(),
                schedulers = get()
            )
        }

        viewModel { (locationManager: LocationManager, contentResolver: ContentResolver) ->
            ReportViewMode(
                timeProvider = TimeProvider(),
                locationPermissionCheck = LocationPermissionCheck(androidContext()),
                userLocationProvider = UserLocationProvider(androidContext(), locationManager),
                photoCapture = PhotoCapture(contentResolver),
                photoUpload = PhotoUpload(),
                schedulers = get()
            )
        }

        factory<DatabaseProvider> { DatabaseProviderImpl() }

        factory { UserRepository(get()) }

        factory { CreateUserErrorMapper(androidContext().resources) }

        factory { RxSchedulers() }

        factory { UserPreferences(androidContext()) }
    }
}