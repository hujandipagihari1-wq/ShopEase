package com.example.shopease

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.shopease.data.model.CartItem
import com.example.shopease.data.remote.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale

class CartActivity : AppCompatActivity() {

    private lateinit var cartContainer: LinearLayout
    private lateinit var textCartTotal: TextView
    private var cartTotal = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)

        val rootLayout = findViewById<android.view.View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cartContainer = findViewById(R.id.cartContainer)
        textCartTotal = findViewById(R.id.textCartTotal)

        val buttonCheckout = findViewById<Button>(R.id.buttonCheckout)

        buttonCheckout.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            intent.putExtra("cart_total", cartTotal)
            startActivity(intent)
        }

        loadCart()
    }

    private fun loadCart() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.getCartItems()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val cartItems = response.body() ?: emptyList()
                        displayCart(cartItems)
                    } else {
                        Toast.makeText(
                            this@CartActivity,
                            "Gagal mengambil keranjang: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@CartActivity,
                        "Terjadi kesalahan: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun displayCart(cartItems: List<CartItem>) {
        cartContainer.removeAllViews()
        cartTotal = 0.0

        for (item in cartItems) {
            val itemLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(16, 16, 16, 16)
            }

            val productName = TextView(this).apply {
                text = item.product_name
                textSize = 17f
            }

            val productPrice = TextView(this).apply {
                text = formatRupiah(item.price)
                textSize = 15f
            }

            val quantityLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(0, 12, 0, 0)
            }

            val buttonMinus = Button(this).apply {
                text = "−"
                setOnClickListener { updateQuantity(item, item.quantity - 1) }
            }

            val textQuantity = TextView(this).apply {
                text = item.quantity.toString()
                textSize = 18f
                setPadding(20, 8, 20, 8)
            }

            val buttonPlus = Button(this).apply {
                text = "+"
                setOnClickListener { updateQuantity(item, item.quantity + 1) }
            }

            quantityLayout.addView(buttonMinus)
            quantityLayout.addView(textQuantity)
            quantityLayout.addView(buttonPlus)

            val buttonDelete = Button(this).apply {
                text = "Hapus"
                setOnClickListener { deleteCartItem(item) }
            }

            itemLayout.addView(productName)
            itemLayout.addView(productPrice)
            itemLayout.addView(quantityLayout)
            itemLayout.addView(buttonDelete)

            cartContainer.addView(itemLayout)

            cartTotal += item.price * item.quantity
        }

        textCartTotal.text = "Total: ${formatRupiah(cartTotal)}"
    }

    private fun updateQuantity(item: CartItem, newQuantity: Int) {
        if (newQuantity < 1) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.updateCartQuantity(
                    "eq.${item.id}",
                    mapOf("quantity" to newQuantity)
                )

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        loadCart()
                    } else {
                        Toast.makeText(this@CartActivity, "Gagal mengubah jumlah", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CartActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun deleteCartItem(item: CartItem) {
        val itemId = item.id
        if (itemId.isNullOrEmpty()) {
            Toast.makeText(this, "ID item tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.deleteCartItem("eq.$itemId")
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CartActivity, "Produk dihapus dari keranjang", Toast.LENGTH_SHORT).show()
                        loadCart()
                    } else {
                        Toast.makeText(this@CartActivity, "Gagal menghapus produk: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CartActivity, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun formatRupiah(value: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        format.maximumFractionDigits = 0
        return format.format(value)
    }
}
