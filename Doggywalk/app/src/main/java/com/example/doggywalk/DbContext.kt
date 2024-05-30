package com.example.doggywalk

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.doggywalk.Models.QueryItem
import com.example.doggywalk.Models.User

class DbContext(val context: Context, val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "Db", factory, 1)
{
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE users (id INT PRIMARY KEY, name TEXT, email TEXT, phone TEXT, description TEXT, password TEXT)"
        db!!.execSQL(query)
        val newquery = "CREATE TABLE queryItems (id INT PRIMARY KEY, image TEXT, title TEXT, description TEXT, date TEXT)"
        db!!.execSQL(newquery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users")
        db!!.execSQL("DROP TABLE IF EXISTS queryItems")
        onCreate(db)
    }

    fun addUser(user: User) {
        val newUser = ContentValues()
        newUser.put("name", user.name)
        newUser.put("email", user.email)
        newUser.put("password", user.password)
        newUser.put("description", user.desc)
        newUser.put("phone", user.phone)

        val db = this.writableDatabase
        db.insert("users", null, newUser)
        db.close()
    }

    fun updateUser(user: User) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("name", user.name)
        values.put("email", user.email)
        values.put("password", user.password)
        values.put("description", user.desc)
        values.put("phone", user.phone)

        db.update(
            "users",
            values,
            "email = ?",
            arrayOf(user.email)
        )
        db.close()
    }

    fun addQueryItem(item: QueryItem) {
        val newItem = ContentValues()
        newItem.put("image", item.image)
        newItem.put("title", item.title)
        newItem.put("description", item.desc)
        newItem.put("date", item.date)

        val db = this.writableDatabase
        db.insert("queryItems", null, newItem)
        db.close()
    }

    @SuppressLint("Recycle")
    fun checkUser(login: String, pass: String): Boolean {
        val db = this.readableDatabase

        val result = db.rawQuery("SELECT * FROM users WHERE email = '$login' AND password = '$pass'", null)
        return result.moveToFirst()
    }

    @SuppressLint("Recycle", "Range")
    fun getUser(login: String): User {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM users WHERE email = '$login'", null)
        if (result.moveToFirst()) {
            var user = User(
                result.getInt(result.getColumnIndex("id")),
                result.getString(result.getColumnIndex("name")),
                result.getString(result.getColumnIndex("email")),
                result.getString(result.getColumnIndex("phone")),
                result.getString(result.getColumnIndex("description")),
                result.getString(result.getColumnIndex("password"))
            )
            result.close()
            return user
        }
        return User(0,"","","","","")
    }

    @SuppressLint("Recycle", "Range")
    fun getItems(): List<QueryItem> {
        val db = this.readableDatabase
        val itemList = arrayListOf<QueryItem>()

        val result = db.rawQuery("SELECT * FROM queryItems", null)
        if (result.moveToFirst()) {
            do {
                val queryItem = QueryItem(
                    result.getString(result.getColumnIndex("image")),
                    result.getString(result.getColumnIndex("title")),
                    result.getString(result.getColumnIndex("description")),
                    result.getString(result.getColumnIndex("date"))
                )
                itemList.add(queryItem)
            } while (result.moveToNext())
        }
        result.close()
        return itemList
    }

}
