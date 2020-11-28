package pl.dziczyzna.login.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.dziczyzna.R
import pl.dziczyzna.databinding.FragmentLoginBinding
import pl.dziczyzna.login.domain.preferences.UserPreferences
import pl.dziczyzna.login.presentation.model.CreateUserUi

internal class LoginFragment : Fragment(R.layout.fragment_login) {

    private val userViewModel: UserViewModel by viewModel()

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextLogin.doAfterTextChanged {
            binding.buttonContinue.isEnabled = !it.isNullOrBlank()
        }
        binding.buttonContinue.setOnClickListener { userViewModel.createUser(getUserName()) }

        observeCreateUserResult()
    }

    private fun observeCreateUserResult() {
        userViewModel.createUserResult().observe(viewLifecycleOwner) { result ->
            when (result) {
                is CreateUserUi.InProgress -> result.render()
                is CreateUserUi.Success -> result.render()
                is CreateUserUi.Error -> result.render()
            }
        }
    }

    private fun CreateUserUi.Success.render() {
        binding.buttonContinue.isEnabled = false
        binding.inputLogin.error = null
        get<UserPreferences>().saveUserLogin(login)
    }

    private fun CreateUserUi.InProgress.render() {
        binding.buttonContinue.isEnabled = false
        binding.inputLogin.error = null
    }

    private fun CreateUserUi.Error.render() {
        binding.buttonContinue.isEnabled = true

        when (this) {
            is CreateUserUi.Error.UserInvalidError -> {
                binding.inputLogin.error = message
            }
            is CreateUserUi.Error.IoError -> {
                binding.inputLogin.error = null
                showSnackbar(message, getString(R.string.io_error)) { userViewModel.createUser(getUserName()) }
            }
        }
    }

    private fun showSnackbar(title: String, actionText: String?, action: (() -> Unit)?) {
        Snackbar.make(binding.root, title, Snackbar.LENGTH_LONG).apply {
            if (actionText != null && action != null) {
                setAction(actionText) { action() }
            }

            show()
        }
    }

    private fun getUserName(): String {
        return binding.editTextLogin.text.toString()
    }
}