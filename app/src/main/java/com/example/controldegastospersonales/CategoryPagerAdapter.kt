package com.example.controldegastospersonales

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CategoryPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ExpenseCategoriesFragment()
            1 -> IncomeCategoriesFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }
}
