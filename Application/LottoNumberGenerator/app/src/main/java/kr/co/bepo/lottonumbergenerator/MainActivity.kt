package kr.co.bepo.lottonumbergenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private val numberPicker: NumberPicker by lazy {
        findViewById(R.id.activity_main_np)
    }

    private val btnNumberAdd: Button by lazy {
        findViewById(R.id.activity_main_btn_number_add)
    }

    private val btnNubmerClear: Button by lazy {
        findViewById(R.id.activity_main_btn_number_clear)
    }

    private val btnRun: Button by lazy {
        findViewById(R.id.activity_main_btn_run)
    }

    private val numberTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.activity_main_tv_number1),
            findViewById(R.id.activity_main_tv_number2),
            findViewById(R.id.activity_main_tv_number3),
            findViewById(R.id.activity_main_tv_number4),
            findViewById(R.id.activity_main_tv_number5),
            findViewById(R.id.activity_main_tv_number6)
        )
    }
    private var didRun: Boolean = false

    private val pickNumberSet: HashSet<Int> = hashSetOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        initRunButton()
        initAddButton()
        initClearButton()
    }

    private fun initRunButton() {
        btnRun.setOnClickListener {
            val list = getRandomNumber()

            didRun = true

            list.forEachIndexed { index, number ->
                val textView: TextView = numberTextViewList[index]

                getNumberBackground(number, textView)
            }

            Log.d("MainActivity", list.toString())
        }
    }

    private fun initAddButton() {
        btnNumberAdd.setOnClickListener {
            if (didRun) {
                Toast.makeText(this, "초기화 후에 시도해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickNumberSet.size >= 5) {
                Toast.makeText(this, "번호는 5개 까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickNumberSet.contains(numberPicker.value)) {
                Toast.makeText(this, "이미 선택한 번호 입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textView: TextView = numberTextViewList[pickNumberSet.size]

            getNumberBackground(numberPicker.value, textView)

            pickNumberSet.add(numberPicker.value)
        }
    }

    private fun initClearButton() {
        btnNubmerClear.setOnClickListener {
            pickNumberSet.clear()
            numberTextViewList.forEach {
                it.isVisible = false
            }

            didRun = false

        }
    }

    private fun getRandomNumber(): List<Int> {
        val numberList = mutableListOf<Int>().apply {
            for (i in 1..45) {
                if (pickNumberSet.contains(i)) {
                    continue
                }
                this.add(i)
            }
        }
        numberList.shuffle()

        val newList = pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size)

        return newList.sorted()
    }

    private fun getNumberBackground(number: Int, textView: TextView) {
        textView.isVisible = true
        textView.text = number.toString()

        when (number) {
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 31..40 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
        }
    }


}