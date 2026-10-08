package com.example.shopease

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var editRegisterName: EditText
    private lateinit var editRegisterEmail: EditText
    private lateinit var editRegisterPassword: EditText
    private lateinit var buttonRegister: Button
    private lateinit var textGoLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        editRegisterName = findViewById(R.id.editRegisterName)
        editRegisterEmail = findViewById(R.id.editRegisterEmail)
        editRegisterPassword = findViewById(R.id.editRegisterPassword)
        buttonRegister = findViewById(R.id.buttonRegister)
        textGoLogin = findViewById(R.id.textGoLogin)

        buttonRegister.setOnClickListener {
            val name = editRegisterName.text.toString().trim()
            val email = editRegisterEmail.text.toString().trim()
            val password = editRegisterPassword.text.toString()

            if (name.isEmpty()) {
                editRegisterName.error = "Nama harus diisi"
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                editRegisterEmail.error = "Email harus diisi"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                editRegisterPassword.error = "Password harus diisi"
                return@setOnClickListener
            }

            Toast.makeText(
                this,
                "Pendaftaran berhasil",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        textGoLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}