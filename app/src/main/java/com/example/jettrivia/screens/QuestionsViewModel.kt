package com.example.jettrivia.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jettrivia.data.DataOrException
import com.example.jettrivia.model.QuestionItem
import com.example.jettrivia.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(private val repository: QuestionRepository) : ViewModel() {
    //This view model will be used within our composables and need a state
    val data: MutableState<DataOrException<ArrayList<QuestionItem>, Boolean, Exception>>
    = mutableStateOf(DataOrException(null, true, Exception("no data")))

    val numberOfQuestions = mutableStateOf(0)

    init {
        getAllQuestions()

    }

    private fun shuffleQuestions() {
        data.value.data?.let {
            it.shuffle()
            data.value = DataOrException(ArrayList(it.take(10)), false, null)
        }
    }

    private fun getAllQuestions(){
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllQuestions()
            if (data.value.data.toString().isNotEmpty()) {
                data.value.loading = false
                shuffleQuestions()
                numberOfQuestions.value = data.value.data?.size!!
            }
        }
    }

}