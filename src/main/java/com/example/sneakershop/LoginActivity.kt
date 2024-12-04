package com.example.sneakershop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val registerBut : Button = findViewById(R.id.registerButton);
        val loginBut : Button = findViewById(R.id.loginButton);
        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)

        val auth = FirebaseAuth.getInstance()

        registerBut.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        loginBut.setOnClickListener{
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Вход успешен
                            val user = auth.currentUser
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                            // Переход на главный экран или экран пользователя
                            startActivity(Intent(this, SneakerListActivity::class.java))
                            finish() // Закрываем экран входа
                        } else {
                            // Ошибка при входе
                            Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
