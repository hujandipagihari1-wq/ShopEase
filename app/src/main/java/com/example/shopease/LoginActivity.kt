package com.example.shopease

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var editLoginEmail: EditText
    private lateinit var editLoginPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var textGoRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        editLoginEmail = findViewById(R.id.editLoginEmail)
        editLoginPassword = findViewById(R.id.editLoginPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        textGoRegister = findViewById(R.id.textGoRegister)

        buttonLogin.setOnClickListener {
            val email = editLoginEmail.text.toString().trim()
            val password = editLoginPassword.text.toString()

            if (email.isEmpty()) {
                editLoginEmail.error = "Email harus diisi"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                editLoginPassword.error = "Password harus diisi"
                return@setOnClickListener
            }

            Toast.makeText(
                this,
                "Login berhasil",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        textGoRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}