package com.example.shopease

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import com.example.shopease.data.model.CartItem
import com.example.shopease.data.remote.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var imageDetail: ImageView
    private lateinit var textDetailName: TextView
    private lateinit var textDetailPrice: TextView
    private lateinit var textDetailRating: TextView
    private lateinit var textDetailCategory: TextView
    private lateinit var textDetailDescription: TextView
    private lateinit var textDetailStock: TextView
    private lateinit var buttonAddToCart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_detail)

        val rootLayout = findViewById<android.view.View>(R.id.imageDetail)?.parent?.parent as? android.view.View
            ?: findViewById(android.R.id.content)

        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageDetail = findViewById(R.id.imageDetail)
        textDetailName = findViewById(R.id.textDetailName)
        textDetailPrice = findViewById(R.id.textDetailPrice)
        textDetailRating = findViewById(R.id.textDetailRating)
        textDetailCategory = findViewById(R.id.textDetailCategory)
        textDetailDescription = findViewById(R.id.textDetailDescription)
        textDetailStock = findViewById(R.id.textDetailStock)
        buttonAddToCart = findViewById(R.id.buttonAddToCart)

        val productName = intent.getStringExtra("product_name")
        val productPrice = intent.getDoubleExtra("product_price", 0.0)
        val productRating = intent.getDoubleExtra("product_rating", 0.0)
        val productCategory = intent.getStringExtra("product_category")
        val productDescription = intent.getStringExtra("product_description")
        val productStock = intent.getIntExtra("product_stock", 0)
        val productImage = intent.getStringExtra("product_image")

        textDetailName.text = productName ?: "Nama Produk"
        textDetailPrice.text = "Rp${String.format("%,.0f", productPrice).replace(",", ".")}"
        textDetailRating.text = "★ $productRating"
        textDetailCategory.text = "Kategori: ${productCategory ?: "-"}"
        textDetailDescription.text = productDescription ?: "Belum ada deskripsi produk."
        textDetailStock.text = "Stok tersedia: $productStock"

        imageDetail.load(productImage) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_foreground)
            error(R.drawable.ic_launcher_foreground)
        }

        buttonAddToCart.setOnClickListener {
            val productId = intent.getStringExtra("product_id")

            if (productId == null) {
                Toast.makeText(this, "Data produk tidak ditemukan", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cartItem = CartItem(
                user_id = "guest",
                product_id = productId,
                product_name = productName ?: "Produk",
                price = productPrice,
                image_url = productImage,
                quantity = 1
            )

            buttonAddToCart.isEnabled = false
            buttonAddToCart.text = "Menambahkan..."

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitClient.api.addToCart(cartItem)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@ProductDetailActivity,
                                "Produk berhasil ditambahkan ke keranjang",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@ProductDetailActivity,
                                "Gagal menambahkan produk: ${response.code()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        buttonAddToCart.isEnabled = true
                        buttonAddToCart.text = "Tambah ke Keranjang"
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ProductDetailActivity,
                            "Terjadi kesalahan: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        buttonAddToCart.isEnabled = true
                        buttonAddToCart.text = "Tambah ke Keranjang"
                    }
                }
            }
        }
    }
}
