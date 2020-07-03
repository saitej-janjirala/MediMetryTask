package com.saitejajanjirala.medimetrytask

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.saitejajanjirala.medimetrytask.adapters.ChatsAdapter
import com.saitejajanjirala.medimetrytask.models.Chats
import com.saitejajanjirala.medimetrytask.utils.Connectivity
import com.saitejajanjirala.medimetrytask.utils.Timeforchat
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chats.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChatsActivity : AppCompatActivity() {
    private lateinit var arraylist:ArrayList<Chats>
    private lateinit var layoutmanager:LinearLayoutManager
    private lateinit var chatsadpater:ChatsAdapter
    private var uid:String=""
    private var name:String=""
    private var imageurl:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)
        setSupportActionBar(toolbar)
        arraylist= ArrayList()
        supportActionBar!!.title=""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        uid= intent.getStringExtra("uid").toString()
        name=intent.getStringExtra("name").toString()
        imageurl=intent.getStringExtra("image").toString()
        chatusername.text=name
        Picasso.get().load(imageurl).error(R.drawable.ic_person).into(chatuserimage)
        retrievechats()
        send.setOnClickListener {
            sendmessage()
            val imm: InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view: View? =currentFocus
            if(view!=null){
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                super.onBackPressed()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun retrievechats(){
        progresschatslayout.visibility=View.GONE
        val queue=Volley.newRequestQueue(this@ChatsActivity)
        val url="https://assignment.medimetry.in/api/v1/users/$uid/chats"
        val request=object:JsonObjectRequest(Request.Method.GET,url,null,
        Response.Listener<JSONObject>{
            val success=it.getInt("success")
            if(success==1) {
                progresschatslayout.visibility = View.GONE
                arraylist.clear()
                val jsonArray: JSONArray = it.getJSONArray("chats")
                for(i in 0 until jsonArray.length()){
                    val obj=jsonArray.getJSONObject(i)
                    val user= Chats(obj.getString("id"),
                        obj.getString("user_id"),
                        obj.getString("message"),
                        obj.getString("deleted_at"),
                        obj.getString("created_at"),
                        obj.getString("updated_at"),
                        "sent"
                    )
                    arraylist.add(user)
                }
                layoutmanager= LinearLayoutManager(this@ChatsActivity)
                layoutmanager.stackFromEnd=true
                chatsadpater= ChatsAdapter(this@ChatsActivity,arraylist)
                chatsrecyclerview.layoutManager=layoutmanager
                chatsrecyclerview.adapter=chatsadpater
            }
            else{
                progresschatslayout.visibility=View.GONE
                Snackbar.make(topchats, it.getString("message"), Snackbar.LENGTH_LONG)
                    .setAction("Retry"){
                        retrievechats()
                    }
            }
        },Response.ErrorListener {
                progresschatslayout.visibility=View.GONE
                Snackbar.make(topchats, it.message.toString(), Snackbar.LENGTH_LONG)
                    .setAction("Retry"){
                        retrievechats()
                    }
            }){}
        queue.add(request)
    }
    fun sendmessage(){
        val obj=Connectivity(this@ChatsActivity)
        if(obj.checkconnectivity()) {
            val msg = message.text.toString()
            if (msg.isEmpty() || msg.isBlank()) {
               Toast.makeText(this@ChatsActivity,"can't send empty message",Toast.LENGTH_SHORT).show()
            } else {
                    message.text=null
                    val ob=arraylist[arraylist.size-1]
                    val timestamp=Timeforchat.gettimestamp()
                    val chats=Chats((Integer.parseInt(ob.id!!)+1).toString(),ob.user_id,msg,
                        null,timestamp,timestamp,"not")
                    arraylist.add(chats)
                    val pos:Int=arraylist.size-1
                    chatsadpater.notifyDataSetChanged()
                chatsrecyclerview.scrollToPosition(pos)
                    val js=JSONObject()
                    js.put("id",uid)
                    js.put("message",msg)
                    val queue=Volley.newRequestQueue(this@ChatsActivity)
                    val url="https://assignment.medimetry.in/api/v1/users/chat"
                    val request2=object:JsonObjectRequest(Request.Method.POST,url,js,
                    Response.Listener<JSONObject>{
                        val success=it.getInt("success")
                        if(success==1){
                            chats.image="sent"
                            arraylist[arraylist.size-1] = chats
                            chatsadpater.notifyDataSetChanged()
                            retrievechats()
                        }
                    },Response.ErrorListener {
                            Toast.makeText(this@ChatsActivity,it.message
                                .toString(),Toast.LENGTH_LONG).show()
                        }){}
                        queue.add(request2)
                    }
            }
        else{
            obj.showdialog()
        }
    }
}