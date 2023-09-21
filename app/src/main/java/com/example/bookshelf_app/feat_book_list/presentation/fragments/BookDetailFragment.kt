package com.example.bookshelf_app.feat_book_list.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bookshelf_app.core.presentation.activities.MainActivity
import com.example.bookshelf_app.databinding.FragmentBookDetailBinding
import com.example.bookshelf_app.feat_book_list.presentation.viewmodels.BookViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookDetailFragment : Fragment() {
    private var _binding: FragmentBookDetailBinding? = null
    private val binding by lazy {
        _binding!!
    }

    private val viewModel:BookViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookDetailBinding.inflate(inflater)
        showProgress()
        setViews()
        hideProgress()
        return binding.root
    }

    private fun setViews() {
        binding.apply {

        }
    }

    private fun showProgress() = (requireActivity() as MainActivity).showProgress()

    private fun hideProgress() = (requireActivity() as MainActivity).hideProgress()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}