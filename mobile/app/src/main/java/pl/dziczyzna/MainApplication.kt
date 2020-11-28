package pl.dziczyzna

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.dziczyzna.di.ApplicationKoinModule

internal class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)

        startKoin {
            androidContext(this@MainApplication)
            modules(ApplicationKoinModule.create())
        }
    }
}