package com.project.kycapp.views.detail

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
            dob = viewModelState.dateOfBirth,
            proofOfResidence = viewModelState.proofOfResidence,
            proofOfId = viewModelState.proofOfId,
            assetId = viewModelState.assetID
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
    dob: String,
    proofOfResidence: String,
    proofOfId: String,
    assetId: String
) {
    val color = when (status) {
        Pending.PENDING -> {
            Color(0xFF87ceeb)
        }
        Pending.APPROVED -> {
            Color(0xFF90EE90)
        }
        else -> {
            Color(0xFFFF7F7F)
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(color)
                .padding(horizontal=16.dp, vertical=24.dp)
        ) {
            DetailItem(value = idNumber, label = "AssetID", style = MaterialTheme.typography.h3)
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                DetailItem(
                    modifier = Modifier.weight(2F),
                    value = fname,
                    label = "first name",
                    maxLines = 2
                )
                DetailItem(modifier = Modifier.weight(2F), value = lname, label = "surname")
            }
            DetailItem(value = phone, label = "phone")
            DetailItem(value = address, label = "address", maxLines = 3)
            DetailItem(value = idNumber, label = "idNumber")
            DetailItem(value = dob, label = "date of birth")
            DetailItem(value = status.name, label = "asset status")
        }

    }
}

@Composable
private fun DetailItem(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    style: TextStyle = MaterialTheme.typography.h5,
    maxLines: Int = 1
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            "$label", style = MaterialTheme.typography.caption,
            color = Color.DarkGray,
            maxLines = maxLines
        )
        Text(
            value,
            style = style,
            color = Color.Black
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
        dob = "29/10/2002",
        proofOfResidence = "",
        proofOfId = "",
        assetId = "sdfsdfdsf1231231"
    )
}