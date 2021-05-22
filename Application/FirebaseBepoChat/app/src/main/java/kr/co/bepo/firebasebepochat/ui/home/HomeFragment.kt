package kr.co.bepo.firebasebepochat.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kr.co.bepo.firebasebepochat.R
import kr.co.bepo.firebasebepochat.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeToolBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_search -> {
                    Log.d(TAG, "Menu Search")
                }

                R.id.menu_person_add -> {
                    Log.d(TAG, "Menu Person Add")
                }
            }
            true
        }
        return binding.root
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}