package com.saitejajanjirala.medimetrytask.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saitejajanjirala.medimetrytask.R
import com.saitejajanjirala.medimetrytask.models.Chats
import com.saitejajanjirala.medimetrytask.utils.Timeforchat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ChatsAdapter(val context:Context,val arraylist:ArrayList<Chats>) :RecyclerView.Adapter<ChatsAdapter.ChatViewHolder>(){
    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message:TextView=itemView.findViewById(R.id.chatmessage)
        val chattime:TextView=itemView.findViewById(R.id.chattime)
        val sentornot: ImageView =itemView.findViewById(R.id.sentimage)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.messagelayout,parent,false)
        return ChatViewHolder(view)
    }
    override fun getItemCount(): Int {
        return arraylist.size
    }
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val obj=arraylist[position]
        holder.message.text=obj.message

        if(obj.image.equals("sent")){
            holder.chattime.text=gettimeelapsed(obj.updated_at!!)
            holder.sentornot.setImageResource(R.drawable.ic_check)
        }
        else{
            holder.sentornot.setImageResource(R.drawable.ic_timer)
        }
    }
    fun gettimeelapsed(timestamp:String):String{
        val current:String=Timeforchat.gettimestamp()
        var retstring=""
        val format = SimpleDateFormat("yyyy-mm-dd HH:mm:ss")
        var d1: Date? = null
        var d2: Date? = null
        try {
            d1 = format.parse(current)
            d2 = format.parse(timestamp)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if(d2!=null && d1!=null){
            val diff: Long =d1.time-d2.time
            val diffSeconds = diff / 1000
            val diffMinutes = diff / (60 * 1000)
            val diffHours = diff / (60 * 60 * 1000)
            val diffdays=diff/(24*60*60*1000)
            val diffyears=diff/(365*60*60*1000)
            if(diffyears!=0.toLong()){
                retstring = if(diffyears==1.toLong()){
                    "$diffyears Year ago"
                } else {
                    "$diffyears Years ago"
                }
            }
            else if(diffdays!=0.toLong()){
                retstring = if(diffdays==1.toLong()){
                    "$diffdays Day ago"
                } else {
                    "$diffdays Days ago"
                }
            }
            else if(diffHours!=0.toLong()){
                retstring = if(diffHours==1.toLong()){
                    "$diffHours Hour ago"
                } else {
                    "$diffHours Hours ago"
                }
            }
            else if(diffMinutes!=0.toLong()){
                retstring = if(diffMinutes==1.toLong()){
                    "$diffMinutes Minute ago"
                } else {
                    "$diffMinutes Minutes ago"
                }
            }
            else if(diffSeconds!=0.toLong()){
                retstring="<1 Minute"
            }
        }
        return retstring

    }
}