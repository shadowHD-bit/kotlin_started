package com.example.lab8_krivikov

import java.io.Serializable

class Task(currentOperation: String): Serializable {
    var firstNumberInTask: Double;
    var secondNumberInTask: Double;

    var currentOperationType: String;
    var rightAnswer: Double;
    var arrayAnswers: Array<Double>;

    init {
        this.firstNumberInTask = (1..10).random().toDouble();
        this.secondNumberInTask = (1..10).random().toDouble();
        this.currentOperationType = currentOperation;

        if (currentOperation == "+") {
            this.rightAnswer = this.firstNumberInTask + this.secondNumberInTask
        } else if (currentOperation == "-") {
            this.rightAnswer = this.firstNumberInTask - this.secondNumberInTask
        } else if (currentOperation == "*") {
            this.rightAnswer = this.firstNumberInTask * this.secondNumberInTask

        } else {
            this.rightAnswer = this.firstNumberInTask / this.secondNumberInTask
        }

        this.arrayAnswers = arrayOf(
            this.rightAnswer,
            this.rightAnswer + (1..2).random().toDouble(),
            this.rightAnswer - (2..4).random().toDouble(),
            this.rightAnswer + (3..4).random().toDouble(),
        )
        this.arrayAnswers.shuffle()
    }
}
