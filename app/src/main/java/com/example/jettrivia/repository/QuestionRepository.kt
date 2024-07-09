package com.example.jettrivia.repository

import android.provider.ContactsContract.Data
import com.example.jettrivia.data.DataOrException
import com.example.jettrivia.model.Question
import com.example.jettrivia.model.QuestionItem
import com.example.jettrivia.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(
    private val api: QuestionApi,
    private val listOfQuestions: DataOrException<ArrayList<QuestionItem>, Boolean, Exception>
    = DataOrException<ArrayList<QuestionItem>, Boolean, Exception>()) {

}