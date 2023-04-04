package com.example.lab9_krivikov

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL

class FragmentListGithubProject : Fragment(), java.io.Serializable  {
    private lateinit var mainContext: Context
    var projects = arrayListOf<String>()
    private val projectsListFullData: MutableList<GitProjectData> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (savedInstanceState != null) {
            var localData = savedInstanceState.getSerializable("data_projects") as ArrayList<String>
            projects = localData;
        }

        val view = inflater.inflate(R.layout.fragment_project_list, container, false)

        val searchTextView = view.findViewById<EditText>(R.id.inputNameProject)
        val searchButton = view.findViewById<Button>(R.id.submitNameProject)
        val listOptions = view.findViewById<ListView>(R.id.git_listview)

        searchButton.setOnClickListener {
            projects.clear()
            projectsListFullData.clear()

            val searchQuery = searchTextView.text.toString()
            GlobalScope.launch {
                val t =
                    URL("https://api.github.com/search/repositories?q=$searchQuery&per_page=5").readText()
                val json = JSONObject(t)
                json.getJSONArray("items").let { array ->
                    for (i in 0 until array.length()) {
                        array.getJSONObject(i).let { item ->
                            projects.add(
                                    item.getString("full_name"),
                            )
                            projectsListFullData.add(GitProjectData(item))
                        }
                    }
                }
                MainScope().launch {
                    listOptions.adapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        projects
                    )
                    listOptions.setOnItemClickListener { parent, view, position, id ->
                        (mainContext as OnDataListener).onData(projectsListFullData[position])
                    }
                }
            }
        }
        listOptions.adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            projects
        )
        listOptions.setOnItemClickListener { parent, view, position, id ->
            (mainContext as OnDataListener).onData(projectsListFullData[position])
        }
    return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainContext = context
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("data_projects", projects);
    }
}