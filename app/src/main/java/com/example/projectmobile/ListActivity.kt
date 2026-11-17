package com.example.projectmobile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListActivity : AppCompatActivity() {

    private lateinit var adapter: ItemAdapter
    private val productList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        // Initialize some default products
        for (i in 1..5) {
            productList.add(Product("Product $i", "Description for product $i"))
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ItemAdapter(productList) { position, product ->
            showProductDialog(position, product)
        }
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.addButton).setOnClickListener {
            showProductDialog(null, null)
        }

        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            finish() // Close this activity to return to login screen
        }
    }

    private fun showProductDialog(position: Int?, product: Product?) {
        val dialogView = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val titleInput = EditText(this).apply {
            hint = "Product Title"
            setText(product?.title ?: "")
        }
        val descInput = EditText(this).apply {
            hint = "Product Description"
            setText(product?.description ?: "")
        }

        dialogView.addView(titleInput)
        dialogView.addView(descInput)

        AlertDialog.Builder(this)
            .setTitle(if (product == null) "Add Product" else "Edit Product")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newTitle = titleInput.text.toString()
                val newDesc = descInput.text.toString()
                
                if (newTitle.isNotEmpty()) {
                    if (product == null) {
                        // Add new product
                        productList.add(Product(newTitle, newDesc))
                        adapter.notifyItemInserted(productList.size - 1)
                    } else if (position != null) {
                        // Edit existing product
                        product.title = newTitle
                        product.description = newDesc
                        adapter.notifyItemChanged(position)
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}