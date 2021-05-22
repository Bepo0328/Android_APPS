package kr.co.bepo.firebasebepochat.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import kr.co.bepo.firebasebepochat.R
import kr.co.bepo.firebasebepochat.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var navController: NavController

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this)

        binding.loginButton.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_loginFragment)
        }

        binding.registerTextView.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_RegisterFragment)

        }

        return binding.root
    }

    companion object {
        private const val TAG = "MainFragment"
    }
}