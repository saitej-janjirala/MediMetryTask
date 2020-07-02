package com.saitejajanjirala.medimetrytask.utils
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class Connectivity(val context: Context) {
    fun checkconnectivity():Boolean{
        val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activenetworkinfo: NetworkInfo?= connectivityManager.activeNetworkInfo
        if(activenetworkinfo?.isConnected!=null){
            return activenetworkinfo.isConnected
        }
        else{
            return false
        }
    }
    fun showdialog(){
        val dialog = AlertDialog.Builder(context)
            .setCancelable(false)
            .setTitle("Network Error")
            .setMessage("No Internet Connection")
            .setPositiveButton("Retry", null)
            .show()
        val positiveButton =
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if(checkconnectivity()){
                dialog.dismiss()
            }
            else{
                dialog.dismiss()
                showdialog()
            }
        }
    }
}