package com.example.calculator

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var actions: TextView
    lateinit var result:TextView
    lateinit var comment:TextView
    lateinit var button: Button
    var inputLine = ""
    var operation = ""
    var lastChar = ""
    var lastOperation = "="
    var operand1: Float? = null
    var operand2: Float? = null
    val notNullOperand: Float = 1F
    var flag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        comment = findViewById<View>(R.id.comment) as TextView
        actions = findViewById<View>(R.id.actions) as TextView
        result = findViewById<View>(R.id.result) as TextView
    }

    fun onNumberClick(view: View?) {
        comment.text = ""
        button = (view as Button?)!!
        if (lastChar == "." && button.text == ".") return
        if (lastChar == "+/-") return
        //Обнуляю результат при вводе цифры после =
        if (lastOperation == "=") {
            operand1 = null
            actions.text = ""
            lastOperation = ""
        }
        inputLine += button.text //Получаю строку для числа
        actions.append(button.text) //Добавляю значение на экран
        lastChar = button.text.toString()
        flag = true
    }

    fun onOperationClick(view: View?) {
        comment.text = ""
        button = (view as Button?)!!
        operation = button.text.toString()
        if (operation == "C") {
            //Очищаю все
            inputLine = ""
            operation = ""
            lastOperation = "="
            operand1 = null
            operand2 = null
            actions.text = ""
            result.text = ""
            flag = true
        } else if (operation == "~") {
            // Стирание последнего введенного знака
            if (inputLine.isNotEmpty()) {
                inputLine = inputLine.substring(0, inputLine.length - 1)
                removChar(1)
            } else if (lastOperation == "=" || lastOperation == "") return else {
                lastOperation = "="
                removChar(1)
            }
            flag = true
        } else {
            if (!flag) return
            //Если есть строка для преобразования в число
            if (inputLine.isNotEmpty()) {
                actions.append(button.text) //Добавляю значение на экран
                if (operand1 == null) {
                    //Получаю 1-е число и вывожу его в результат
                    operand1 = inputLine.toFloat()
                    if (operation == "+/-") {
                        //Меняю знак
                        notNullOperand.let { operand1!! * -1}
                        operand1 = notNullOperand
                        lastChar = operation
                    }
                    result.text = operand1.toString()
                } else {
                    try {
                        //Получаю 2-е число
                        operand2 = inputLine.toFloat()
                        if (operation == "+/-") {
                            //Меняю знак
                            notNullOperand.let { operand2!! * -1}
                            operand2 = notNullOperand
                            lastChar = operation
                        } else { //Вызываю выполнение операции
                            commitOperation(operand2!!, lastOperation)
                        }
                    } catch (ex: NumberFormatException) {
                        actions.text = ""
                    }
                }
                inputLine = "" //Очищаю строку для ввода числа
            } else if (operand2 != null) {
                actions.append(button.text) //Добавляю значение на экран
                commitOperation(operand2!!, lastOperation) //Вызываю выполнение операции
            } else if (operand1 != null) {
                actions.append(button.text) //Добавляю значение на экран
            }

            //Запоминаю последнюю операцию
            //не работает if
            if (operation != "+/-") {
                lastOperation = operation
                flag = false
            }
        }
        //При нажатии кнопки = обнуляю экран и оставляю результат
        if (operation == "=") {
            actions.text = operand1.toString()
            result.text = ""
            lastOperation = operation
            flag = true
        }
    }

    private fun commitOperation(number: Float, lastOperation: String) {
        when (lastOperation) {
            "+" -> operand1 = operand1?.plus(number)
            "-" -> operand1 = operand1?.minus(number)
            "*" -> operand1 = operand1?.times(number)
            "/" -> if (number == 0F) {
                comment.text = "На 0 делить нельзя!"
                removChar(2)
                flag = false
            } else {
                operand1 = operand1?.div(number)
            }
        }
        operand2 = null //Обнуляю второе число
        result.text = operand1.toString() //Вывожу результат на экран
    }

    private fun removChar(amt: Int) {
        val actionsLine = actions.text.toString()
        if (actionsLine.length >= amt) actions.text =
            actionsLine.substring(0, actionsLine.length - amt)
    }

}

//TO DO
// Смена знака
//Деление на 0 - сохранить в последней операции деление на 0