package com.example.sneakershop

import Sneaker
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.example.sneakershop.R
//import com.example.sneakershop.CartManager

class SneakerDetailsActivity : AppCompatActivity() {

    private var selectedSize: String? = null // Для хранения выбранного размера
    private lateinit var addToCartButton: Button // Кнопка добавления в корзину

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sneaker_details)

        val sneaker = intent.getParcelableExtra<Sneaker>("sneaker")
        if (sneaker != null) {
            // Заполнение UI данными кроссовка
            val sneakerImage = findViewById<ImageView>(R.id.sneakerImage)
            val sneakerModel = findViewById<TextView>(R.id.sneakerModel)
            val sneakerBrand = findViewById<TextView>(R.id.sneakerBrand)
            val sneakerPrice = findViewById<TextView>(R.id.sneakerPrice)
            val sizeButtonsContainer = findViewById<LinearLayout>(R.id.sizeButtonsContainer)
            val selectedSizeText = findViewById<TextView>(R.id.selectedSize)
            addToCartButton = findViewById(R.id.addToCartButton)

            Picasso.get().load(sneaker.imageUrl).into(sneakerImage)
            sneakerModel.text = sneaker.model
            sneakerBrand.text = sneaker.brand
            sneakerPrice.text = "$${sneaker.price}"

            // Добавление кнопок для выбора размера
            val sizes = arrayOf("9.0", "9.5", "10.0", "10.5", "11.0", "11.5", "12.0")

            for (size in sizes) {
                val button = Button(this)
                button.text = size
                val params = LinearLayout.LayoutParams(
                    0, // Ширина равна части контейнера
                    LinearLayout.LayoutParams.WRAP_CONTENT // Высота кнопки
                )
                params.weight = 1f // Кнопка занимает равную долю ширины
                button.layoutParams = params

                button.setOnClickListener {
                    selectedSize = size
                    selectedSizeText.text = "Выбран размер: $size"
                }
                sizeButtonsContainer.addView(button)
            }

            val checkAvailabilityButton = findViewById<Button>(R.id.addToCartButton)
            checkAvailabilityButton.setOnClickListener {
                // Создаем и показываем AlertDialog
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Информация о наличии")
                    .setMessage("Для уточнения информации о данной паре кроссовок звонить по номеру +375333710167")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .create()

                dialog.show()
            }
        }
    }
}