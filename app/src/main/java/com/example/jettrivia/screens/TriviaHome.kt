package com.example.jettrivia.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.AnnotatedString
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
import com.example.jettrivia.model.QuestionItem
import com.example.jettrivia.util.AppColours

@Composable
fun TriviaHome(viewModel: QuestionsViewModel = hiltViewModel()){
    Questions(viewModel)
}

@Composable
fun Questions(viewmodel: QuestionsViewModel){
    // The type that we have in our question is an array list
    val questions = viewmodel.data.value.data?.toMutableList()
    val questionIndex = remember {
        mutableStateOf(0)
    }

    if (viewmodel.data.value.loading == true) {
        CircularProgressIndicator()
    } else {
        val question = try {
            questions?.get(questionIndex.value)
        } catch (ex: Exception){
            null
        }

        if (questions != null) {
            QuestionDisplay(
                question = question!!,
                questionIndex = questionIndex,
                viewModel = viewmodel){
                questionIndex.value = questionIndex.value + 1
            }
        }
    }
}

//@Preview
@Composable
fun QuestionDisplay(
    question: QuestionItem,
    questionIndex: MutableState<Int>,
    viewModel: QuestionsViewModel,
    onNextClicked: (Int) -> Unit = {}
) {
    val surfaceModifier = Modifier
        .fillMaxSize()
    val columnModifier = Modifier.padding(12.dp)

    val choicesState = remember(question) {
       question.choices.toMutableList()
    }

    val answerState = remember(question) {
        mutableStateOf<Int?>(null)
    }

    val correctAnswerState = remember(question) {
        mutableStateOf<Boolean?>(null)
    }

    val updateAnswer: (Int) -> Unit = remember(question) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == question.answer
        }
    }

    Surface(
        modifier = surfaceModifier,
        color = AppColours.mDarkPurple
    ) {
        Column(
            modifier = columnModifier,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            QuestionsTracker(counter = questionIndex.value, outOf = viewModel.numberOfQuestions.value)
            DrawDottedLine()
            QuestionToAnswer(text = question.question)
            //Choices
            choicesState.forEachIndexed { index, answerText ->
                Row(modifier = CustomRowModifier(),
                    verticalAlignment = Alignment.CenterVertically){
                    RadioButton(
                        modifier = Modifier
                            .padding(start = 16.dp),
                        colors =  RadioButtonDefaults.colors(
                            selectedColor = if (correctAnswerState.value == true && index == answerState.value) {
                                Color.Green
                            } else if (correctAnswerState.value == false && index == answerState.value) {
                                Color.Red
                            }
                            else {
                                AppColours.mOffWhite
                            }
                        ),
                        selected = (answerState.value == index),
                        onClick = {
                            updateAnswer(index)
                        }
                    )
                    Text(text = annotatedString(correctAnswerState, index, answerState, answerText))
                }
            }
            Button(
                modifier = Modifier
                    .padding(3.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                shape = RoundedCornerShape(34.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColours.mLightBlue),
                onClick = { onNextClicked(questionIndex.value)}) {
                Text(text = "Next",
                    modifier = Modifier.padding(4.dp),
                    color = AppColours.mOffWhite,
                    fontSize = 17.sp)
            }
        }
    }
}

@Composable
private fun annotatedString(
    correctAnswerState: MutableState<Boolean?>,
    index: Int,
    answerState: MutableState<Int?>,
    answerText: String
): AnnotatedString {
    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Light,
                color = if (correctAnswerState.value == true && index == answerState.value) {
                    Color.Green
                } else if (correctAnswerState.value == false && index == answerState.value) {
                    Color.Red
                } else {
                    AppColours.mOffWhite
                }, fontSize = 17.sp
            )
        ) {
            append(answerText)
        }
    }
    return annotatedString
}


fun CustomRowModifier() :Modifier {
    val rowModifier = Modifier
        .padding(3.dp)
        .fillMaxWidth()
        .height(45.dp)
        .border(
            width = 4.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    AppColours.mOffDarkPurple,
                    AppColours.mOffDarkPurple
                )
            ),
            shape = RoundedCornerShape(15.dp)
        )
        .clip(
            RoundedCornerShape(
                topStartPercent = 50,
                topEndPercent = 50,
                bottomEndPercent = 50,
                bottomStartPercent = 50
            )
        )
        .background(Color.Transparent)
    return  rowModifier
}

@Composable
private fun QuestionToAnswer(text: String = "Question goes here") {
    Column {
        Text(
            text = text,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = AppColours.mOffWhite,
            modifier = Modifier
                .padding(6.dp)
                .align(alignment = Alignment.Start)
                .fillMaxHeight(0.3f)
        )
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
