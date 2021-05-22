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
import kr.co.bepo.firebasebepochat.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var navController: NavController

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this)

        binding.backImageView.setOnClickListener {
            Log.d(TAG, "${navController.currentBackStackEntry}")
            Log.d(TAG, "${navController.backStack}")
            Log.d(TAG, "${navController.previousBackStackEntry}")

            if (navController.previousBackStackEntry != null) {
                navController.popBackStack()
            }
        }

        binding.registerTextView.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_RegisterFragment)
        }

        binding.loginAnonymousButton.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_HomeFragment)
        }

        return binding.root
    }

    companion object {
        private const val TAG = "LoginFragment"
    }
}