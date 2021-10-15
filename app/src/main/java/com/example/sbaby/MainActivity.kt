package com.example.sbaby

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val giftFragment = GiftFragment()
        val calendarFragment = CalendarFragment()
        val taskFragment = TaskFragment()
        val settingsFragment = SettingsFragment()


        setCurrentFragment(taskFragment)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.gift -> setCurrentFragment(giftFragment)
                R.id.calendar -> setCurrentFragment(calendarFragment)
                R.id.task -> setCurrentFragment(taskFragment)
                R.id.settings -> setCurrentFragment(settingsFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment).commit()
        }
}