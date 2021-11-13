package com.example.sbaby

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sbaby.auth.AuthActivity
import com.example.sbaby.auth.FirebaseAuthManager
import com.example.sbaby.gift.GiftFragment
import com.example.sbaby.task.TaskFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.getKoin

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private val authManager: FirebaseAuthManager by getKoin().inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val actionBar: androidx.appcompat.app.ActionBar? = getSupportActionBar()
        actionBar?.hide()
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        if (!authManager.isLoginIn()) {
            val content = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    bindNavigationBar()
                }
            }
            content.launch(Intent(applicationContext, AuthActivity::class.java))
        } else {
            bindNavigationBar()
        }
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
