package com.example.lab11_krivikov

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.random.Random

private var radius: Float = 50f
private var cx = radius
private var cy = radius
private var dx = 10
private var dy = 10

private var radius2: Float = 50f
private var cx2 = 600f
private var cy2 = 600f
private var dx2 = -10
private var dy2 = -10


class DrawSurface : SurfaceView, SurfaceHolder.Callback {
    private var paint = Paint()
    private var paint2 = Paint()
    private lateinit var job: Job

    val rnd = Random.Default
    var colorBg = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    var colorCircle = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    var colorCircle2 = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    override fun surfaceChanged(holder: SurfaceHolder, format: Int,
                                width: Int, height: Int) {
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        paint.color = colorCircle
        paint.color = colorCircle2

        job = GlobalScope.launch {
            var canvas: Canvas?
            while (true) {
                canvas = holder.lockCanvas(null)
                if (canvas != null) {
                    canvas.drawColor(colorBg)
                    canvas.drawCircle(cx, cy, radius, paint)
                    canvas.drawCircle(cx2, cy2, radius2, paint2)

                    holder.unlockCanvasAndPost(canvas)
                }
                // Перемещение
                cx += dx
                cy += dy

                cx2 += dx2
                cy2 += dy2

                if (cx > width - radius || cx < radius){
                    dx = -dx
                }
                if (cy > height - radius || cy < radius){
                    dy = -dy
                }
                if (cx2 > width - radius2 || cx2 < radius2){
                    dx2 = -dx2
                }
                if (cy2 > height - radius2 || cy2 < radius2){
                    dy2 = -dy2
                }

                if (Math.sqrt(((cx - cx2) * (cx - cx2) + (cy - cy2) * (cy - cy2)).toDouble()) < radius + radius2){
                    dx = -dx
                    dx2 = -dx2
                    dy = -dy
                    dy2 = -dy2
                }
            }
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        job.cancel()
    }

    fun onTap(){
        colorBg = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        colorCircle = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        colorCircle2 = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

        dx = (0..100).random()
        dy = (0..100).random()
        radius = (30..100).random().toFloat()

        dx2 = (0..100).random()
        dy2 = (0..100).random()
        radius2 = (30..100).random().toFloat()

        paint.color = colorCircle
        paint2.color = colorCircle2

        surfaceDestroyed(holder)
        surfaceDestroyed(holder)
    }

    init {
        holder.addCallback(this)
    }
}