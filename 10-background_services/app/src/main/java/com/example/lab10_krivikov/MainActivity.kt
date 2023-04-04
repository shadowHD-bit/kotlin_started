package com.example.lab10_krivikov

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.EditText
import android.widget.TextView

const val BROADCAST_TIME_EVENT = "com.example.lab09.timeevent"
var receiver: BroadcastReceiver? = null

class MainActivity : AppCompatActivity() {

    //Инициализация начальных переменных
    private var firstValue = 1
    private var interval = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        receiver = object : BroadcastReceiver() {

            // Получено широковещательное сообщение
            override fun onReceive(context: Context?, intent: Intent) {
                val counter = intent.getIntExtra("counter", 0)
                val textCounter = findViewById<TextView>(R.id.textCounter)
                textCounter.text = counter.toString()
            }
        }
        // Фильтр для ресивера
        val filter = IntentFilter(BROADCAST_TIME_EVENT)
        // Регистрация ресивера и фильтра
        registerReceiver(receiver, filter)

        findViewById<EditText>(R.id.firstValueInput).setText("1")
        findViewById<EditText>(R.id.intervalInput).setText("1")
    }

    fun saveDataIntervalInputs(view: View) {
        this.interval = findViewById<EditText>(R.id.intervalInput).text.toString().toInt();
        if (isBound)
            myService!!.setInterval(interval)
    }

    fun removeFirstValue(view: View) {
        var inp = findViewById<EditText>(R.id.firstValueInput);
        inp.setText("0")
        if (isBound)
            myService!!.resetFirstValue()
    }

    override fun onDestroy() {
        // Удаление регистрации ресивера и фильтра
        unregisterReceiver(receiver);
        super.onDestroy()
    }

    var myService: TimeService? = null
    var isBound = false
    val myConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TimeService.MyBinder
            myService = binder.getService()
            isBound = true
        }
        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
        }
    }

    fun buttonStartService(view: View) {
        var intent = Intent(this, TimeService::class.java)
        var intervalInputValue: Int = findViewById<EditText>(R.id.intervalInput).text.toString().toInt();
        var firstValueInputValue: Int = findViewById<EditText>(R.id.firstValueInput).text.toString().toInt();
        intent.putExtra("interval", intervalInputValue)
        intent.putExtra("firstValue", firstValueInputValue)
        startService(intent)

        bindService(Intent(this, TimeService::class.java),
            myConnection, Context.BIND_AUTO_CREATE)

    }
    fun buttonStopService(view: View) {
        stopService(Intent(this, TimeService::class.java))
        unbindService(myConnection)
    }

    fun buttonGetValue(view: View) {
        if (isBound)
            findViewById<TextView>(R.id.textCounter).text =
                myService!!.getCounter().toString()
    }
}