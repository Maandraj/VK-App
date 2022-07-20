package com.maandraj.album_impl.ui.album

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.maandraj.album_impl.R
import com.maandraj.album_impl.domain.model.Photo
import com.maandraj.album_impl.domain.model.Photos
import com.maandraj.album_impl.ui.album.image.ZoomableImage
import com.maandraj.auth_api.AuthFeatureApi
import com.maandraj.core.data.result.ResultOf
import com.maandraj.core.utils.constants.KEY_SNACKBAR
import com.maandraj.core.utils.extensions.popSaveStateNavigation
import com.maandraj.core_ui.cardShapePhoto
import com.maandraj.core_ui.colors.PrimaryColor
import com.maandraj.core_ui.colors.SecondColor
import com.maandraj.core_ui.imageHeightGrid
import com.maandraj.core_ui.imageWidthGrid
import com.maandraj.core_ui.samples.DialogSample
import com.maandraj.core_ui.samples.TextButtonView
import com.maandraj.core_ui.samples.loadImage
import kotlinx.coroutines.launch

@Composable
fun AlbumScreen(
    navController: NavHostController,
    viewModel: AlbumScreenVM,
    authFeatureApi: AuthFeatureApi,
    modifier: Modifier,
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val isLogout: ResultOf<Boolean> by viewModel.isLogout.observeAsState(ResultOf.Success(false))
    val isSavePhoto: ResultOf<Boolean> by viewModel.isSavePhoto.observeAsState(ResultOf.Success(
        false))
    val albumScope = rememberCoroutineScope()
    val (photos, setPhotos) = remember { mutableStateOf(Photos(mutableListOf())) }
    viewModel.getAllPhotos {
        when (it) {
            is ResultOf.Success -> {
                with(it.value) {
                    setPhotos(this)
                }
            }
            is ResultOf.Failure -> {
                albumScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }
    val downloadPainter = painterResource(id = R.drawable.ic_cloud_download)
    val (isOpenController, setOpenController) = remember { mutableStateOf(false) }
    val (isPainter, setPainter) = remember { mutableStateOf(downloadPainter) }
    val (isPhoto, setPhoto) = remember { mutableStateOf(Photo(-1, "", null, "")) }

    if (isPhoto.date != -1 && isOpenController)
        FullScreenController(
            painter = isPainter,
            photo = isPhoto,
            scaffoldState = scaffoldState,
            context = context,
            viewModel = viewModel) {
            setOpenController(false)
        }
    DataSetObservables(navController = navController,
        context = context,
        scaffoldState = scaffoldState,
        isLogout = isLogout,
        isSavePhoto = isSavePhoto,
        route = authFeatureApi.route())
    if (!isOpenController)
        AlbumContent(modifier = modifier,
            scaffoldState = scaffoldState,
            photos = photos,
            viewModel = viewModel,
            setOpenController = {
                setOpenController(it)
            }, setPainter = {
                setPainter(it)
            }, setPhoto = {
                setPhoto(it)
            })


}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun AlbumContent(
    modifier: Modifier,
    scaffoldState: ScaffoldState,
    viewModel: AlbumScreenVM,
    photos: Photos,
    setPainter: (painter: Painter) -> Unit,
    setPhoto: (photo: Photo) -> Unit,
    setOpenController: (isOpen: Boolean) -> Unit,
) {
    val title = stringResource(R.string.album_photo_download)
    val text = stringResource(R.string.album_text_dialog_out)
    val confirm = stringResource(R.string.album_confirm_dialog)
    val dismiss = stringResource(R.string.album_dismiss_dialog)
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    if (showDialog)
        DialogSample(title = title,
            text = text,
            confirmButtonText = confirm,
            dismissButtonText = dismiss,
            showDialog) {
            if (it) {
                viewModel.logout()
            }
            setShowDialog(false)
        }
    Scaffold(modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd) {
                TextButtonView(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(id = R.string.album_logout_button),
                    onClick = {
                        setShowDialog(true)
                    })
            }
        },
        content = {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                cells = GridCells.Fixed(2),
                contentPadding = PaddingValues(5.dp)
            ) {
                items(photos.items.size) { itemIndex ->
                    val item = photos.items[itemIndex]
                    val errorHolder = painterResource(id = R.drawable.ic_error)
                    val placeholder = painterResource(id = R.drawable.ic_cloud_download)
                    val painter = loadImage(item.srcUrl,
                        placeholder = placeholder,
                        errorHolder = errorHolder)
                    Card(
                        modifier = Modifier
                            .padding(4.dp)
                            .border(width = 0.5.dp,
                                color = PrimaryColor,
                                shape = cardShapePhoto),
                        shape = cardShapePhoto,
                        onClick = {
                            setPhoto(item)
                            setPainter(painter)
                            setOpenController(true)
                        }
                    ) {
                        Image(
                            painter = painter,
                            contentScale = ContentScale.FillBounds,
                            contentDescription = item.text ?: "Photo",
                            modifier = Modifier.size(imageWidthGrid.dp, imageHeightGrid.dp)
                        )
                    }
                }
            }

        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("ObsoleteSdkInt")
@Composable
private fun FullScreenController(
    painter: Painter,
    scaffoldState: ScaffoldState,
    photo: Photo,
    context: Context,
    viewModel: AlbumScreenVM,
    closeClick: (controllerClick: Unit) -> Unit,
) {
    val (progress, setProgress) = remember { mutableStateOf(0) }
    val (progressVisible, setProgressVisible) = remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize()) {
        Text(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
            textAlign = TextAlign.Center,
            text = photo.dateString)

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .border(width = 0.5.dp,
                    color = PrimaryColor,
                    shape = cardShapePhoto),
            shape = cardShapePhoto,
        ) {
            var expanded by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()
            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    viewModel.savePhoto(url = photo.srcUrl, context = context) {
                        setProgress(it)
                    }
                } else {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.error_not_accept_download_permission))
                    }
                }
            }
            Column(modifier = Modifier.fillMaxSize()) {
                AnimatedVisibility(visible = progressVisible,
                    enter = expandHorizontally(),
                    exit = shrinkHorizontally()) {
                    val percent = (progress / 120).toFloat()
                    LinearProgressIndicator(
                        progress = percent,
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = PrimaryColor,
                        backgroundColor = SecondColor
                    )
                }
                setProgressVisible(progress != 0)

                ZoomableImage(painter = painter,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop)
            }
            Box(contentAlignment = Alignment.TopStart) {
                IconButton(onClick = { closeClick(Unit) }) {
                    Icon(Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.album_exit_controller_description))
                }
            }
            Box(contentAlignment = Alignment.TopEnd) {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.album_show_menu_description))
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        Text(stringResource(R.string.album_menu_save_item_text),
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable(onClick = {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                                        askStoragePermission(context = context, launcher) {
                                            viewModel.savePhoto(url = photo.srcUrl,
                                                context = context) {
                                                setProgress(it)
                                            }
                                        }
                                    } else {
                                        viewModel.savePhoto(url = photo.srcUrl, context = context) {
                                            setProgress(it)
                                        }
                                    }
                                    expanded = false
                                }))
                        Divider()
                        Text(stringResource(R.string.album_menu_share_item_text),
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable(onClick = {
                                    shareImage(context = context, url = photo.srcUrl)
                                    expanded = false
                                }))
                    }
                }
            }
        }
    }
}


