package com.example.lab3_krivikov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

var firstNumber = 0
var secondNumber = 0
var answer = 0f
var numberOperation = 0 //+, -, *, /
var textQuestion = ""

var variantAnswer: MutableList<String> = mutableListOf()
var numberTrueAnswer = 0
var numberFalseAnswer = 0

class MainActivity : AppCompatActivity() {

    fun generateTextQuestion(firstNumber: Int, secondNumber: Int, numberOperation: Int):String{
        var text = ""
        when (numberOperation) {
            0 -> {
                 text = "$firstNumber + $secondNumber = ?"
            }
            1 -> {
                 text = "$firstNumber - $secondNumber = ?"
            }
            2 -> {
                 text = "$firstNumber * $secondNumber = ?"
            }
            3 -> {
                 text = "$firstNumber / $secondNumber = ?"
            }
        }
        return text
    }

    fun getAnswer(firstNumber: Int, secondNumber: Int, numberOperation: Int):Float{
        var answer = 0f
        when (numberOperation) {
            0 -> {
                answer = firstNumber.toFloat() + secondNumber.toFloat()
            }
            1 -> {
                answer = firstNumber.toFloat() - secondNumber.toFloat()
            }
            2 -> {
                answer = firstNumber.toFloat() * secondNumber.toFloat()
            }
            3 -> {
                answer = firstNumber.toFloat() / secondNumber.toFloat()
            }
        }
        return answer
    }

    fun generateListAnswer(answer:Float): MutableList<String> {

        var variants: MutableSet<String> = mutableSetOf()
        var plusOrMinusOperation = 0
        variants.add(answer.toString())

        while(variants.count() != 4){
            plusOrMinusOperation = (0..1).random()
            if(plusOrMinusOperation == 0){
                variants.add((answer + (0..5).random()).toString())
            }else{
                variants.add((answer - (0..5).random()).toString())
            }
        }
        return variants.shuffled().toMutableList()
    }

    fun init(): ListView{
        var questionView:TextView = findViewById(R.id.question)
        var trueAnswer:TextView = findViewById(R.id.number_true)
        var falseAnswer:TextView = findViewById(R.id.number_false)

        firstNumber = (0..10).random()
        secondNumber = (0..10).random()
        numberOperation = (0..3).random()

        answer = getAnswer(firstNumber, secondNumber, numberOperation)
        textQuestion = generateTextQuestion(firstNumber, secondNumber, numberOperation)

        questionView.text = textQuestion
        trueAnswer.text = "Количество правильных ответов:  $numberTrueAnswer"
        falseAnswer.text = "Количество неправильных ответов:  $numberFalseAnswer"

        variantAnswer = generateListAnswer(answer)

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, variantAnswer)
        val listView: ListView = findViewById(R.id.listView)
        listView.adapter = adapter

        return listView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var listView = init()

        listView.setOnItemClickListener { parent, itemClicked, position, id ->
            if(answer.toString() == (itemClicked as TextView).text){
                numberTrueAnswer++
                val toast: Toast = Toast.makeText(this,
                    "Правильный ответ!",
                    Toast.LENGTH_SHORT)
                toast.show()

            }else{
                numberFalseAnswer++
                val toast: Toast = Toast.makeText(this,
                    "Не правильный ответ!",
                    Toast.LENGTH_SHORT)
                toast.show()
            }
            var listView = init()

        }
    }
}
