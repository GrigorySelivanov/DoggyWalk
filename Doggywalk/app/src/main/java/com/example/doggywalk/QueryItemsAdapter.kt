package com.example.doggywalk

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doggywalk.Models.QueryItem

class QueryItemsAdapter(var items: List<QueryItem>) : RecyclerView.Adapter<QueryItemsAdapter.MyViewHolder>()
{
    class  MyViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val image: ImageView = view.findViewById(R.id.query_list_image)
        val title: TextView = view.findViewById(R.id.query_list_title)
        val date: TextView = view.findViewById(R.id.query_list_time)
        val desc: TextView = view.findViewById(R.id.query_list_desc)
        val button: Button = view.findViewById(R.id.query_list_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.query_item_in_list, parent, false)
        return  MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  items.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = items[position].title
        holder.desc.text = items[position].desc
        holder.date.text = items[position].date
        val f = items[position].image
        /*val imageId =
            holder.itemView.context.resources.getIdentifier(
            items[position].image,
            "drawable",
            holder.itemView.context.packageName
        )*/
        loadImageFromUri(holder, f.toUri())
        holder.button.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Уведомление пользователю отправлено!", Toast.LENGTH_LONG).show()
        }
        //holder.image.setImageResource(imageId)
    }

    private fun loadImageFromUri(holder: MyViewHolder, imageUri: Uri?) {
        imageUri?.let { uri ->
            Glide.with(holder.itemView)
                .load(uri)
                .into(holder.image)
        }
    }
}