package com.example.sbaby.auth

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.sbaby.R

class AuthActivity : AppCompatActivity(R.layout.activity_auth) {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

    }

    override fun onStart() {
        super.onStart()
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<AuthRoleSelectionFragment>(R.id.fragment_container)
        }
    }
}