package com.anwesh.uiprojects.linkedthreelinerotstepview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.threelinerotview.ThreeLineRotStepView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThreeLineRotStepView.create(this)
    }
}
