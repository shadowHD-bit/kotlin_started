package com.example.lab8_krivikov

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment

class RightFragment(data: DataTask = DataTask("+", 0, 0)) : Fragment() {
    private val currentOperation = data.typeOperation;
    private var mathTask = Task(data.typeOperation);
    private var correctAnswers: Int? = data.countRightAnswer;
    private var incorrectAnswers: Int? = data.countUnrightAnswer;
    private lateinit var mainContext: Context;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragmen_task, container, false)
        var currentTask: TextView = view.findViewById(R.id.textAnswer);
        var correctAnswersTextView = view.findViewById<TextView>(R.id.countRightAnswers)

        currentTask.text =
            "${mathTask.firstNumberInTask}${mathTask.currentOperationType}${mathTask.secondNumberInTask}=?"
        var adapter = ArrayAdapter<Double>(
            requireContext(),
            android.R.layout.simple_list_item_1, mathTask.arrayAnswers
        )

        correctAnswersTextView.text =
            "Количество правильных ответов: ${correctAnswers}"
        var incorrectAnswersTextView = view.findViewById<TextView>(R.id.countUnrightAnswers)

        incorrectAnswersTextView.text =
            "Количество неправильных ответов: ${incorrectAnswers}"
        val listView: ListView = view.findViewById(R.id.listAnswers)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            if (mathTask.rightAnswer == mathTask.arrayAnswers[position]) {
                this.correctAnswers = this.correctAnswers?.plus(1);

            } else {
                this.incorrectAnswers = this.incorrectAnswers?.plus(1);
            }

            correctAnswersTextView.text =
                "Количество правильных ответов: ${correctAnswers}"

            incorrectAnswersTextView.text =
                "Количество неправильных ответов: ${incorrectAnswers}"

            mathTask = Task(currentOperation)

            currentTask.text =
                "${mathTask.firstNumberInTask}${mathTask.currentOperationType}${mathTask.secondNumberInTask}"

            adapter = ArrayAdapter<Double>(
                requireContext(),
                android.R.layout.simple_list_item_1, mathTask.arrayAnswers
            )

            listView.adapter = adapter

            (mainContext as OnDataListener).onData(
                DataTask(
                    currentOperation,
                    correctAnswers,
                    incorrectAnswers
                )
            )
        }
        return view;
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainContext = context
    }

}
