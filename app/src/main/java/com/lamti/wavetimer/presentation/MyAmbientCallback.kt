package com.lamti.wavetimer.presentation

import android.os.Bundle
import androidx.wear.ambient.AmbientModeSupport

internal class MyAmbientCallback : AmbientModeSupport.AmbientCallback() {

    override fun onEnterAmbient(ambientDetails: Bundle?) {
        println("OnEnterAmbient: $ambientDetails")
    }

    override fun onExitAmbient() {
        println("OnExitAmbient")
    }

    override fun onUpdateAmbient() {
        println("onUpdateAmbient")
    }
}