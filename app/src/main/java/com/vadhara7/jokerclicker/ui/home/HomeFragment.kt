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
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.vadhara7.jokerclicker.R
import com.vadhara7.jokerclicker.databinding.FragmentHomeBinding

private const val TAG = "HomeFragment"

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var mAdView: AdView
    private var mInterstitialAd: InterstitialAd? = null

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
        viewModel.showInterstitial.observe(viewLifecycleOwner, Observer {
            if (it && mInterstitialAd != null) {
                mInterstitialAd?.show(requireActivity())
                setUpInterstitialAd()
            }
        })

        MobileAds.initialize(requireActivity())
        mAdView = binding.adView
        setUpBanner()
        setUpInterstitialAd()

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        saveNumber(viewModel.currentProgress.value ?: 0)
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


    private fun setUpBanner() {
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    private fun setUpInterstitialAd() {
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            requireContext(),
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.message)
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                }

            })

    }

}