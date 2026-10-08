package com.example.shopease

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.NumberFormat
import java.util.Locale

class PaymentSandboxActivity : AppCompatActivity() {

    private lateinit var textSandboxTotal: TextView
    private lateinit var textCountdown: TextView
    private lateinit var buttonCheckPayment: Button

    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment_sandbox)

        val rootLayout = findViewById<android.view.View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textSandboxTotal = findViewById(R.id.textSandboxTotal)
        textCountdown = findViewById(R.id.textCountdown)
        buttonCheckPayment = findViewById(R.id.buttonCheckPayment)

        val totalPayment = intent.getDoubleExtra("total_payment", 0.0)
        textSandboxTotal.text = "Total: ${formatRupiah(totalPayment)}"

        startCountdown()

        buttonCheckPayment.setOnClickListener {
            val successIntent = Intent(this, PaymentSuccessActivity::class.java).apply {
                putExtra("total_payment", totalPayment)
            }
            startActivity(successIntent)
            finish()
        }
    }

    private fun startCountdown() {
        countDownTimer = object : CountDownTimer(5 * 60 * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 60000
                val seconds = (millisUntilFinished % 60000) / 1000
                textCountdown.text = String.format(
                    Locale.getDefault(),
                    "Batas pembayaran: %02d:%02d",
                    minutes,
                    seconds
                )
            }

            override fun onFinish() {
                textCountdown.text = "Waktu pembayaran habis"
                buttonCheckPayment.isEnabled = false
                Toast.makeText(
                    this@PaymentSandboxActivity,
                    "Waktu pembayaran telah habis",
                    Toast.LENGTH_LONG
                ).show()
            }
        }.start()
    }

    private fun formatRupiah(value: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        format.maximumFractionDigits = 0
        return format.format(value)
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}
