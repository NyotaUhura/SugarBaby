package com.example.sbaby.auth

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.sbaby.MainActivity
import com.example.sbaby.R
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.getKoin

class AuthActivity : AppCompatActivity(R.layout.activity_auth) {

    private val auth: FirebaseAuth by getKoin().inject()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        if (auth.currentUser != null) {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<AuthRoleSelectionFragment>(R.id.fragment_container)
        }
    }

    fun finishAuth(isParent: Boolean) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        /* val data = Intent().apply {
             data = Uri.parse(isParent.toString())
         }
         this.setResult(Activity.RESULT_OK, data)
         this.finish()*/
    }
}