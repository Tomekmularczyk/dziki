package pl.dziczyzna.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.dziczyzna.common.RxSchedulers
import pl.dziczyzna.database.DatabaseProvider
import pl.dziczyzna.database.DatabaseProviderImpl
import pl.dziczyzna.login.domain.preferences.UserPreferences
import pl.dziczyzna.login.domain.repository.UserRepository
import pl.dziczyzna.login.presentation.UserViewModel
import pl.dziczyzna.login.presentation.mapper.CreateUserErrorMapper

internal object ApplicationKoinModule {

    fun create() = module {

        viewModel {
            UserViewModel(
                userRepository = get(),
                errorMapper = get(),
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