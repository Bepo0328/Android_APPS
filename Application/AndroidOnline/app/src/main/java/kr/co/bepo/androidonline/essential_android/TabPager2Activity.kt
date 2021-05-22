package kr.co.bepo.androidonline.essential_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.bepo.androidonline.R
import kr.co.bepo.androidonline.databinding.ActivityTabPager2Binding

class TabPager2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityTabPager2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTabPager2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("One"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Two"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Three"))

        val adapter = PageAdapter2(LayoutInflater.from(this), 3)
//        binding.viewPager.adapter = adapter

        val tabLayoutTextArray = arrayOf("One", "Two", "Three")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabLayoutTextArray[position]
        }.attach()
    }
}

class PageAdapter2(
    private val layoutInflater: LayoutInflater,
    private val tabCount: Int
) : PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return when (position) {
            1 -> {
                val view = layoutInflater.inflate(R.layout.fragment_one, container, false)
                container.addView(view)
                view
            }
            2 -> {
                val view = layoutInflater.inflate(R.layout.fragment_two, container, false)
                container.addView(view)
                view
            }
            3 -> {
                val view = layoutInflater.inflate(R.layout.fragment_three, container, false)
                container.addView(view)
                view
            }
            else -> {
                val view = layoutInflater.inflate(R.layout.fragment_one, container, false)
                container.addView(view)
                view
            }
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return tabCount
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View
    }
}