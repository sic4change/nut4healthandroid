package org.sic4change.nut4health.ui.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun rememberMainState(
    id: MutableState<String> = rememberSaveable { mutableStateOf("") },
    username: MutableState<String> = rememberSaveable { mutableStateOf("") },
    email: MutableState<String> = rememberSaveable { mutableStateOf("") },
    role: MutableState<String> = rememberSaveable { mutableStateOf("") },
    point: MutableState<String> = rememberSaveable { mutableStateOf("") },
    phoneCode: MutableState<String> = rememberSaveable { mutableStateOf("") },
    phoneLength: MutableState<Int> = rememberSaveable { mutableStateOf(0) },
    pointId: MutableState<String> = rememberSaveable { mutableStateOf("") },
    logout: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    changePass: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    roleError: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    phoneToCheck: MutableState<String> = rememberSaveable { mutableStateOf("") },
    openDialogSearchByPhone: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    editPhoneToCheck: MutableState<String> = rememberSaveable { mutableStateOf("") },
    filterText: MutableState<String> = rememberSaveable { mutableStateOf("") },
    showPhotoSelector: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    avatar: MutableState<String?> = rememberSaveable { mutableStateOf("https://firebasestorage.googleapis.com/v0/b/chuqabp.appspot.com/o/ic_empty_avatar.png?alt=media&token=41418f7f-a4e3-4510-a14c-c8fbc5f0f12d") },
) = remember{ MainState(
    id, username, email, role, point, phoneCode, phoneLength, pointId, logout, changePass, roleError, phoneToCheck, openDialogSearchByPhone,
    editPhoneToCheck, filterText, showPhotoSelector, avatar) }

class MainState(
    val id: MutableState<String>,
    val username: MutableState<String>,
    val email: MutableState<String>,
    val role: MutableState<String>,
    val point: MutableState<String>,
    val phoneCode: MutableState<String>,
    val phoneLength: MutableState<Int>,
    val pointId: MutableState<String>,
    val logout: MutableState<Boolean>,
    val changePass: MutableState<Boolean>,
    val roleError: MutableState<Boolean>,
    val phoneToCheck: MutableState<String>,
    val openDialogSearchByPhone: MutableState<Boolean>,
    val editPhoneToCheck: MutableState<String>,
    val filterText: MutableState<String>,
    val showPhotoSelector: MutableState<Boolean>,
    val avatar: MutableState<String?>
) {
    fun showPhotoSelector() {
        showPhotoSelector.value = !showPhotoSelector.value
    }

    fun showLogoutQuestion() {
        logout.value = !logout.value
    }

    fun showChangePassQuestion() {
        changePass.value = !changePass.value
    }

    fun showRoleError() {
        roleError.value = !roleError.value
    }

}
