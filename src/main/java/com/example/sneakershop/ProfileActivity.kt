package com.example.sneakershop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    private lateinit var uidTextView: TextView
    private lateinit var usernameTextView: TextView
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileImage = findViewById(R.id.profileImage)
        uidTextView = findViewById(R.id.uidTextView)
        usernameTextView = findViewById(R.id.usernameTextView)
        logoutButton = findViewById(R.id.logoutButton)

        // Получаем текущего пользователя
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Проверяем, есть ли пользователь
        if (currentUser != null) {
            // Отображаем UID и имя пользователя
            uidTextView.text = "UID: ${currentUser.uid}"
            usernameTextView.text = "Username: ${currentUser.displayName ?: "Не указано"}"
        } else {
            // Если пользователь не авторизован, перенаправляем на страницу входа
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Обработка нажатия кнопки logout
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
