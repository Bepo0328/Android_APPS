package kr.co.bepo.androidonline.essential_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.bepo.androidonline.databinding.ActivityCalculator2Binding

class Calculator2 : AppCompatActivity() {

    private lateinit var binding: ActivityCalculator2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalculator2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        var new = "0"
        var old = "0"

        binding.one.setOnClickListener {
            new += "1"
            binding.result.text = new
        }
        binding.two.setOnClickListener {
            new += "2"
            binding.result.text = new
        }
        binding.three.setOnClickListener {
            new += "3"
            binding.result.text = new
        }
        binding.four.setOnClickListener {
            new += "4"
            binding.result.text = new
        }
        binding.five.setOnClickListener {
            new += "5"
            binding.result.text = new
        }
        binding.six.setOnClickListener {
            new += "6"
            binding.result.text = new
        }
        binding.seven.setOnClickListener {
            new += "7"
            binding.result.text = new
        }
        binding.eight.setOnClickListener {
            new += "8"
            binding.result.text = new
        }
        binding.nine.setOnClickListener {
            new += "9"
            binding.result.text = new
        }
        binding.zero.setOnClickListener {
            new += "0"
            binding.result.text = new
        }

        binding.plus.setOnClickListener {
            old = (old.toInt() + new.toInt()).toString()
            new = "0"
            binding.result.setText(old)
        }

        binding.clear.setOnClickListener {
            old = "0"
            new = "0"
            binding.result.setText(old)
        }

    }
}