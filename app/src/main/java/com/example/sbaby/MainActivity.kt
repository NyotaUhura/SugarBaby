package com.example.sbaby

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sbaby.gift.GiftFragment
import com.example.sbaby.task.TaskFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val actionBar: androidx.appcompat.app.ActionBar? = getSupportActionBar()
        actionBar?.hide()
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bindNavigationBar()
    }

    private fun bindNavigationBar() {
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.task -> setCurrentFragment(TaskFragment())
                R.id.gift -> setCurrentFragment(GiftFragment())
                R.id.calendar -> setCurrentFragment(CalendarFragment())
                R.id.settings -> setCurrentFragment(SettingsFragment())
            }
            true
        }
        bottomNavigationView.selectedItemId = R.id.task
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment).commit()
        }
}
