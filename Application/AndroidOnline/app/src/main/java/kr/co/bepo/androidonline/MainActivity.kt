package kr.co.bepo.androidonline

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private var money: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val topView: View = findViewById(R.id.topView)
        val timeTextView: TextView = findViewById(R.id.timeTextView)
        val moneyTextView: TextView = findViewById(R.id.moneyTextView)
        val addTenButton: Button = findViewById(R.id.addTenButton)
        val addHundredButton: Button = findViewById(R.id.addHundredButton)
        val addThousandButton: Button = findViewById(R.id.addThousandButton)
        val addTenThousandButton: Button = findViewById(R.id.addTenThousandButton)

        val format = DecimalFormat("###,###")
        var value: Int = 1

        var millisecond = 0
        var second = 0
        var minute = 0
        var hour = 0

        var moneyTen: String = "10"
        var countTen = 0
        var booleanTen: Boolean = false


        timer(period = 1) {
            millisecond++

            if (millisecond % 1000 == 0) {
                money += value
                second++
            }

            if (millisecond % 60000 == 0) {
                second = 0
                minute++
            }

            if (millisecond % 3600000 == 0) {
                second = 0
                minute = 0
                hour++
            }

            runOnUiThread {
                moneyTextView.text = format.format(money)
                timeTextView.text = String.format("%02d:%02d:%02d", hour, minute, second)

                addTenButton.isEnabled = booleanTen
                addTenButton.text = moneyTen
                addHundredButton.isEnabled = money > 100
                addThousandButton.isEnabled = money > 1000
                addTenThousandButton.isEnabled = money > 10000
            }

            if (countTen == 0 && money > 10) {
                booleanTen = true
            } else if (countTen == 1 && money > 100) {
                booleanTen = true
            }
        }

        topView.setOnClickListener {
            money += 1
        }

        addTenButton.setOnClickListener {
            when (countTen) {
                0 -> {
                    value += 10
                    countTen++
                    booleanTen = false
                    moneyTen = "100"

                }
                1 -> {
                    value += 100
                    countTen++
                    booleanTen = false
                    moneyTen = "1000"
                }
            }
        }

        addHundredButton.setOnClickListener {
            value += 100
        }

        addThousandButton.setOnClickListener {
            value += 1000
        }

        addTenThousandButton.setOnClickListener {
            value += 10000
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart!")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume!")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause!")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop!")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy!")
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}