package com.udacity.loadingbtn


sealed class ButtonState {
    object ReadyToDownload : ButtonState()
    object Loading : ButtonState()
    object Completed : ButtonState()
}