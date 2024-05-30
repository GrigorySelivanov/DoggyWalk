package com.example.doggywalk.ui.walk

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.doggywalk.DbContext
import com.example.doggywalk.MainScreenActivity
import com.example.doggywalk.Models.QueryItem
import com.example.doggywalk.Models.User
import com.example.doggywalk.databinding.FragmentWalkBinding
import java.io.File
import java.io.FileOutputStream

@Suppress("UNREACHABLE_CODE")
class WalkFragment : Fragment() {

    private var _binding: FragmentWalkBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val REQUEST_CODE_PICK_IMAGE = 123

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { _binding = FragmentWalkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonPickImage.setOnClickListener {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            val title: EditText = binding.titleGiveFragment
            val desc: EditText = binding.descGiveFragment
            val date: EditText = binding.dateGiveFragment
            val button: Button = binding.newQueryButton
            loadImageFromUri(imageUri)
            //val path = saveImageToFileSystem(imageUri)
            button.setOnClickListener {
                val ttl = title.text.toString()
                val dsc = desc.text.toString()
                val dt = date.text.toString()
                if (ttl == "" || dt == "")
                    Toast.makeText(this.requireContext(), "Заполните все поля", Toast.LENGTH_LONG).show()
                else {
                    val item =  QueryItem(imageUri.toString(), ttl, dsc, dt)
                    val db = DbContext(this.requireContext(), null)
                    db.addQueryItem(item)

                    val intent = Intent(this.context, MainScreenActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun loadImageFromUri(imageUri: Uri?) {
        imageUri?.let { uri ->
            Glide.with(this)
                .load(uri)
                .into(binding.imageView)
        }
    }
    private fun saveImageToFileSystem(imageUri: Uri?) : String {
        imageUri?.let { uri ->
            // Получение имени файла из URI
            val fileName = getFileNameFromUri(uri)

            // Создание каталога для сохранения изображений
            val imagesDir = File(Environment.getExternalStorageDirectory(), "Files")
            if (!imagesDir.exists()) {
                imagesDir.mkdirs()
            }

            // Сохранение изображения в файловую систему
            val imageFile = File(imagesDir, fileName)
            val inputStream = this.requireContext().contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(imageFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.flush()
            outputStream.close()

            return imageFile.absolutePath
        }
        return  ""
    }

    private fun getFileNameFromUri(uri: Uri): String {
        val cursor = this.requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                return it.getString(nameIndex)
            }
        }

        return "image_${System.currentTimeMillis()}.jpg"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}