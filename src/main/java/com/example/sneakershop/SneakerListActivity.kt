package com.example.sneakershop

import Sneaker
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.*

class SneakerListActivity : AppCompatActivity() {

    private lateinit var sneakersRecyclerView: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var adapter: SneakersAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sneaker_list)

        // Инициализация SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        sneakersRecyclerView = findViewById(R.id.sneakersRecyclerView)
        sneakersRecyclerView.layoutManager = GridLayoutManager(this, 2)

        // Инициализация базы данных
        database = FirebaseDatabase.getInstance().getReference("sneakers")

        // Настройка адаптера
        adapter = SneakersAdapter(this, emptyList())
        sneakersRecyclerView.adapter = adapter

        // Загружаем данные с базы данных
        fetchSneakers()

        // Устанавливаем слушатель для SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            fetchSneakers()
        }

        val SearchButton: Button = findViewById(R.id.menuItem2);
        SearchButton.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        val ProfileButton: Button = findViewById(R.id.menuItem1);
        ProfileButton.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchSneakers() {
        swipeRefreshLayout.isRefreshing = true // Показываем индикатор обновления

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val sneakers = mutableListOf<Sneaker>()
                for (data in snapshot.children) {
                    val sneaker = data.getValue(Sneaker::class.java)
                    sneaker?.let {
                        sneakers.add(it)
                    }
                }

                // Обновляем адаптер с новыми данными
                adapter = SneakersAdapter(this@SneakerListActivity, sneakers)
                sneakersRecyclerView.adapter = adapter

                swipeRefreshLayout.isRefreshing = false // Скрываем индикатор обновления
            }

            override fun onCancelled(error: DatabaseError) {
                swipeRefreshLayout.isRefreshing = false // Скрываем индикатор обновления при ошибке
                Toast.makeText(this@SneakerListActivity, "Failed to load data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
