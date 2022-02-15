package com.project.kycapp.views.submit


import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.project.kycapp.R
import com.project.kycapp.models.Gender
import com.project.kycapp.views.login.CustomField
import com.project.kycapp.views.submit.components.CameraCapture
import com.project.kycapp.views.submit.components.DatePickerView
import com.project.kycapp.views.submit.components.GallerySelect
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SubmitScreen(
    navHostController: NavHostController = rememberAnimatedNavController(),
    submitViewModel: SubmitViewModel = hiltViewModel()
) {

    val viewModelState by submitViewModel.state.collectAsState()

    var expandedGenderState by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    val current = LocalContext.current

    val state = rememberScaffoldState()

    var open by remember {
        mutableStateOf(false)
    }

    var cameraOpen by remember {
        mutableStateOf(false)
    }
    val scrollState: ScrollState = rememberScrollState()

    LaunchedEffect(Unit) { scrollState.animateScrollTo(10000) }

    Scaffold(scaffoldState = state, topBar = {
        TopAppBar(
            title = {
                Text("Submit", color = MaterialTheme.colors.primary)
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
        SubmitDetail(
            context = current,
            firstName = viewModelState.firstName,
            surname = viewModelState.surname,
            idNumber = viewModelState.idNumber,
            phoneNumber = viewModelState.phoneNumber,
            address = viewModelState.address,
            dateOfBirth = viewModelState.dateOfBirth,
            gender = viewModelState.gender,
            expandedGenderState = expandedGenderState,
            open = open,
            cameraOpen = cameraOpen,
            proofResidence = viewModelState.proofOfResidence,
            proofOfId = viewModelState.proofOfId,
            onClick = {
                scope.launch {
                    submitViewModel.onEvent(it)
                }
            },
            onChangeExpandedState = {
                expandedGenderState = it
            },
            onToggleGalleryButton = {
                open = it
            },
            onToggleCameraButton = {
                cameraOpen = it
            },
            scrollState = scrollState
        )
    }
}


@Composable
fun SubmitDetail(
    context: Context,
    firstName: String,
    surname: String,
    idNumber: String,
    phoneNumber: String,
    address: String,
    dateOfBirth: String,
    gender: Gender,
    expandedGenderState: Boolean,
    open: Boolean,
    cameraOpen: Boolean,
    proofResidence: String,
    proofOfId: String,
    onClick: (SubmitEvents) -> Unit,
    onChangeExpandedState: (Boolean) -> Unit,
    onToggleGalleryButton: (Boolean) -> Unit,
    onToggleCameraButton: (Boolean) -> Unit,
    scrollState: ScrollState
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        val (container, buttonContainer) = createRefs()

        Column(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .constrainAs(container) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(buttonContainer.top, 16.dp)
                    width = Dimension.fillToConstraints
                }
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            CustomField(value = firstName, label = "FirstName", onchange = {
                onClick(SubmitEvents.ChangeFirstName(it))
            })

            CustomField(value = surname, label = "Surname", onchange = {
                onClick(SubmitEvents.ChangeSurname(it))
            })

            CustomField(value = idNumber, label = "idNumber", onchange = {
                onClick(SubmitEvents.ChangeIdNumber(it))
            })

            CustomField(value = phoneNumber, label = "Phone", onchange = {
                onClick(SubmitEvents.ChangePhoneNumber(it))
            })

            CustomField(value = address, label = "Address", onchange = {
                onClick(SubmitEvents.ChangeAddress(it))
            })

            DatePickerView()

            CustomField(value = dateOfBirth, label = "Date of Birth", onchange = {
                onClick(SubmitEvents.ChangeDateOfBirth(it))
            })

            IconButton(onClick = { onChangeExpandedState(true) }) {
                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_expand_more_24),
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(text = gender.name, color = MaterialTheme.colors.primary)
                }

                DropdownMenu(
                    expanded = expandedGenderState,
                    onDismissRequest = { onChangeExpandedState(false) }
                ) {
                    DropdownMenuItem(onClick = {
                        onChangeExpandedState(false)
                        onClick(SubmitEvents.ChangeGender(Gender.MALE))
                    }) {
                        Text(text = "Male", color = MaterialTheme.colors.primary)
                    }

                    DropdownMenuItem(onClick = {
                        onChangeExpandedState(false)
                        onClick(SubmitEvents.ChangeGender(Gender.FEMALE))
                    }) {
                        Text(text = "Female", color = MaterialTheme.colors.primary)
                    }
                }
            }

            Box {

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("You picked: $proofResidence", color = MaterialTheme.colors.primary)

                    Button(onClick = {
                        onToggleGalleryButton(true)
                    }) {
                        Text("Pick Proof Of Residence")
                    }
                    if (open) {
                        GallerySelect(
                            modifier = Modifier,
                            onImageUri = { uri ->
                                onClick(SubmitEvents.ChangeProofOfResidence(uri.toString()))
                                onToggleGalleryButton(false)
                            }
                        )
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                }
            }

            Box {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Your image location: $proofOfId", color = MaterialTheme.colors.primary)

                    Button(onClick = {
                        onToggleCameraButton(true)
                    }) {
                        Text("Capture NationalID")
                    }

                    if (cameraOpen) {
                        CameraCapture(
                            modifier = Modifier,
                            onImageFile = { file ->
                                onClick(SubmitEvents.ChangeProofOfId(file.toUri().toString()))
                                onToggleCameraButton(false)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.size(20.dp))
                }
            }


        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .constrainAs(buttonContainer) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, 2.dp)
                }
        ) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .align(Alignment.Center),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF475569))
            ) {
                Text(text = "Submit", color = MaterialTheme.colors.primary)
            }
        }
    }
}

@Preview
@Composable
fun RenderSubmit() {

    MaterialTheme {
        Surface {
            SubmitDetail(
                context = LocalContext.current,
                firstName = "Loveness",
                surname = "Jameson",
                idNumber = "62-219283X98",
                phoneNumber = "26377747474",
                address = "123 Green Area, Harare",
                dateOfBirth = "13/1/2002",
                gender = Gender.FEMALE,
                expandedGenderState = false,
                onClick = {},
                onChangeExpandedState = {},
                cameraOpen = false,
                open = false,
                onToggleCameraButton = {},
                onToggleGalleryButton = {},
                proofResidence = "",
                proofOfId = "",
                scrollState = rememberScrollState()
            )
        }
    }

}

@Preview
@Composable
fun RenderCamera(modifier: Modifier = Modifier) {
    MaterialTheme {
        Surface {
            val emptyImageUri = Uri.parse("file://dev/null")
            var imageUri by remember { mutableStateOf(emptyImageUri) }

            Box(modifier = modifier) {
                CameraCapture(
                    modifier = modifier,
                    onImageFile = { file ->
                        imageUri = file.toUri()
                    }
                )
                Button(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(4.dp),
                    onClick = {

                    }
                ) {
                    Text("Select from Gallery")
                }
            }
        }

    }
}