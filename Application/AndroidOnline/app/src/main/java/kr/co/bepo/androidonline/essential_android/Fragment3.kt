package kr.co.bepo.androidonline.essential_android

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.bepo.androidonline.R

class Fragment3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "Fragment3 onCreateView!")
        return inflater.inflate(R.layout.fragment_three, container, false)
    }

    companion object {
        private const val TAG = "Fragment3"
    }
}