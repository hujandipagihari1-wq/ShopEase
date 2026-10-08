package com.example.shopease

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopease.data.model.Product
import com.example.shopease.ui.home.ProductAdapter
import com.example.shopease.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val productViewModel: ProductViewModel by viewModels()

    private lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerViewProducts: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var allProducts = emptyList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonCart = findViewById<Button>(R.id.buttonCart)

        buttonCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        val buttonCategory = findViewById<android.widget.Button>(R.id.buttonCategory)

        buttonCategory.setOnClickListener {
            val intent = android.content.Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }

        val buttonProfile = findViewById<android.widget.Button>(R.id.buttonProfile)

        buttonProfile.setOnClickListener {
            val intent = android.content.Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        recyclerViewProducts = findViewById(R.id.recyclerViewProducts)
        progressBar = findViewById(R.id.progressBar)

        setupRecyclerView()
        observeProducts()
        setupSearch()

        productViewModel.loadProducts()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(emptyList()) { product ->
            val intent = Intent(this, ProductDetailActivity::class.java).apply {
                putExtra("product_id", product.id)
                putExtra("product_name", product.name)
                putExtra("product_price", product.price)
                putExtra("product_rating", product.rating)
                putExtra("product_category", product.category)
                putExtra("product_description", product.description)
                putExtra("product_stock", product.stock)
                putExtra("product_image", product.image_url)
            }
            startActivity(intent)
        }

        recyclerViewProducts.layoutManager = GridLayoutManager(this, 2)
        recyclerViewProducts.adapter = productAdapter
    }

    private fun setupSearch() {
        val searchEditText = findViewById<EditText>(R.id.searchEditText)

        searchEditText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val keyword = s.toString().trim()

                val filteredProducts = if (keyword.isEmpty()) {
                    allProducts
                } else {
                    allProducts.filter { product ->
                        product.name.contains(keyword, ignoreCase = true) ||
                                product.category.contains(keyword, ignoreCase = true)
                    }
                }

                productAdapter.updateProducts(filteredProducts)
            }

            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    private fun observeProducts() {
        lifecycleScope.launch {
            productViewModel.products.collect { products ->
                allProducts = products
                productAdapter.updateProducts(products)
            }
        }

        lifecycleScope.launch {
            productViewModel.isLoading.collect { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            productViewModel.errorMessage.collect { message ->
                if (!message.isNullOrEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
