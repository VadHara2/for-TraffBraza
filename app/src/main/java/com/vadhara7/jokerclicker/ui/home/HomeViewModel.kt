package com.vadhara7.jokerclicker.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vadhara7.jokerclicker.R

class HomeViewModel : ViewModel() {

    private val images = listOf<Int>(
        R.drawable.img_first_level,
        R.drawable.img_second_level,
        R.drawable.img_third_level,
        R.drawable.img_fourth_level,
        R.drawable.img_you_win
    )

    val currentProgress = MutableLiveData<Int>(0)
    val mainImageUrl = MutableLiveData<Int>()
    val showInterstitial = MutableLiveData<Boolean>(false)
    val nextLevelMessage = MutableLiveData<String>("0")

    fun onImageClick() {
        currentProgress.value = currentProgress.value?.plus(1)
        changePhoto()
        if (currentProgress.value!! > 7777) {
            currentProgress.value = 0
        }
        showInterstitial.value = currentProgress.value!! % 50 == 0

        when (currentProgress.value) {
            8 -> nextLevelMessage.value = "Next Level"
            78 -> nextLevelMessage.value = "Next Level"
            778 -> nextLevelMessage.value = "Next Level"
            0 -> nextLevelMessage.value = "Restart a game"
            else -> "0"
        }
    }

    fun changePhoto() {
        val url = when (currentProgress.value) {
            in 0..6 -> images[0]
            in 7..76 -> images[1]
            in 77..776 -> images[2]
            in 777..7776 -> images[3]
            7777 -> images[4]
            else -> images[0]
        }

        mainImageUrl.value = url
    }

}