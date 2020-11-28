package pl.dziczyzna.login.domain.error

internal class LoginAlreadyTakenException : Throwable("Cannot use thid login, use another one")