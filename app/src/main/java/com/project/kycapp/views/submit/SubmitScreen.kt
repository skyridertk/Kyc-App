package com.project.kycapp.views.submit


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.project.kycapp.R
import com.project.kycapp.models.Gender
import com.project.kycapp.views.login.CustomField
import com.project.kycapp.views.submit.components.CameraCapture
import com.project.kycapp.views.submit.components.GallerySelect
import com.project.kycapp.views.submit.components.showDatePicker
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


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

    var toggleDob by remember {
        mutableStateOf(false)
    }
    val scrollState: ScrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        scrollState.animateScrollTo(10000)

        submitViewModel.eventFlow.collect {
            when (it) {
                is SubmitViewModel.UIEvent.Error -> {
                    Toast.makeText(current, it.message, Toast.LENGTH_LONG).show()
                }
                is SubmitViewModel.UIEvent.Main -> {
//                    navHostController.navigate("dashboard"){
//                        popUpTo("dashboard")
//                        launchSingleTop = true
//                    }

                    Toast.makeText(current, "Has been saved", Toast.LENGTH_LONG).show()

                    submitViewModel.onEvent(SubmitEvents.Clear)
                }
            }
        }
    }

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
            scrollState = scrollState,
            toggleDob = toggleDob,
            onToggleDateOfBirth = {
                toggleDob = it
            },
            loading = viewModelState.loading,
            proofResPreview = viewModelState.proofResPreview,
            proofIdPreview = viewModelState.proofIdPreview,
            onTogglePreviewRes = {
                scope.launch {
                    submitViewModel.onEvent(SubmitEvents.ChangeResidencePreview(it))
                }
            },
            onTogglePreviewId = {
                scope.launch {
                    submitViewModel.onEvent(SubmitEvents.ChangeIDPreview(it))
                }
            }
        )
    }
}


@OptIn(DelicateCoroutinesApi::class)
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
    toggleDob: Boolean,
    onClick: (SubmitEvents) -> Unit,
    onChangeExpandedState: (Boolean) -> Unit,
    onToggleGalleryButton: (Boolean) -> Unit,
    onToggleCameraButton: (Boolean) -> Unit,
    onToggleDateOfBirth: (Boolean) -> Unit,
    scrollState: ScrollState,
    loading: Boolean,
    proofResPreview: Boolean,
    proofIdPreview: Boolean,
    onTogglePreviewRes: (Boolean) -> Unit,
    onTogglePreviewId: (Boolean) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        val (container, buttonContainer, loader) = createRefs()

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


            Box {
                Column(modifier = Modifier) {

                    Button(onClick = {
                        onToggleDateOfBirth(true)
                    }) {
                        Text(text = if(dateOfBirth == "") "Pick DOB" else "DOB: $dateOfBirth")
                    }

                    if (toggleDob) {
                        showDatePicker(context = context, onClick = {
                            onClick(SubmitEvents.ChangeDateOfBirth(it))
                        }, onDismiss = {
                            onToggleDateOfBirth(false)
                        })
                    }
                }
            }

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
                    if (proofResPreview) {
                        ImagePreviewLoad()
                    } else {
                        if(proofResidence != ""){
                            val file = File(Uri.parse(proofResidence).toFile().toURI())
                            val filePath: String = file.path
                            val bitmap = BitmapFactory.decodeFile(filePath)
                            ImagePreview(bitmap = bitmap)
                        }
                    }

                    Button(onClick = {
                        onToggleGalleryButton(true)
                    }) {
                        Text("Pick Proof Of Residence")
                    }
                    if (open) {
                        GallerySelect(
                            modifier = Modifier,
                            onImageUri = { uri ->
                                onToggleGalleryButton(false)
                                onTogglePreviewRes(true)

                                GlobalScope.launch(Dispatchers.IO) {

                                    val image =
                                        ImageDecoder.createSource(context.contentResolver, uri)
                                    val bitmap = ImageDecoder.decodeBitmap(image)

                                    val file =
                                        bitmapToFile(bitmap, "${System.currentTimeMillis()}.png")

                                    Compressor.compress(context, file!!) {
                                        default()
                                        destination(file)
                                    }

                                    onClick(
                                        SubmitEvents.ChangeProofOfResidence(
                                            file.toURI().toString()
                                        )
                                    )

                                    onTogglePreviewRes(false)

                                }
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
                    if (proofIdPreview) {
                        ImagePreviewLoad()
                    } else {
                        if(proofOfId != ""){
                            val file = File(Uri.parse(proofOfId).toFile().toURI())
                            val filePath: String = file.path
                            val bitmap = BitmapFactory.decodeFile(filePath)
                            ImagePreview(bitmap = bitmap)
                        }

                    }

                    Button(onClick = {
                        onToggleCameraButton(true)
                    }) {
                        Text("Capture NationalID")
                    }

                    if (cameraOpen) {
                        CameraCapture(
                            modifier = Modifier,
                            onImageFile = { file ->
                                onToggleCameraButton(false)
                                onTogglePreviewId(true)

                                GlobalScope.launch(Dispatchers.IO) {
                                    val image = ImageDecoder.createSource(
                                        context.contentResolver,
                                        file.toUri()
                                    )
                                    val bitmap = ImageDecoder.decodeBitmap(image)

                                    val file =
                                        bitmapToFile(bitmap, "${System.currentTimeMillis()}.png")

                                    val compressedImageFile = Compressor.compress(context, file!!) {
                                        default()
                                        destination(file)
                                    }

                                    onClick(SubmitEvents.ChangeProofOfId(file.toUri().toString()))
                                    onTogglePreviewId(false)
                                }
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
                onClick = {
                    onClick(SubmitEvents.Submit)
                },
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .constrainAs(loader) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    top.linkTo(parent.top)
                }
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(200.dp)
                        .align(Alignment.Center),
                )
            }

        }
    }
}

@Composable
fun ImagePreviewLoad() {
    Box(
        modifier = Modifier
            .size(250.dp)
            .background(Color.LightGray)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun ImagePreview(bitmap: Bitmap) {
    Box(
        modifier = Modifier
            .size(250.dp)
            .background(Color.LightGray)
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null
        )
    }
}

fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
    //create a file to write bitmap data
    var file: File? = null
    return try {
        val path = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )

        path.mkdirs();
        file = File(path.toString() + File.separator + fileNameToSave)
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
        val bitmapdata = bos.toByteArray()

        //write the bytes in file
        val fos = FileOutputStream(file)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
        file
    } catch (e: Exception) {
        e.printStackTrace()
        file // it will return null
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
                scrollState = rememberScrollState(),
                toggleDob = false,
                onToggleDateOfBirth = {},
                loading = true,
                proofIdPreview = false,
                proofResPreview = false,
                onTogglePreviewId = {},
                onTogglePreviewRes = {}
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