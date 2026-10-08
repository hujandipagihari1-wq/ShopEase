package com.example.shopease

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.NumberFormat
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {

    private lateinit var editAddress: EditText
    private lateinit var radioRegular: RadioButton
    private lateinit var radioExpress: RadioButton
    private lateinit var textCheckoutTotal: TextView
    private lateinit var buttonContinueCheckout: Button

    private var cartTotal = 0.0
    private var shippingCost = 10_000.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_checkout)

        val rootLayout = findViewById<android.view.View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editAddress = findViewById(R.id.editAddress)
        radioRegular = findViewById(R.id.radioRegular)
        radioExpress = findViewById(R.id.radioExpress)
        textCheckoutTotal = findViewById(R.id.textCheckoutTotal)
        buttonContinueCheckout = findViewById(R.id.buttonContinueCheckout)

        cartTotal = intent.getDoubleExtra("cart_total", 0.0)
        radioRegular.isChecked = true
        updateTotal()

        radioRegular.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                shippingCost = 10_000.0
                updateTotal()
            }
        }

        radioExpress.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                shippingCost = 20_000.0
                updateTotal()
            }
        }

        buttonContinueCheckout.setOnClickListener {
            val address = editAddress.text.toString().trim()

            if (address.isEmpty()) {
                editAddress.error = "Alamat harus diisi"
                return@setOnClickListener
            }

            val totalPayment = cartTotal + shippingCost
            val intent = Intent(this, PaymentActivity::class.java).apply {
                putExtra("address", address)
                putExtra("shipping_method", if (radioExpress.isChecked) "Express" else "Regular")
                putExtra("shipping_cost", shippingCost)
                putExtra("cart_total", cartTotal)
                putExtra("total_payment", totalPayment)
            }
            startActivity(intent)
        }
    }

    private fun updateTotal() {
        val totalPayment = cartTotal + shippingCost
        textCheckoutTotal.text = "Total Pembayaran: ${formatRupiah(totalPayment)}"
    }

    private fun formatRupiah(value: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        format.maximumFractionDigits = 0
        return format.format(value)
    }
}
