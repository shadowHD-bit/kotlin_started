package com.example.lab8_krivikov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentTransaction

interface OnDataListener {
    fun onData(Data: DataTask)
}

class MainActivity : AppCompatActivity(), OnDataListener {
    private var isTwoPane = false;
    private var currentDataAboutTask: DataTask = DataTask("+", 0, 0);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            var localData = savedInstanceState.getSerializable("data") as DataTask
            currentDataAboutTask = localData;
        }

        isTwoPane = findViewById<View>(R.id.frame_right) != null && findViewById<View>(R.id.frame_left) != null
        if (isTwoPane) {
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_left, LeftFragment())
                .add(R.id.frame_right, RightFragment(currentDataAboutTask))
                .commit()
        } else {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, LeftFragment())
                    .commit()
        }
    }

    override fun onData(Data: DataTask) {
        if (Data.countRightAnswer != null && Data.countUnrightAnswer != null) {
            this.currentDataAboutTask.countRightAnswer = Data.countRightAnswer
            this.currentDataAboutTask.countUnrightAnswer = Data.countUnrightAnswer
        } else {
            this.currentDataAboutTask.typeOperation = Data.typeOperation;
            isTwoPane = findViewById<View>(R.id.frame_right) != null && findViewById<View>(R.id.frame_left) != null
            supportFragmentManager.beginTransaction()
                .replace(
                    if (isTwoPane)
                        R.id.frame_right
                    else
                        R.id.container,
                    RightFragment(this.currentDataAboutTask)
                )
                .hide(RightFragment(this.currentDataAboutTask))
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("data", currentDataAboutTask);
    }
}



