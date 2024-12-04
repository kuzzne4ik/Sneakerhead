package com.example.sneakershop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth

class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        FirebaseAuth.getInstance().signOut()

        val loginButton: Button = findViewById(R.id.loginButton)
        val skipButton: Button = findViewById(R.id.skipButton)

        loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        skipButton.setOnClickListener{
            val intent = Intent(this, SneakerListActivity::class.java);
            startActivity(intent)
        }
    }

}
