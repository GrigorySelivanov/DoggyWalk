package com.example.doggywalk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //val для статических типов
        val email: EditText = findViewById(R.id.email_auth)
        val password: EditText = findViewById(R.id.password_auth)
        val button: Button = findViewById(R.id.button_auth)
        val linkToReg: TextView = findViewById(R.id.register_auth)

        linkToReg.setOnClickListener{
            val intent = Intent(this, MainActivity:: class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            val e = email.text.toString().trim()
            val pass = password.text.toString().trim()

            if (e == "" || pass == "")
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show()
            else {
                val db = DbContext(this, null)

                if (db.checkUser(e, pass)) {
                    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("login", e)
                    editor.apply()
                    val intent = Intent(this, MainScreenActivity:: class.java)
                    startActivity(intent)
                }
                else
                    Toast.makeText(this, "Некорректный логин или пароль!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}