package com.example.lab9_krivikov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentTransaction

interface OnDataListener {
    fun onData(Data: GitProjectData)
}

class MainActivity : AppCompatActivity(), OnDataListener  {

    private var isTwoPane = false;
    private var currentDataAboutProject: GitProjectData = GitProjectData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            var localData = savedInstanceState.getSerializable("data") as GitProjectData
            currentDataAboutProject = localData;
        }

        isTwoPane = findViewById<View>(R.id.frame_left) != null
        if (isTwoPane) {
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_left, FragmentListGithubProject())
                .add(R.id.frame_right, FragmentGithubInfo(currentDataAboutProject))
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, FragmentListGithubProject())
                .commit()
        }

    }

    override fun onData(data: GitProjectData) {
        if(data.projectURL != null){
            this.currentDataAboutProject = data
        }
        isTwoPane = findViewById<View>(R.id.frame_left) != null
        supportFragmentManager.beginTransaction()
            .replace(
                if (isTwoPane)
                    R.id.frame_right
                else
                    R.id.container,
                FragmentGithubInfo(currentDataAboutProject)
            )
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("data", currentDataAboutProject);
    }
}
