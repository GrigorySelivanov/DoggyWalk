package com.example.doggywalk.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.doggywalk.DbContext
import com.example.doggywalk.Models.QueryItem
import com.example.doggywalk.R
import com.example.doggywalk.databinding.FragmentProfileBinding
import com.example.doggywalk.databinding.FragmentWalkBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val name: TextView = binding.profileName
        val email: TextView = binding.profileLogin
        val desc: EditText = binding.descProfile
        val phone: EditText = binding.phoneNumberProfile
        val button: Button = binding.editProfileButton
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val login = sharedPreferences.getString("login", "")
        val db = DbContext(requireContext(), null)

        val user = db.getUser(login.toString())
        name.text = user.name
        email.text = user.email
        desc.setText(user.desc)
        phone.setText(user.phone)

        button.setOnClickListener {
            val p = phone.text.toString()
            val d = desc.text.toString()
            if (p == "" || d == "")
                Toast.makeText(this.requireContext(), "Сначала добавьте данные", Toast.LENGTH_LONG)
                    .show()
            else {
                user.phone = p
                user.desc = d
                Thread {
                    db.updateUser(user)
                }.start()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}