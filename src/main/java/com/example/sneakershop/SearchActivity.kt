package com.example.sneakershop

import Sneaker
import com.example.sneakershop.SneakersAdapter
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var filterButton: Button
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var adapter: SneakersAdapter

    private val filterOptions = arrayOf("white", "black", "red", "blue", "green", "Nike", "Adidas", "New Balance", "Asics")
    private val selectedFiltersState = BooleanArray(filterOptions.size) // Хранит состояния выбора фильтров
    private val selectedFilters = mutableListOf<String>() // Хранит выбранные фильтры

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Инициализация элементов
        searchEditText = findViewById(R.id.searchEditText)
        filterButton = findViewById(R.id.filterButton)
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView)

        // Настройка RecyclerView для отображения результатов в виде сетки с 2 колонками
        searchResultsRecyclerView.layoutManager = GridLayoutManager(this, 2)

        database = FirebaseDatabase.getInstance().getReference("sneakers")

        // Обработчик нажатия кнопки фильтров
        filterButton.setOnClickListener {
            showFilterDialog()
        }

        // Слушатель нажатия кнопки поиска
        searchEditText.setOnEditorActionListener { _, _, _ ->
            val query = searchEditText.text.toString().trim()
            searchSneakers(query, selectedFilters)
            true
        }
    }

    private fun showFilterDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Filters")
        builder.setMultiChoiceItems(filterOptions, selectedFiltersState) { _, which, isChecked ->
            selectedFiltersState[which] = isChecked // Обновляем состояние выбора
        }
        builder.setPositiveButton("Apply") { _, _ ->
            // Обновляем список выбранных фильтров
            selectedFilters.clear()
            for (i in filterOptions.indices) {
                if (selectedFiltersState[i]) {
                    selectedFilters.add(filterOptions[i])
                }
            }
            val query = searchEditText.text.toString().trim()
            searchSneakers(query, selectedFilters) // Обновляем результаты поиска
        }
        builder.setNegativeButton("Cancel", null)
        builder.setNeutralButton("Clear All") { _, _ ->
            selectedFiltersState.fill(false) // Сбрасываем выбор фильтров
            selectedFilters.clear()
            val query = searchEditText.text.toString().trim()
            searchSneakers(query, selectedFilters) // Обновляем результаты без фильтров
        }
        builder.create().show()
    }

    private fun searchSneakers(query: String, filters: List<String>) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val sneakers = mutableListOf<Sneaker>()
                for (data in snapshot.children) {
                    val sneaker = data.getValue(Sneaker::class.java)
                    sneaker?.let {
                        val matchesQuery = query.isEmpty() || it.model.contains(query, ignoreCase = true) || it.brand.contains(query, ignoreCase = true)
                        val matchesFilters = filters.isEmpty() || filters.contains(it.color) || filters.contains(it.brand)

                        if (matchesQuery && matchesFilters) {
                            sneakers.add(it)
                        }
                    }
                }
                // Установка адаптера для отображения результатов
                adapter = SneakersAdapter(this@SearchActivity, sneakers)
                searchResultsRecyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SearchActivity, "Failed to load data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
