package com.example.bookshelf_app.feat_auth.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bookshelf_app.R
import com.example.bookshelf_app.core.presentation.activities.MainActivity
import com.example.bookshelf_app.core.utils.UserProvider
import com.example.bookshelf_app.databinding.FragmentLoginBinding
import com.example.bookshelf_app.feat_auth.domain.models.UserModel
import com.example.bookshelf_app.feat_auth.presentation.viewmodels.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding by lazy {
        _binding!!
    }
    private val viewModel: LoginViewModel by viewModels()

    private var users: List<UserModel> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If user is already logged in then, user must go directly to main book screen
        lifecycleScope.launch {
            if (UserProvider.getLoggedInUser(requireContext()) != null) {
                findNavController().popBackStack()
                findNavController().navigate(R.id.mainBookListFragment)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater)
        showProgress()
        setViews()
        hideProgress()
        return binding.root
    }

    private fun setViews() {
        viewModel.getAllUserList()
        binding.apply {
            fragmentLoginTvSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }
            fragmentLoginBtLogin.setOnClickListener {
                val userName = fragmentLoginTietUsername.text.toString()
                val password = fragmentLoginTietPassword.text.toString()
                if (userName.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.username_must_not_be_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (password.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.pass_must_not_be_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    lifecycleScope.launch {
                        val user = UserModel(null, userName, password)
                        checkDoesUserExist(user)?.let { userFromDb ->
                            if (UserProvider.saveCurrentLoggedUser(requireContext(), userFromDb)) {
                                findNavController().popBackStack()
                                findNavController().navigate(R.id.mainBookListFragment)
                            }
                        } ?: Snackbar.make(
                            binding.root.rootView,
                            "Wrong credentials",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.mLoginState.collectLatest { state ->
                val users = state.users
                val containsError: String? = state.containsError
                val isLoading: Boolean? = state.isLoading
                isLoading?.let {
                    if (it) showProgress()
                    else hideProgress()
                }
                containsError?.let { error ->
                    Snackbar.make(requireView(), error, Snackbar.LENGTH_SHORT).show()
                }
                this@LoginFragment.users = users
            }
        }
    }

    private fun checkDoesUserExist(userModel: UserModel): UserModel? {
        val list = users.filter { it.userName == userModel.userName }
        if (list.isNotEmpty()) {
            val user = list[0]
            if (user.password == userModel.password) return list[0]
        }
        return null
    }

    private fun showProgress() = (requireActivity() as MainActivity).showProgress()

    private fun hideProgress() = (requireActivity() as MainActivity).hideProgress()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}