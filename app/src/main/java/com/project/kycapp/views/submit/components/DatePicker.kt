package com.project.kycapp.views.submit.components

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import java.util.*

@Composable
fun showDatePicker(context: Context, onClick: (String) -> Unit){

    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val datePickerDialog = DatePickerDialog(
        context,
        {_: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            onClick("$dayOfMonth/$month/$year")
        }, year, month, day
    )

    datePickerDialog.show()

}

@Preview
@Composable
fun RenderDatePicker() {
    MaterialTheme{
        Surface {
            var state by remember {
                mutableStateOf(false)
            }

            Button(onClick = { state = true }) {
                Text("Toggle")
            }

            if(state){
                showDatePicker(context = LocalContext.current,  onClick = {
                    Log.d("RenderDatePicker", "RenderDatePicker: $it")
                    state = false
                })
            }
        }
    }
}