package com.example.bookshelf_app.feat_book_list.presentation.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookshelf_app.R
import com.example.bookshelf_app.core.presentation.activities.MainActivity
import com.example.bookshelf_app.core.utils.UserProvider
import com.example.bookshelf_app.databinding.FragmentMainBookListBinding
import com.example.bookshelf_app.feat_book_list.domain.models.BookModel
import com.example.bookshelf_app.feat_book_list.presentation.adapters.BookAdapter
import com.example.bookshelf_app.feat_book_list.presentation.viewmodels.BookViewModel
import com.example.bookshelf_app.feat_book_list.utils.BookUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainBookListFragment : Fragment() {
    private var _binding: FragmentMainBookListBinding? = null
    private val binding by lazy {
        _binding!!
    }
    private val viewModel: BookViewModel by viewModels()
    private lateinit var bookAdapter: BookAdapter
    private var books: List<BookModel> = listOf()
    private var isAscending: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBookListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress()
        setViews()
        hideProgress()
    }

    private fun setViews() {
        //checking for user info
        lifecycleScope.launch {
            if (UserProvider.getLoggedInUser(requireContext()) == null) {
                lifecycleScope.launch {
                    Toast.makeText(
                        requireContext(), getString(R.string.logged_out), Toast.LENGTH_SHORT
                    ).show()
                    UserProvider.removeCurrentLoggedInUser(requireContext())
                    while (findNavController().currentBackStack.value.isNotEmpty()){
                        findNavController().popBackStack()
                    }
                    findNavController().navigate(R.id.loginFragment)
                }
            }
        }
        bookAdapter = BookAdapter()
        binding.apply {
            fragmentMainBookListMbtg.addOnButtonCheckedListener { group, checkedId, isChecked ->
                if (isChecked) {
                    val selectedButton = requireActivity().findViewById<MaterialButton>(checkedId)
                    when (selectedButton) {
                        fragmentMainBookListMbtgTitle -> {
                            if (isAscending) bookAdapter.setList(books.sortedBy { it.title })
                            else bookAdapter.setList(books.sortedByDescending { it.title })
                        }

                        fragmentMainBookListMbtgHits -> {
                            if (isAscending) bookAdapter.setList(books.sortedBy { it.hits })
                            else bookAdapter.setList(books.sortedByDescending { it.hits })
                        }

                        fragmentMainBookListMbtgFav -> {
                            bookAdapter.setList(books.filter { it.isFav == true }.apply {
                                map { it.copy(isFav = true) }
                            })
                        }
                    }
                } else {
                    bookAdapter.setList(books)
                }
            }
            fragmentMainBookListSwtOrder.setOnCheckedChangeListener { _, isChecked ->
                isAscending = isChecked
                updateBookListAccToSelectedToggleButton()
            }
            fragmentMainBookListBtLogout.setOnClickListener {
                lifecycleScope.launch {
                    UserProvider.removeCurrentLoggedInUser(requireContext())
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.loginFragment)
                }
            }
            fragmentMainBookListRvBooks.apply {
                adapter = bookAdapter.apply {
                    setClickListener {
                        findNavController()
                            .navigate(R.id.action_mainBookListFragment_to_bookDetailFragment,
                                Bundle().apply {
                                    putString(BookUtils.BOOK_ITEM_KEY, Gson().toJson(it))
                                })
                    }
                    setFavouriteClicked { book, favouriteChecked ->
                        //updating the list according to favourite checked
                        val resultList = books.toMutableList()
                        if (!favouriteChecked) {
                            resultList.apply {
                                filter { it.id == book.id }.map { it.isFav = true }
                            }
                        } else {
                            resultList.apply {
                                filter { it.id == book.id }.map { it.isFav = false }
                            }
                        }
                        books = resultList
                        setList(resultList)
                        updateBookListAccToSelectedToggleButton()
                    }
                }
                layoutManager = GridLayoutManager(requireContext(),2)
            }
            lifecycleScope.launch {
                //getting the data from view model
                viewModel.mBookState.collectLatest { state ->
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

                    this@MainBookListFragment.books = books.shuffled()
                    bookAdapter.setList(this@MainBookListFragment.books)
                }
            }
        }
    }

    private fun FragmentMainBookListBinding.updateBookListAccToSelectedToggleButton() {
        when (fragmentMainBookListMbtg.checkedButtonId) {
            fragmentMainBookListMbtgTitle.id -> {
                if (isAscending) bookAdapter.setList(books.sortedBy { it.title })
                else bookAdapter.setList(books.sortedByDescending { it.title })
            }

            fragmentMainBookListMbtgHits.id -> {
                if (isAscending) bookAdapter.setList(books.sortedBy { it.hits })
                else bookAdapter.setList(books.sortedByDescending { it.hits })
            }

            fragmentMainBookListMbtgFav.id -> {
                bookAdapter.setList(books.filter { it.isFav == true })
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