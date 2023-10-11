package com.example.bookshelf_app.feat_book_list.presentation.fragments

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.bookshelf_app.R
import com.example.bookshelf_app.core.presentation.activities.MainActivity
import com.example.bookshelf_app.core.utils.UserProvider
import com.example.bookshelf_app.databinding.FragmentBookDetailBinding
import com.example.bookshelf_app.feat_book_list.domain.models.BookModel
import com.example.bookshelf_app.feat_book_list.utils.BookUtils
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class BookDetailFragment : Fragment() {
    private var _binding: FragmentBookDetailBinding? = null
    private val binding by lazy {
        _binding!!
    }

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

    @SuppressLint("SimpleDateFormat")
    private fun setViews() {
        //checking for user info
        lifecycleScope.launch {
            if (UserProvider.getLoggedInUser(requireContext()) == null) {
                lifecycleScope.launch {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.logged_out),
                        Toast.LENGTH_SHORT
                    ).show()
                    UserProvider.removeCurrentLoggedInUser(requireContext())
                    findNavController().popBackStack(R.id.loginFragment, true)
                    findNavController().navigate(R.id.loginFragment)
                }
            }
        }
        val book = Gson().fromJson(
            requireArguments().getString(BookUtils.BOOK_ITEM_KEY),
            BookModel::class.java
        )
        binding.apply {
            book.apply {
                fragmentBookDetailTvTitle.text = title
                fragmentBookTvAlias.text = alias
                fragmentBookTvHits.text = hits.toString()
                fragmentBookDetailIvBook.load(image)
                val date = Date(lastChapterDate!!.toLong())
                val simpleDateFormat = SimpleDateFormat(getString(R.string.yyyy_mm_dd))
                val formattedDate = simpleDateFormat.format(date)
                fragmentBookTvUpdatedOn.text = formattedDate
                isFav?.let {
                    if (it) fragmentBookDetailIvFav.imageTintList = ColorStateList.valueOf(
                        root.context.getColor(
                            R.color.color01939A
                        )
                    )
                    else fragmentBookDetailIvFav.imageTintList = ColorStateList.valueOf(Color.WHITE)
                }
                fragmentBookDetailIvFav.setOnClickListener {
                    if (fragmentBookDetailIvFav.imageTintList == ColorStateList.valueOf(
                            root.context.getColor(
                                R.color.color01939A
                            )
                        )
                    ) fragmentBookDetailIvFav.imageTintList = ColorStateList.valueOf(root.context.getColor(R.color._878787))
                    else fragmentBookDetailIvFav.imageTintList =
                        ColorStateList.valueOf(root.context.getColor(R.color.color01939A))
                }
                fragmentBookDetailIvBack.setOnClickListener {
                    findNavController().popBackStack()
                }
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