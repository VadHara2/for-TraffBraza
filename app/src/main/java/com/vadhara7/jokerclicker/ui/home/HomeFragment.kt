package com.vadhara7.jokerclicker.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.vadhara7.jokerclicker.R
import com.vadhara7.jokerclicker.databinding.FragmentHomeBinding

private const val TAG = "HomeFragment"

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)


        binding.apply {
            lifecycleOwner = this@HomeFragment
            viewmodel = viewModel
        }

        viewModel.currentProgress.value = loadNumber()
        viewModel.changePhoto()
        return binding.root
    }

    private fun saveNumber(number: Int) {
        val sharedPreferences =
            requireActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        Log.i(TAG, "saveNumber: $number")
        editor.apply {
            putInt("CLICKS", number)
        }.apply()
    }

    private fun loadNumber() =
        requireActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
            .getInt("CLICKS", 0)

    override fun onPause() {
        super.onPause()
        saveNumber(viewModel.currentProgress.value ?: 0)
    }

}