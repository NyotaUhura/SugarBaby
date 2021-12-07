package com.example.sbaby

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.sbaby.auth.AuthActivity
import com.example.sbaby.auth.FirebaseAuthManager
import com.example.sbaby.calendar.CalendarFragment
import com.example.sbaby.gift.GiftFragment
import com.example.sbaby.task.TaskFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.getKoin


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private val authManager: FirebaseAuthManager by getKoin().inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val actionBar: androidx.appcompat.app.ActionBar? = supportActionBar
        actionBar?.hide()
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bindNavigationBar()

        lifecycleScope.launchWhenCreated {
            authManager.firebaseUserId.collect { user ->
                if (user == null) {
                    startActivity(Intent(this@MainActivity, AuthActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun startAuthActivity() {
        val content = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                bindNavigationBar()
            }
        }
        content.launch(Intent(applicationContext, AuthActivity::class.java))
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

    override fun onResume() {
        super.onResume()
        FullScreencall()
    }

    fun FullScreencall() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            val v = this.window.decorView
            v.systemUiVisibility = View.GONE
        } else if (Build.VERSION.SDK_INT >= 19) {
            val decorView = window.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            decorView.systemUiVisibility = uiOptions
        }
    }
}
