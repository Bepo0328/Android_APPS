package kr.co.bepo.firebasebepochat.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import kr.co.bepo.firebasebepochat.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this)

        binding.backImageView.setOnClickListener {
            navController.popBackStack()
        }

        return binding.root
    }

    companion object {
        private const val TAG = "RegisterFragment"
    }
}