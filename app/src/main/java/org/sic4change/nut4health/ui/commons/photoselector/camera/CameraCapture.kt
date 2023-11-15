package org.sic4change.nut4health.ui.commons.photoselector.camera

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import java.io.File
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.sic4change.nut4health.R
import org.sic4change.nut4health.ui.commons.photoselector.Permission

@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalCoroutinesApi
@Composable
fun CameraCapture(
    modifier: Modifier = Modifier,
    onImageFile: (File) -> Unit = { }
) {

    val frontCameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()
    val backCameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

    var currentCameraSelector by remember { mutableStateOf(backCameraSelector) }
    var useFrontCamera by remember { mutableStateOf(false) }

    val cameraSwitchIcon: Painter = painterResource(id = R.drawable.ic_switch_camera)

    //var cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    val context = LocalContext.current
    Permission(
        permission = Manifest.permission.CAMERA,
        rationale = "Quieres acceder a la cámara de fotos. Necesitamos que aceptes el permiso.",
        permissionNotAvailableContent = {
            Column(modifier) {
                Text("O noes! No Camera!")
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        context.startActivity(
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                        )
                    }
                ) {
                    Text("Abrir ajustes")
                }
            }
        }
    ) {
        Box{
            val lifecycleOwner = LocalLifecycleOwner.current
            val coroutineScope = rememberCoroutineScope()
            var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }
            val imageCaptureUseCase by remember {
                mutableStateOf(
                    ImageCapture.Builder()
                        .setCaptureMode(CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setJpegQuality(20)
                        .build()
                )
            }
            Box {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    onUseCase = {
                        previewUseCase = it
                    }
                )

                Box(modifier = modifier.padding(16.dp).align(Alignment.BottomEnd)) {
                    Icon(painter = cameraSwitchIcon,
                        "Take",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(60.dp).background(color = Color.White, shape = RoundedCornerShape(32.dp))
                            .padding(16.dp).align(Alignment.Center).clickable {
                            coroutineScope.launch {
                                useFrontCamera = !useFrontCamera
                                currentCameraSelector = if (useFrontCamera) frontCameraSelector else backCameraSelector
                            }
                        }
                    )
                }

                Box(modifier = modifier.padding(16.dp).align(Alignment.BottomCenter)) {
                    Icon(painter = painterResource(id = R.drawable.ic_take_photo),
                        "Take",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(100.dp).background(color = Color.White, shape = RoundedCornerShape(32.dp),
                        ).padding(16.dp).align(Alignment.Center).clickable {
                            coroutineScope.launch {
                                imageCaptureUseCase.takePicture(context.executor).let {
                                    onImageFile(it)
                                }
                            }
                        }
                    )
                }

            }
            LaunchedEffect(previewUseCase) {
                val cameraProvider = context.getCameraProvider()
                try {
                    // Must unbind the use-cases before rebinding them.
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, currentCameraSelector, previewUseCase, imageCaptureUseCase
                    )
                } catch (ex: Exception) {
                    Log.e("CameraCapture", "Failed to bind camera use cases", ex)
                }
            }

            LaunchedEffect(currentCameraSelector) {
                val cameraProvider = context.getCameraProvider()
                try {
                    // Must unbind the use-cases before rebinding them.
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, currentCameraSelector, previewUseCase, imageCaptureUseCase
                    )
                } catch (ex: Exception) {
                    Log.e("CameraCapture", "Failed to bind camera use cases", ex)
                }
            }
        }
    }
}
