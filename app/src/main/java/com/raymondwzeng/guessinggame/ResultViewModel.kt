package com.raymondwzeng.guessinggame

import androidx.lifecycle.ViewModel

class ResultViewModel(finalResult : String) : ViewModel() {
    val result = finalResult
}