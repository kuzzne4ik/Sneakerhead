package com.example.sneakershop

import Sneaker
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.example.sneakershop.R
import com.example.sneakershop.SneakerDetailsActivity

class SneakersAdapter(private val context: Context, private val sneakerList: List<Sneaker>) : RecyclerView.Adapter<SneakersAdapter.SneakerViewHolder>() {

    init {
        System.loadLibrary("native-lib")
    }
    external fun updateLastClickTime(currentTime: Long): Long

    inner class SneakerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val sneakerImage: ImageView = itemView.findViewById(R.id.sneakerImage)
        private val sneakerModel: TextView = itemView.findViewById(R.id.sneakerModel)
        private val sneakerBrand: TextView = itemView.findViewById(R.id.sneakerBrand)
        private val sneakerPrice: TextView = itemView.findViewById(R.id.sneakerPrice)

        private val handler = Handler()
        private var lastClickTime: Long = 0

        fun bind(sneaker: Sneaker) {
            sneakerModel.text = sneaker.model
            sneakerBrand.text = sneaker.brand
            sneakerPrice.text = "$${sneaker.price}"

            // Загружаем изображение через Picasso
            Picasso.get()
                .load(sneaker.imageUrl)
                .into(sneakerImage)

            // Обработка нажатий
            sneakerImage.setOnClickListener {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime < 300) { // Проверка времени для двойного нажатия (300 мс)
                    // Двойное нажатие
                    sneakerImage.scaleX = 1.5f
                    sneakerImage.scaleY = 1.5f

                    // Устанавливаем задержку на 1 секунду, чтобы вернуть изображение к нормальному размеру
                    handler.postDelayed({
                        sneakerImage.scaleX = 1.0f
                        sneakerImage.scaleY = 1.0f
                    }, 1000)
                } else {
                    // Одиночное нажатие
                    handler.postDelayed({
                        if (currentTime == lastClickTime) { // Проверка, что не произошло двойного нажатия
                            val intent = Intent(context, SneakerDetailsActivity::class.java)
                            intent.putExtra("sneaker", sneaker) // Передача объекта
                            context.startActivity(intent)
                        }
                    }, 300) // Задержка для проверки одиночного нажатия (300 мс)
                }

                lastClickTime = updateLastClickTime(currentTime)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SneakerViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_sneaker, parent, false)
        return SneakerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SneakerViewHolder, position: Int) {
        val sneaker = sneakerList[position]
        holder.bind(sneaker)
    }

    override fun getItemCount(): Int {
        return sneakerList.size
    }
}
