package com.example.shopease

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.NumberFormat
import java.util.Locale

class PaymentActivity : AppCompatActivity() {

    private lateinit var textPaymentTotal: TextView
    private lateinit var buttonPay: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment)

        val rootLayout = findViewById<android.view.View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textPaymentTotal = findViewById(R.id.textPaymentTotal)
        buttonPay = findViewById(R.id.buttonPay)

        val totalPayment = intent.getDoubleExtra("total_payment", 0.0)
        val address = intent.getStringExtra("address") ?: ""
        val shippingMethod = intent.getStringExtra("shipping_method") ?: "Regular"
        val shippingCost = intent.getDoubleExtra("shipping_cost", 0.0)
        val cartTotal = intent.getDoubleExtra("cart_total", 0.0)

        textPaymentTotal.text = "Total Pembayaran: ${formatRupiah(totalPayment)}"

        buttonPay.setOnClickListener {
            val intent = Intent(this, PaymentSandboxActivity::class.java).apply {
                putExtra("address", address)
                putExtra("shipping_method", shippingMethod)
                putExtra("shipping_cost", shippingCost)
                putExtra("cart_total", cartTotal)
                putExtra("total_payment", totalPayment)
            }
            startActivity(intent)
        }
    }

    private fun formatRupiah(value: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        format.maximumFractionDigits = 0
        return format.format(value)
    }
}
