package com.example.lab8_krivikov

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class LeftFragment() : Fragment() {
    private lateinit var mainContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_left, container, false)
        val listOptions = view.findViewById<ListView>(R.id.list_fruits)


        val arrayMathOperationType = arrayOf("+", "-", "*", "/");
        listOptions.adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            arrayMathOperationType
        )

        listOptions.setOnItemClickListener { parent, view, position, id ->
            (mainContext as OnDataListener).onData(
                DataTask(
                    arrayMathOperationType[position],
                    null,
                    null
                )
            )
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainContext = context
    }
}
