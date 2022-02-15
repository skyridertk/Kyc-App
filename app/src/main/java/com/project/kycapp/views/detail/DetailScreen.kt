package com.project.kycapp.views.detail

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.project.kycapp.R
import com.project.kycapp.models.Gender
import com.project.kycapp.models.Pending
import com.project.kycapp.views.browse.BoxState


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DetailScreen(
    navHostController: NavHostController = rememberAnimatedNavController(),
    detailViewModel: DetailViewModel = hiltViewModel()
) {
    val viewModelState by detailViewModel.state.collectAsState()

    val state = rememberScaffoldState()
    Scaffold(scaffoldState = state, topBar = {
        TopAppBar(
            title = {
                Text("Detail", color = MaterialTheme.colors.primary)
            },
            navigationIcon = {
                IconButton(onClick = {
                    navHostController.popBackStack()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            backgroundColor = MaterialTheme.colors.background,
            elevation = 2.dp
        )
    }) {
        DetailContent(
            fname = viewModelState.firstName,
            lname = viewModelState.surname,
            phone = viewModelState.phoneNumber,
            address = viewModelState.address,
            idNumber = viewModelState.idNumber,
            status = viewModelState.status,
            gender = viewModelState.gender,
            dob = viewModelState.dateOfBirth
        )
    }

}

@Composable
private fun DetailContent(
    fname: String,
    lname: String,
    phone: String,
    address: String,
    idNumber: String,
    status: Pending,
    gender: Gender,
    dob: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            when (status) {
                Pending.PENDING -> {
                    BoxState(text = "P", Color(0xFF87ceeb))
                }
                Pending.APPROVED -> {
                    BoxState(text = "A", Color(0xFF90EE90))
                }
                Pending.REJECTED -> {
                    BoxState(text = "R", Color(0xFFFF7F7F))
                }
            }
        }
        DetailItem(idNumber, "ID No", style = MaterialTheme.typography.h3)
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.LightGray)
        ) {

        }

        DetailItem(fname, "first name")
        DetailItem(lname, "surname")
        DetailItem(phone, "phone")
        DetailItem(address, "address")
        DetailItem(idNumber, "id")
        DetailItem(dob, "dob")
    }
}

@Composable
private fun DetailItem(
    idNumber: String,
    label: String,
    style: TextStyle = MaterialTheme.typography.h5
) {
    Column {
        Text(
            "$label", style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.primaryVariant
        )
        Text(
            idNumber,
            style = style,
            color = MaterialTheme.colors.primary
        )
    }
}

@Preview(backgroundColor = 0xFFFF00FF)
@Composable
fun DetailContentPreview() {
    MaterialTheme() {
        Surface {
            Client()
        }
    }
}

@Preview(backgroundColor = 0xFFFF00FF)
@Composable
fun DetailContentPreview2() {
    MaterialTheme() {
        Surface {
            Scaffold {
                Client()
            }
        }
    }
}

@Preview(backgroundColor = 0xFFFF00FF)
@Composable
fun DetailContentPreview3() {
    MaterialTheme() {
        Surface {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "Test") }
                    )
                }
            ) {
                Client()
            }
        }
    }
}

@Composable
private fun Client() {
    DetailContent(
        fname = "Loveness Mudungwe",
        lname = "Jamson",
        phone = "263784848598",
        address = "123 Green Area, Msasa",
        idNumber = "63-20393049X23",
        status = Pending.PENDING,
        gender = Gender.FEMALE,
        dob = "29/10/2002"
    )
}