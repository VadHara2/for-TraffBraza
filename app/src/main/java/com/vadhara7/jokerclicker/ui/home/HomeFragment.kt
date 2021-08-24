package com.vadhara7.jokerclicker.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val vibro = (requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)

        binding.apply {
            lifecycleOwner = this@HomeFragment
            viewmodel = viewModel
            navigationBar.layoutParams.height = getNavigationBarHeight()
            statusBar.layoutParams.height = getStatusBarHeight()
            imageView.setOnTouchListener { v, event ->

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibro.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
                        }else{
                            vibro.vibrate(100)
                        }

                        cardView.scaleY = 0.9f
                        cardView.scaleX = 0.9f
                        cardView.invalidate()
                        Log.i(TAG, "onCreateView: DOWN")
                    }

                    MotionEvent.ACTION_UP -> {
                        cardView.scaleY = 1f
                        cardView.scaleX = 1f
                        cardView.invalidate()
                        Log.i(TAG, "onCreateView: UP")
                    }
                }

                false
            }
        }

        viewModel.apply {
            currentProgress.value = loadNumber()
            changePhoto()
            showInterstitial.observe(viewLifecycleOwner, Observer {
                if (it && mInterstitialAd != null) {
                    mInterstitialAd?.show(requireActivity())
                    setUpInterstitialAd()
                }
            })
            nextLevelMessage.observe(viewLifecycleOwner, Observer {
                if (it != "0") {
                    Toast.makeText(requireContext(),"$it", Toast.LENGTH_LONG).show()
                }
            })
        }

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


    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun getNavigationBarHeight(): Int {
        var result = 0
        val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

}