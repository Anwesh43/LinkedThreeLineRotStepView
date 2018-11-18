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

fun Canvas.drawTLRNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = w / (nodes)
    val size : Float = gap / SIZE_FACTOR
    val sc1 : Float = scale.divideScale(0, 2)
    val sc2 : Float = scale.divideScale(1, 2)
    val sc11 : Float = sc1.divideScale(0, 2)
    val sc12 : Float = sc2.divideScale(1, 2)
    paint.color = COLOR
    paint.strokeWidth = Math.min(w, h) / STROKE_FACTOR
    paint.strokeCap = Paint.Cap.ROUND
    save()
    translate(gap * (i + 1), h/2)
    rotate(90f * sc2)
    drawLine(-size, -size, size, -size, paint)
    var y : Float = -size
    for (j in 0..(lines - 1)) {
        val sca : Float = sc11.divideScale(j, lines)
        val scl : Float = sc12.divideScale(j, lines)
        y += size * sca
        drawLine(-size, y, size, y, paint)
        for (k in 0..1) {
            val x : Float = -size + k * 2 * size
            val y : Float = -size + j * size
            drawLine(x, y, x, y + size * scl, paint)
        }
    }
    restore()
}
