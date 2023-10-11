package com.example.bookshelf_app.feat_auth.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bookshelf_app.R
import com.example.bookshelf_app.core.presentation.activities.MainActivity
import com.example.bookshelf_app.core.utils.UserProvider
import com.example.bookshelf_app.databinding.FragmentSignUpBinding
import com.example.bookshelf_app.feat_auth.domain.models.UserModel
import com.example.bookshelf_app.feat_auth.presentation.viewmodels.SignUpViewModel
import com.example.bookshelf_app.feat_auth.utils.AuthUtils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding by lazy {
        _binding!!
    }
    private val viewModel: SignUpViewModel by viewModels()
    private var users: List<UserModel> = listOf()
    private var userName: String = ""
    private var password: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater)
        showProgress()
        setViews()
        hideProgress()
        return binding.root
    }

    private fun setViews() {
        val spinnerAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                AuthUtils.countryList
            )
        viewModel.getAllUserList()
        binding.apply {
            fragmentSignUpSpCountry.adapter = spinnerAdapter
            fragmentSignUpBtSignUp.setOnClickListener {
                userName = fragmentSignUpTietUsername.text.toString()
                password = fragmentSignUpTietPassword.text.toString()
                val country = fragmentSignUpSpCountry.selectedItem.toString()
                val isPasswordValid = AuthUtils.validatePassword(password)
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
                } else if (isPasswordValid) {
                    lifecycleScope.launch {
                        val user = UserModel(null, userName, password, country)
                        if (!users.any { it.userName == userName }) viewModel.insertUser(user)
                        else Toast.makeText(
                            requireContext(),
                            "Username already taken",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Snackbar.make(
                        binding.root.rootView,
                        getString(R.string.for_password_min_8_characters_with_atleast_one_number_special_characters_one_lowercase_letter_and_one_uppercase_letter_is_mandatory),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            fragmentSignUpTvLogin.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        lifecycleScope.launch {
            viewModel.mSignUpState.collectLatest { state ->
                val users = state.users
                val insertStatus: Boolean? = state.insertStatus
                val containsError: String? = state.containsError
                val isLoading: Boolean? = state.isLoading
                users.let {
                    this@SignUpFragment.users = users
                }
                isLoading?.let {
                    if (it) showProgress()
                    else hideProgress()
                }
                containsError?.let { error ->
                    Snackbar.make(requireView(), error, Snackbar.LENGTH_SHORT).show()
                }
                insertStatus?.let { status ->
                    if (status) {
                        lifecycleScope.launch {
                            val user = UserModel(null, userName , password)
                            if (UserProvider.saveCurrentLoggedUser(requireContext(), user)) {
                                while (findNavController().currentBackStack.value.isNotEmpty()) {
                                    findNavController().popBackStack()
                                }
                                findNavController().navigate(R.id.mainBookListFragment)
                            }
                        }
                        
                    }
                }
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