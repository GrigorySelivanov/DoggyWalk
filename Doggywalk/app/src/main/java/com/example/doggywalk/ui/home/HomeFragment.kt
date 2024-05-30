package com.example.doggywalk.ui.home

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doggywalk.DbContext
import com.example.doggywalk.Models.QueryItem
import com.example.doggywalk.Models.User
import com.example.doggywalk.QueryItemsAdapter
import com.example.doggywalk.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val queryItemList: RecyclerView = binding.QueryList
        val db = DbContext(this.requireContext(), null)
        val items =  db.getItems()

        if (items.isNotEmpty()) {
            queryItemList.layoutManager = LinearLayoutManager(this.context)
            queryItemList.adapter = QueryItemsAdapter(items)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}