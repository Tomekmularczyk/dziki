package pl.dziczyzna.login.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import pl.dziczyzna.common.RxSchedulers
import pl.dziczyzna.login.domain.repository.UserRepository
import pl.dziczyzna.login.presentation.mapper.CreateUserErrorMapper
import pl.dziczyzna.login.presentation.model.CreateUserUi

internal class UserViewModel(
    private val userRepository: UserRepository,
    private val errorMapper: CreateUserErrorMapper,
    private val schedulers: RxSchedulers
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val createUserResult = MutableLiveData<CreateUserUi>()

    fun createUserResult(): LiveData<CreateUserUi> {
        return createUserResult
    }

    fun createUser(login: String) {
        userRepository
            .createUser(login)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.android())
            .doOnSubscribe { pushState(CreateUserUi.InProgress) }
            .subscribe({
                pushState(CreateUserUi.Success(login))
            }, { throwable ->
                pushState(errorMapper.mapError(throwable))
            })
            .also { disposables.addAll(it) }
    }

    private fun pushState(state: CreateUserUi) {
        createUserResult.value = state
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}