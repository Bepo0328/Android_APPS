package kr.co.bepo.androidonline.essential_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.bepo.androidonline.databinding.ActivityTabPagerBinding

class TabPagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTabPagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTabPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("One"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Two"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Three"))

        val pagerAdapter = PagerAdapter(this, 3)
        binding.viewPager.adapter = pagerAdapter

//        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//            }
//
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                binding.viewPager.currentItem = tab?.position ?: 0
//            }
//        })

        val tabLayoutTextArray = arrayOf("One", "Two", "Three")

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabLayoutTextArray[position]
        }.attach()
    }
}

class PagerAdapter(
    private val fragmentActivity: FragmentActivity,
    private val tabCount: Int
): FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Fragment1()
            1 -> Fragment2()
            2 -> Fragment3()
            else -> Fragment1()
        }
    }

    override fun getItemCount(): Int = tabCount
}
