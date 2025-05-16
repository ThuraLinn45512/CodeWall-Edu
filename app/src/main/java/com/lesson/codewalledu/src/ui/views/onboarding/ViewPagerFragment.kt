package com.lesson.codewalledu.src.ui.views.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.lesson.codewalledu.databinding.FragmentViewPagerBinding
import com.lesson.codewalledu.R
import com.lesson.codewalledu.src.ui.adapters.onboarding.ViewPagerAdapter


class ViewPagerFragment : Fragment() {

    lateinit var binding: FragmentViewPagerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding = FragmentViewPagerBinding.bind(view)
        val viewPagerAdapter = ViewPagerAdapter(requireActivity())
        binding.viewpagerOnboarding.adapter = viewPagerAdapter


        ///Change viewPager orientation
        binding.viewpagerOnboarding.orientation = ViewPager2.ORIENTATION_VERTICAL
        binding.dotsIndicator.attachTo(binding.viewpagerOnboarding)




    }
}