package com.example.lab10_krivikov

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


//Инициализация фоновой службы
class TimeService : Service() {
    private var firstValue = 0
    private var interval = 1

    private var counter = firstValue
    private lateinit var job: Job

    private val myBinder = MyBinder()

    //Связь с клиентом
    override fun onBind(intent: Intent?): IBinder? {
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun getService() : TimeService {
            return this@TimeService
        }
    }

    //Вызывается при запуске службы
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //Получение данных из интента
        if(intent != null){
            firstValue = intent.getIntExtra("firstValue", 0)
            interval = intent.getIntExtra("interval", 1)
        }
        counter = firstValue

        //Поток в фоновом режиме
        job = GlobalScope.launch {
            while (true) {
                delay((1000 * interval).toLong())
                Log.d("SERVICE", "Timer Is Ticking: " + counter)
                counter++
                val intent = Intent(BROADCAST_TIME_EVENT);
                intent.putExtra("counter", counter);
                sendBroadcast(intent);
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    //Срабатывает при завершение работы службы
    override fun onDestroy() {
        Log.d("SERVICE", "onDestroy")
        job.cancel()
        super.onDestroy()
    }

    fun getCounter(): Int {
        return counter
    }

    fun setInterval(newInterval:Int){
        interval = newInterval
    }

    fun resetFirstValue(){
        this.counter = 0
    }
}