package com.saitejajanjirala.medimetrytask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.saitejajanjirala.medimetrytask.adapters.UsersAdapter
import com.saitejajanjirala.medimetrytask.models.Users
import com.saitejajanjirala.medimetrytask.utils.Connectivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var connectivity:Connectivity
    private lateinit var layoutmanager:LinearLayoutManager
    private lateinit var arraylist:ArrayList<Users>
    private lateinit var adapter:UsersAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.title="Chats"
        arraylist=ArrayList()
        getdata()

    }
    fun getdata(){
        progresslayout.visibility=View.VISIBLE
        connectivity= Connectivity(this@MainActivity)
        if(connectivity.checkconnectivity()){
            val queue=Volley.newRequestQueue(this@MainActivity)
            val url="https://assignment.medimetry.in/api/v1/users/get"
            val jsonRequest=object:JsonObjectRequest(Request.Method.GET,url,null,
                Response.Listener<JSONObject>{
                    val success=it.getInt("success")
                    if(success==1) {
                        progresslayout.visibility=View.GONE
                        arraylist.clear()
                        val jsonArray: JSONArray = it.getJSONArray("users")
                        for(i in 0 until jsonArray.length()){
                            val obj=jsonArray.getJSONObject(i)
                            val user=Users(obj.getString("id"),
                                obj.getString("name"),
                                obj.getString("age"),
                                obj.getString("gender"),
                                obj.getString("image"),
                                obj.getString("created_at"),
                                obj.getString("updated_at")
                            )
                            arraylist.add(user)
                        }
                        layoutmanager= LinearLayoutManager(this@MainActivity)
                        usersrecyclerview.layoutManager=layoutmanager
                        usersrecyclerview.addItemDecoration(DividerItemDecoration
                            (this@MainActivity,layoutmanager.orientation))
                        adapter=UsersAdapter(this@MainActivity,arraylist)
                        usersrecyclerview.adapter=adapter
                    }
                    else{
                        progresslayout.visibility=View.GONE
                        Snackbar.make(topmain, it.getString("message"),Snackbar.LENGTH_LONG)
                            .setAction("Retry"){
                                getdata()
                            }
                    }
            },
            Response.ErrorListener {
                Snackbar.make(topmain, it.message.toString(),Snackbar.LENGTH_LONG)
                    .setAction("Retry"){
                        getdata()
                    }
            }){}
            queue.add(jsonRequest)
        }
        else{
            progresslayout.visibility=View.GONE
            connectivity.showdialog()
        }
    }
}