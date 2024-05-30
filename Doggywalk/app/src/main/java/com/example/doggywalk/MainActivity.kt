package com.example.doggywalk

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
import com.example.doggywalk.Models.User

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        val name: EditText = findViewById(R.id.name)
        val email: EditText = findViewById(R.id.email)
        val password: EditText = findViewById(R.id.password)
        val button: Button = findViewById(R.id.accept)
        val linkToLogin: TextView = findViewById(R.id.login)

        linkToLogin.setOnClickListener{
            val intent = Intent(this, AuthActivity:: class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            val n = name.text.toString().trim()
            val e = email.text.toString().trim()
            val pass = password.text.toString().trim()

            if (n == "" || e == "" || pass == "")
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show()
            else {
                val user =  User(0,n, e,"","", pass)
                val db = DbContext(this, null)
                db.addUser(user)
                Toast.makeText(this, "Успешно!", Toast.LENGTH_SHORT).show()

                name.text.clear()
                email.text.clear()
                password.text.clear()
                val intent = Intent(this, AuthActivity:: class.java)
                startActivity(intent)
            }
        }
    }
}