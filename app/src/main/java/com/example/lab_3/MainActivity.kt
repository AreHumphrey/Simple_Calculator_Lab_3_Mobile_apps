package com.example.lab_3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var resultTextView: TextView
    private var lastInputIsNumber: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.resultTextView)
        val buttons = arrayOf<Button>(
            findViewById(R.id.buttonComma),
            findViewById(R.id.button0),
            findViewById(R.id.button1),
            findViewById(R.id.button2),
            findViewById(R.id.button3),
            findViewById(R.id.button4),
            findViewById(R.id.button5),
            findViewById(R.id.button6),
            findViewById(R.id.button7),
            findViewById(R.id.button8),
            findViewById(R.id.button9),
            findViewById(R.id.buttonPlus),
            findViewById(R.id.buttonMinus),
            findViewById(R.id.buttonMultiply),
            findViewById(R.id.buttonDivide),
            findViewById(R.id.buttonPercentage)
        )

        for (button in buttons) {
            button.setOnClickListener { appendSymbol(button.text.toString()) }
        }

        val equalsButton = findViewById<Button>(R.id.buttonEquals)
        equalsButton.setOnClickListener {
            calculateExpression()
        }

        val clearButton = findViewById<Button>(R.id.buttonClear)
        clearButton.setOnClickListener {
            clearInput()
        }

        val negativeButton = findViewById<Button>(R.id.buttunNegative)
        negativeButton.setOnClickListener {
            prependMinusSymbol()
        }

        val percentageButton = findViewById<Button>(R.id.buttonPercentage)
        percentageButton.setOnClickListener {
            applyPercentage()
        }
    }

    private fun appendSymbol(symbol: String) {
        if (symbol != "+" && symbol != "*" && symbol != "/") {
            lastInputIsNumber = true
        }

        if (symbol == "," && resultTextView.text.isEmpty()) {
            resultTextView.append("0")
        }

        if (!lastInputIsNumber && (symbol == "+" || symbol == "*" || symbol == "/")) {
            return
        }

        if (symbol == "." && resultTextView.text.contains(".")) {
            return
        }

        val currentText = resultTextView.text.toString()
        if (symbol == "-" && currentText.endsWith("-")) {
            return
        }

        if (symbol == "-" && currentText.count { it == '-' } >= 2) {
            return
        }

        resultTextView.append(symbol)
        lastInputIsNumber = symbol != "+" && symbol != "*" && symbol != "/"
    }

    private fun clearInput() {
        resultTextView.text = ""
        lastInputIsNumber = false
    }

    private fun prependMinusSymbol() {
        if (resultTextView.text.isEmpty()) {
            resultTextView.append("-")
        } else {
            val currentText = resultTextView.text.toString()
            if (!currentText.startsWith("-")) {
                resultTextView.text = "-$currentText"
            } else {
                resultTextView.text = currentText.substring(1)
            }
        }
    }

    private fun applyPercentage() {
        val expressionText = resultTextView.text.toString()
        val expression = ExpressionBuilder(expressionText).build()
        try {
            val result = expression.evaluate() / 100
            resultTextView.text = result.toString()
            lastInputIsNumber = true
        } catch (e: ArithmeticException) {
            resultTextView.text = "Error"
            lastInputIsNumber = false
        }
    }

    private fun calculateExpression() {
        val expression = ExpressionBuilder(resultTextView.text.toString()).build()
        try {
            val result = expression.evaluate()
            if (result % 1 == 0.0) {
                resultTextView.text = result.toInt().toString()
            } else {
                resultTextView.text = result.toString()
            }
            lastInputIsNumber = true
        } catch (e: ArithmeticException) {
            resultTextView.text = "Error"
            lastInputIsNumber = false
        }
    }
}
