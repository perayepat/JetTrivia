package com.example.jettrivia.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jettrivia.util.AppColours

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

@Preview
@Composable
fun QuestionDisplay() {
    val surfaceModifier = Modifier
        .fillMaxSize()
        .padding(4.dp)
    val columnModifier = Modifier.padding(12.dp)


    Surface(
        modifier = surfaceModifier,
        color = AppColours.mDarkPurple
    ) {
        Column(
            modifier = columnModifier,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            QuestionsTracker()
            DrawDottedLine()
        }
    }
}
@Composable
fun DrawDottedLine(pathEffect: PathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)) {
    androidx.compose.foundation.Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)){
        drawLine(color = AppColours.mLightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, y = 0f),
            pathEffect = pathEffect)
    }
}

@Composable
fun QuestionsTracker(counter: Int = 10, outOf: Int = 100){
    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)){
            withStyle(style = SpanStyle(color = AppColours.mLightGray,
                fontWeight = FontWeight.Bold,
                fontSize = 27.sp)){
                append("Question $counter/")
                withStyle(style = SpanStyle(color = AppColours.mLightGray,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )){
                    append("$outOf")
                }
            }
        }
    },
        modifier = Modifier
            .padding(20.dp))

}
