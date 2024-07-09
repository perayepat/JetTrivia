package com.example.jettrivia.repository

import android.provider.ContactsContract.Data
import android.util.Log
import com.example.jettrivia.data.DataOrException
import com.example.jettrivia.model.Question
import com.example.jettrivia.model.QuestionItem
import com.example.jettrivia.network.QuestionApi
import javax.inject.Inject
import java.lang.Exception

class QuestionRepository @Inject constructor(
    private val api: QuestionApi){

    private val dataOrException = DataOrException<ArrayList<QuestionItem>, Boolean, Exception>()
    suspend fun getAllQuestions(): DataOrException<ArrayList<QuestionItem>, Boolean, Exception> {
        try {
            // We can let the user know things are loading and allow them to add UI for this
            dataOrException.loading = true
            dataOrException.data = api.getAllQuestions()
            // Whoever is consuming this can see that we are done loading
            if (dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false

        } catch (exception: Exception) {
            dataOrException.e = exception
            dataOrException.loading = false
            Log.d("getAllQuestions", "getAllQuestions: ${dataOrException.e.toString()} ")
        }
        return dataOrException
    }
}