package kr.co.bepo.androidonline.essential_android

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.bepo.androidonline.databinding.FragmentOneBinding

class FragmentOne : Fragment() {
    private lateinit var binding: FragmentOneBinding

    interface OnDataPassListener {
        fun onDataPass(data: String?)
    }

    private lateinit var dataPassListener: OnDataPassListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "F onAttach!")
        dataPassListener = context as OnDataPassListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "F onCreate!")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "F onCreateView!")
        binding = FragmentOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "F onViewCreated!")

        binding.passButton.setOnClickListener {
            dataPassListener.onDataPass("Good Bye")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "F onActivityCreated!")

        val data = arguments?.getString("hello").orEmpty()
        Log.d(TAG, "data: $data")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "F onStart!")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "F onResume!")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "F onPause!")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "F onStop!")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "F onDestroyView!")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "F onDetach!")

    }

    companion object {
        private const val TAG = "FragmentOne"
    }
}