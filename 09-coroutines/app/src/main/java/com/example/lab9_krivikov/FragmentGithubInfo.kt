package com.example.lab9_krivikov

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.net.URL

class FragmentGithubInfo(
    private val project: GitProjectData = GitProjectData()
) : Fragment()  {
    private lateinit var mainContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_project_info, container, false)

        if (project.avatarURL != "") {
            GlobalScope.launch {
                val buf = URL(project.avatarURL).readBytes()
                val bitmap = BitmapFactory.decodeByteArray(buf, 0, buf.size);
                MainScope().launch {
                    view.findViewById<ImageView>(R.id.profileImage).setImageBitmap(bitmap)
                }
            }
        }

        view.findViewById<TextView>(R.id.projectName).text = project.name
        if(project.description == null){
            view.findViewById<TextView>(R.id.projectDescription).text =
                "Нет описания..."
        }else{
            view.findViewById<TextView>(R.id.projectDescription).text =
                project.description
        }
        view.findViewById<TextView>(R.id.projectURL).text = project.projectURL
        view.findViewById<TextView>(R.id.watchersCount).text =
            "${project.watchersCount}"
        view.findViewById<TextView>(R.id.size).text = "${project.size}"
        view.findViewById<TextView>(R.id.createdAt).text =
            project.createdAt
        view.findViewById<TextView>(R.id.updatedAt).text =
            project.updatedAt
        view.findViewById<TextView>(R.id.license).text = project.license

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainContext = context
    }
}