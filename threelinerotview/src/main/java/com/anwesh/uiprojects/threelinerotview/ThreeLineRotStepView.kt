package com.anwesh.uiprojects.threelinerotview

/**
 * Created by anweshmishra on 18/11/18.
 */

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.app.Activity
import android.content.Context

val nodes : Int = 5
val lines : Int = 2
val SIZE_FACTOR : Int = 3
val STROKE_FACTOR : Int = 60
val scDiv : Double = 0.5
val scGap : Float = 0.05f
val COLOR : Int = Color.parseColor("#0277BD")
val BACK_COLOR : Int = Color.parseColor("#BDBDBD")

fun Int.getInverse() : Float = 1f / this

fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.getInverse(), Math.max(0f, this - i * n.getInverse())) * n

fun Float.getScaleFactor() : Float =  Math.floor(this / scDiv).toFloat()

fun Float.getMirrorValue(a : Int, b : Int) : Float = (1 - this) * a.getInverse() + b.getInverse() * this

fun Float.updateScale(dir : Float, a : Int, b : Int) : Float = dir * scGap * getScaleFactor().getMirrorValue(a, b)


