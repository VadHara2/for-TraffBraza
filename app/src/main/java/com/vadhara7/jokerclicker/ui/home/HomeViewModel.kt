package com.vadhara7.jokerclicker.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val images = listOf<String>(
        "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/aaa79357-9476-4c1d-b9a8-6e881f2449d3/ddhiww4-06113dc6-a12e-4501-bcda-309994db832d.png?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOiIsImlzcyI6InVybjphcHA6Iiwib2JqIjpbW3sicGF0aCI6IlwvZlwvYWFhNzkzNTctOTQ3Ni00YzFkLWI5YTgtNmU4ODFmMjQ0OWQzXC9kZGhpd3c0LTA2MTEzZGM2LWExMmUtNDUwMS1iY2RhLTMwOTk5NGRiODMyZC5wbmcifV1dLCJhdWQiOlsidXJuOnNlcnZpY2U6ZmlsZS5kb3dubG9hZCJdfQ.xtLU1HyPCPblKvFKfnj9qLIYVzUqFCLNeLUyqzSUTkc",
        "https://yt3.ggpht.com/a/AGF-l7_No9kPSr9QfrW2KvF-is04ptBiRwlHLbe-Gg=s900-c-k-c0xffffffff-no-rj-mo",
        "https://www.comicsbeat.com/wp-content/uploads/2020/03/1960-Mattina-JOKER-80.jpg",
        "https://www.zoom-comics.com/wp-content/uploads/sites/36/2013/11/Joker-Smiles.jpg",
        "https://thumbs.dreamstime.com/b/you-win-congratulation-bright-glossy-banner-lettering-composition-99214005.jpg"
    )

    val currentProgress = MutableLiveData<Int>(0)
    val mainImageUrl = MutableLiveData<String>()
    val showInterstitial = MutableLiveData<Boolean>(false)

    fun onImageClick() {
        currentProgress.value = currentProgress.value?.plus(1)
        changePhoto()
        if (currentProgress.value!! > 7777) {
            currentProgress.value = 0
        }
        showInterstitial.value = currentProgress.value!! % 50 == 0

    }

    fun changePhoto() {
        val url = when (currentProgress.value) {
            in 0..7 -> images[0]
            in 8..77 -> images[1]
            in 78..777 -> images[2]
            in 778..7776 -> images[3]
            7777 -> images[4]
            else -> images[0]
        }

        mainImageUrl.value = url
    }

}