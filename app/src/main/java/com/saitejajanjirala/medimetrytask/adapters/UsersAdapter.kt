package com.saitejajanjirala.medimetrytask.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saitejajanjirala.medimetrytask.ChatsActivity
import com.saitejajanjirala.medimetrytask.R
import com.saitejajanjirala.medimetrytask.models.Users
import com.squareup.picasso.Picasso

class UsersAdapter(val context: Context, val arraylist:ArrayList<Users>) :RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {
    class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userimage:ImageView=itemView.findViewById(R.id.userimage)
        val username:TextView=itemView.findViewById(R.id.username)
        val userrelative:RelativeLayout=itemView.findViewById(R.id.userrelative)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.userslist,parent,false)
        return UsersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arraylist.size
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val obj=arraylist[position]
        Picasso.get().load(obj.image).error(R.drawable.ic_person).into(holder.userimage)
        holder.username.text=obj.name
        holder.userrelative.setOnClickListener {
            val intent=Intent(context.applicationContext,ChatsActivity::class.java)
            intent.putExtra("uid",obj.id)
            intent.putExtra("image",obj.image)
            intent.putExtra("name",obj.name)
            context.startActivity(intent)
        }
    }
}