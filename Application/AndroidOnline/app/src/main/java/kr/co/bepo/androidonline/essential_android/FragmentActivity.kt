package kr.co.bepo.androidonline.essential_android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import kr.co.bepo.androidonline.R
import kr.co.bepo.androidonline.databinding.ActivityFragmentBinding

class FragmentActivity : AppCompatActivity(), FragmentOne.OnDataPassListener {
    private lateinit var binding: ActivityFragmentBinding

    override fun onDataPass(data: String?) {
        Log.d(TAG,"data: $data")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "onCreate!")

        val fragmentOne: FragmentOne = FragmentOne()
        val bundle: Bundle = Bundle()
        bundle.putString("hello", "hello")
        fragmentOne.arguments = bundle

        binding.showButton.setOnClickListener {
            val fragmentManger: FragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManger.beginTransaction()
            fragmentTransaction.replace(R.id.containerLayout, fragmentOne)
            fragmentTransaction.commit()
        }

        binding.clearButton.setOnClickListener {
            val fragmentManger: FragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManger.beginTransaction()
            fragmentTransaction.remove(fragmentOne)
            fragmentTransaction.commit()
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
        private const val TAG = "FragmentActivity"
    }


}