package com.example.lab8_krivikov

import java.io.Serializable

class DataTask(): Serializable {
    var typeOperation: String = "";
    var countRightAnswer: Int? = 0;
    var countUnrightAnswer: Int? = 0;

    constructor (typeOperation: String, countRightAnswer: Int?, countUnrightAnswer: Int?) : this() {
        this.typeOperation = typeOperation
        this.countRightAnswer = countRightAnswer
        this.countUnrightAnswer = countUnrightAnswer
    }
}
