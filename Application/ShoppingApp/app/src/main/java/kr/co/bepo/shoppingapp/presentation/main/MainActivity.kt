package kr.co.bepo.shoppingapp.presentation.main

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kr.co.bepo.shoppingapp.R
import kr.co.bepo.shoppingapp.databinding.ActivityMainBinding
import kr.co.bepo.shoppingapp.presentation.BaseActivity
import kr.co.bepo.shoppingapp.presentation.BaseFragment
import kr.co.bepo.shoppingapp.presentation.list.ProductListFragment
import kr.co.bepo.shoppingapp.presentation.profile.ProfileFragment
import org.koin.android.ext.android.inject

internal class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val viewModel: MainViewModel by inject()

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_products -> {
                    showFragment(ProductListFragment(), ProductListFragment.TAG)
                    return@setOnItemSelectedListener true
                }
                R.id.menu_profile -> {
                    showFragment(ProfileFragment(), ProfileFragment.TAG)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
        showFragment(ProductListFragment(), ProductListFragment.TAG)
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.fragments.forEach { fm ->
            supportFragmentManager.beginTransaction().hide(fm).commit()
        }
        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commit()
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()
        }
    }

    override fun observeData() = viewModel.mainStateLiveData.observe(this) {
        when (it) {
            is MainState.RefreshOrderList -> {
                binding.bottomNav.selectedItemId = R.id.menu_profile
                val fragment = supportFragmentManager.findFragmentByTag(ProfileFragment.TAG)
                (fragment as? BaseFragment<*, *>)?.viewModel?.fetchData()
            }
        }
    }
}