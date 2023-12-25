package com.example.dynamic

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.dynamiclinks.androidParameters
import com.google.firebase.dynamiclinks.iosParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    var shortLink = ""
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val shareButton = findViewById<ImageView>(R.id.shortBtn)

//        val intentFilter = IntentFilter(Intent.ACTION_VIEW)
//        intentFilter.addDataScheme("https")
//        intentFilter.addDataAuthority("google.com",null)
//        registerReceiver(dynamicLinkReceiver,intentFilter)


        Firebase.dynamicLinks.shortLinkAsync {
            link = Uri.parse("https://google.com/?name=vinay&email=viay12@gmail.com&phone= 9135609361")
            domainUriPrefix = "https://mydynamic.page.link"

            androidParameters {  }
            iosParameters("com.google.ios"){ }
        }
            .addOnSuccessListener {
                shortLink = it.shortLink.toString()
            }
            .addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.setType("text/plain")
            intent.putExtra(Intent.EXTRA_SUBJECT,"App Link")
            intent.putExtra(Intent.EXTRA_TEXT,shortLink)
            startActivity(Intent.createChooser(intent,"Dynamic Link App"))
        }
        dynamicLinkReceiver()
    }

//
//    private val dynamicLinkReceiver = object : BroadcastReceiver(){
//
//        override fun onReceive (context: Context, intent: Intent){
//
//        }
//    }

    private fun dynamicLinkReceiver(){
        Firebase.dynamicLinks.getDynamicLink(intent)
            .addOnSuccessListener { pendingDynamicLinkData ->
                var deepLinkUri: Uri?= null
                if (pendingDynamicLinkData != null){
                    deepLinkUri = pendingDynamicLinkData.link
                }
                if (deepLinkUri != null){
                    val name = deepLinkUri.getQueryParameter("name")
                    val email = deepLinkUri.getQueryParameter("email")
                    val phone = deepLinkUri.getQueryParameter("phone")
                    val nameTv = findViewById<TextView>(R.id.nameText)

                    nameTv.text = "Name :- $name \nEmail :- $email \nPhone :- $phone"
                }

            }
            .addOnFailureListener {
                Toast.makeText(this, ""+it.message, Toast.LENGTH_SHORT).show()
            }
    }
}