private fun shareImage(context: Context, url: String) {
    val shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND
    shareIntent.type = "text/plain"
    shareIntent.putExtra(Intent.EXTRA_TEXT, url)
    context.startActivity(Intent.createChooser(shareIntent,
        context.getString(R.string.album_show_menu_description)))
}

private fun askStoragePermission(
    context: Context,
    launcher: ManagedActivityResultLauncher<String, Boolean>,
    granted: (granted: Unit) -> Unit,
) {
    when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ),
        -> {
            granted(Unit)
        }
        else -> {
            launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }
}


@Composable
private fun DataSetObservables(
    navController: NavHostController,
    context: Context,
    scaffoldState: ScaffoldState,
    route: String,
    isLogout: ResultOf<Boolean>,
    isSavePhoto: ResultOf<Boolean>,
) {
    isLogout.let {
        when (it) {
            is ResultOf.Success -> {
                if (it.value) {
                    navController.popSaveStateNavigation(route = route,
                        navController.graph.startDestinationId, save = false)
                }
            }
            is ResultOf.Failure -> {
                LaunchedEffect(KEY_SNACKBAR) {
                    scaffoldState.snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }
    isSavePhoto.let {
        when (it) {
            is ResultOf.Success -> {
                if (it.value)
                    LaunchedEffect(KEY_SNACKBAR) {
                        scaffoldState.snackbarHostState.showSnackbar(context.getString(R.string.album_photo_download))
                    }
            }
            is ResultOf.Failure -> {
                LaunchedEffect(KEY_SNACKBAR) {
                    scaffoldState.snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }
}