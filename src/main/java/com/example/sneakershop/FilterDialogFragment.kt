package com.example.sneakershop

import android.app.Dialog
import android.os.Bundle
import android.widget.*
import androidx.fragment.app.DialogFragment

class FilterDialogFragment : DialogFragment() {

    interface FilterSelectionListener {
        fun onFilterSelected(color: String?, brand: String?)
    }

    private lateinit var listener: FilterSelectionListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_filter)

        val colorSpinner: Spinner = dialog.findViewById(R.id.colorSpinner)
        val brandSpinner: Spinner = dialog.findViewById(R.id.brandSpinner)
        val applyButton: Button = dialog.findViewById(R.id.applyButton)

        // Настройка спиннеров для выбора цвета и бренда
        colorSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOf("White", "Black", "Red", "Blue"))
        brandSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOf("Nike", "Adidas", "New Balance", "Asics"))

        applyButton.setOnClickListener {
            val selectedColor = colorSpinner.selectedItem?.toString()
            val selectedBrand = brandSpinner.selectedItem?.toString()

            listener.onFilterSelected(selectedColor, selectedBrand)
            dismiss()
        }

        return dialog
    }

    fun setFilterSelectionListener(listener: FilterSelectionListener) {
        this.listener = listener
    }
}
