package com.example.contacts

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_contact_details.*


class ContactDetailsActivity : AppCompatActivity() {
    private var no = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)

//        obtaining contact info from the adapter class
        val name = intent.getStringExtra("name")
        no = intent.getStringExtra("no").toString()
        val img = intent.getStringExtra("img")?.toInt()


//        setting the info in the views
        if (img != null) {
            imgview.setImageResource(img)
        }
        contactname.text = name
        contactno.text = no

        //copy the phone number on long press
        contactno.setOnLongClickListener {

            val clipboard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("phone number", contactno.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Phone number copied", Toast.LENGTH_SHORT).show()

            return@setOnLongClickListener true
        }


    }

    //call function
    fun call(view: View) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$no")
        startActivity(dialIntent)
    }

    //sms function
    fun sms(view: View) {
        val sendIntent = Intent(Intent.ACTION_SENDTO)
        sendIntent.data = Uri.parse("smsto:" + Uri.encode(no))
        startActivity(sendIntent)
    }
}