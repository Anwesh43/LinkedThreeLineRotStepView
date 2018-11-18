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

class ThreeLineRotStepView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            val k : Float = scale.updateScale(dir, lines * lines, 1)
            scale += k
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start(cb : () -> Unit) {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class TLRSNode(var i : Int, val state : State = State()) {

        private var prev : TLRSNode? = null

        private var next : TLRSNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < nodes - 1) {
                next = TLRSNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawTLRNode(i, state.scale, paint)
            next?.draw(canvas, paint)
        }

        fun update(cb : (Int, Float) -> Unit) {
            state.update {
                cb(i, it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : TLRSNode {
            var curr : TLRSNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }
}