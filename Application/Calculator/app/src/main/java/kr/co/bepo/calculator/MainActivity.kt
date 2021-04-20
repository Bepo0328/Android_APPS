package kr.co.bepo.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.room.Room
import kr.co.bepo.calculator.model.History
import kotlin.math.exp

class MainActivity : AppCompatActivity() {

    private val tvExpression: TextView by lazy {
        findViewById<TextView>(R.id.activity_main_tv_expression)
    }

    private val tvResult: TextView by lazy {
        findViewById<TextView>(R.id.activity_main_tv_result)
    }

    private val constraintLayoutHistory: View by lazy {
        findViewById<View>(R.id.activity_main_constraintLayout_history)
    }

    private val linearLayoutHistory: LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.activity_main_linearLayout_history)
    }

    private var isOperator: Boolean = false
    private var hasOperator: Boolean = false
    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "historyDB"
        ).build()
    }

    fun buttonClicked(view: View) {
        when (view.id) {
            R.id.activity_main_btn_zero -> numberButtonClicked("0")
            R.id.activity_main_btn_one -> numberButtonClicked("1")
            R.id.activity_main_btn_two -> numberButtonClicked("2")
            R.id.activity_main_btn_three -> numberButtonClicked("3")
            R.id.activity_main_btn_four -> numberButtonClicked("4")
            R.id.activity_main_btn_five -> numberButtonClicked("5")
            R.id.activity_main_btn_six -> numberButtonClicked("6")
            R.id.activity_main_btn_seven -> numberButtonClicked("7")
            R.id.activity_main_btn_eight -> numberButtonClicked("8")
            R.id.activity_main_btn_nine -> numberButtonClicked("9")
            R.id.activity_main_btn_add -> operatorButtonClicked("+")
            R.id.activity_main_btn_subtract -> operatorButtonClicked("-")
            R.id.activity_main_btn_multiply -> operatorButtonClicked("*")
            R.id.activity_main_btn_percent -> operatorButtonClicked("%")
            R.id.activity_main_btn_divide -> operatorButtonClicked("/")

        }

    }

    private fun numberButtonClicked(number: String) {
        if (isOperator) {
            tvExpression.append(" ")
            isOperator = false
        }

        val expressionText = tvExpression.text.split(" ")

        if (expressionText.isNotEmpty() && expressionText.last().length >= 15) {
            Toast.makeText(this, "15자리 까지만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return
        } else if (expressionText.last().isEmpty() && number == "0") {
            Toast.makeText(this, "0은 제일 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        tvExpression.append(number)
        tvResult.text = calculateExpression()
    }

    private fun operatorButtonClicked(operator: String) {
        if (tvExpression.text.isEmpty()) {
            return
        }

        when {
            isOperator -> {
                val text = tvExpression.text.toString()
                tvExpression.text = text.dropLast(1) + operator
            }

            hasOperator -> {
                Toast.makeText(this, "연산자는 한 번만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return
            }

            else -> {
                tvExpression.append(" $operator")
            }

        }

        val ssb = SpannableStringBuilder(tvExpression.text)
        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.sushi)),
            tvExpression.text.length -1,
            tvExpression.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvExpression.text = ssb
        isOperator = true
        hasOperator = true

    }

    fun clearButtonClicked(view: View) {
        tvExpression.text = ""
        tvExpression.text = ""
        isOperator = false
        hasOperator = false
    }

    fun resultButtonClicked(view: View) {
        val expressionTexts = tvExpression.text.split(" ")

        if (tvExpression.text.isEmpty() || expressionTexts.size == 1) {
            return
        }

        if (expressionTexts.size !=3 && hasOperator) {
            Toast.makeText(this, "아직 완성되지 않은 수식입니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val expressionText = tvExpression.text.toString()
        val resultText = calculateExpression()

        Thread(Runnable {
            db.historyDao().insertHistory(History(null, expressionText, resultText))
        }).start()

        tvResult.text = ""
        tvExpression.text = resultText

        isOperator = false
        hasOperator = false

    }

    private fun calculateExpression(): String {
        val expressionTexts = tvExpression.text.split(" ")

        if (hasOperator.not() || expressionTexts.size != 3) {
            return ""
        } else if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            return ""
        }

        val exp1 = expressionTexts[0].toBigInteger()
        val exp2 = expressionTexts[2].toBigInteger()

        return when (expressionTexts[1]) {
            "+" -> (exp1 + exp2).toString()
            "-" -> (exp1 - exp2).toString()
            "*" -> (exp1 * exp2).toString()
            "/" -> (exp1 / exp2).toString()
            "%" -> (exp1 % exp2).toString()
            else -> ""
        }
    }

    fun historyButtonClicked(view: View) {
        constraintLayoutHistory.isVisible = true
        linearLayoutHistory.removeAllViews()

        Thread(Runnable {
            db.historyDao().getAll().reversed().forEach {
                runOnUiThread {
                    val historyView = LayoutInflater.from(this).inflate(R.layout.history_row, null, false)
                    historyView.findViewById<TextView>(R.id.history_row_tv_expression).text = it.expression
                    historyView.findViewById<TextView>(R.id.history_row_tv_result).text = "= ${it.result}"
                    //historyView.findViewById<TextView>(R.id.history_row_tv_result).text = getString(R.string.activity_main_result2, it.result)

                    linearLayoutHistory.addView(historyView)
                }
            }
        }).start()
    }

    fun historyCloseButtonClicked(view: View) {
        constraintLayoutHistory.isVisible = false
    }

    fun historyClearButtonClicked(view: View) {
        linearLayoutHistory.removeAllViews()
        Thread(Runnable {
            db.historyDao().deleteAll()
        }).start()
    }
}

private fun String.isNumber(): Boolean {
    return try {
        this.toBigInteger()
        true
    } catch (e: NumberFormatException){
        false
    }
}
