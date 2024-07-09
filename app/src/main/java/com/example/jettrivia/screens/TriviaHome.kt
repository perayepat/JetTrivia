package com.example.jettrivia.screens

import android.util.Log
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TriviaHome(viewModel: QuestionsViewModel = hiltViewModel()){
    Questions(viewModel)
}

@Composable
fun Questions(viewmodel: QuestionsViewModel){
    // The type that we have in our question is an array list
    val questions = viewmodel.data.value.data?.toMutableList()
    if (viewmodel.data.value.loading == true) {
        CircularProgressIndicator()
    } else {
        Log.d("Questions Composable", "Questions: ${questions?.size} ")
    }

}