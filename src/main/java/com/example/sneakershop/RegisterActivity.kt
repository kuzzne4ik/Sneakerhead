package com.example.sneakershop

import androidx.appcompat.widget.Toolbar
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val registerButton : Button = findViewById(R.id.registerButton);

        val  auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        registerButton.setOnClickListener{

            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Регистрация успешна
                            val user = auth.currentUser
                            saveUserData(user?.uid, email)
                            startActivity(Intent(this, SneakerListActivity::class.java))
                        } else {
                            // Ошибка при регистрации
                            Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            finish()
        }
    }

    private fun saveUserData(userId: String?, email: String) {
        val database = FirebaseDatabase.getInstance().reference
        val user = User(userId, email)

        userId?.let {
            database.child("users").child(it).setValue(user)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                        // Переходим на экран входа
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish() // Закрываем экран регистрации
                    } else {
                        Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}