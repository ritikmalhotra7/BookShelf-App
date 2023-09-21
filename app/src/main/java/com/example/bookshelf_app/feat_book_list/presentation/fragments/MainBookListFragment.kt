package com.example.bookshelf_app.feat_book_list.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bookshelf_app.core.presentation.activities.MainActivity
import com.example.bookshelf_app.databinding.FragmentMainBookListBinding
import com.example.bookshelf_app.feat_book_list.domain.models.BookModel
import com.example.bookshelf_app.feat_book_list.presentation.viewmodels.BookViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainBookListFragment : Fragment() {
    private var _binding: FragmentMainBookListBinding? = null
    private val binding by lazy {
        _binding!!
    }
    private val viewModel:BookViewModel by viewModels()
    private var books: List<BookModel> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBookListBinding.inflate(inflater)
        showProgress()
        setViews()
        hideProgress()
        return binding.root
    }

    private fun setViews() {
        binding.apply {

        }
        lifecycleScope.launch {
            viewModel.mBookState.collectLatest {state->
                val books = state.books
                val containsError: String? = state.containsError
                val isLoading: Boolean? = state.isLoading

                isLoading?.let {
                    if (it) showProgress()
                    else hideProgress()
                }
                containsError?.let { error ->
                    Snackbar.make(requireView(), error, Snackbar.LENGTH_SHORT).show()
                }
                this@MainBookListFragment.books = books
                Log.d("taget",books.toString()
                )
            }
        }
    }

    private fun showProgress() = (requireActivity() as MainActivity).showProgress()

    private fun hideProgress() = (requireActivity() as MainActivity).hideProgress()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}