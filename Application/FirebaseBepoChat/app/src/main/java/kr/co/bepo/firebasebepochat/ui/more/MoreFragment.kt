package kr.co.bepo.firebasebepochat.ui.more

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.bepo.firebasebepochat.databinding.FragmentMoreBinding
import kr.co.bepo.firebasebepochat.ui.login.LoginFragment

class MoreFragment : Fragment() {
    private lateinit var binding: FragmentMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreBinding.inflate(inflater, container, false)

        return binding.root
    }
    companion object {
        private const val TAG = "MoreFragment"
    }
}