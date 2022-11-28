package com.example.smspezesha

import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony.Sms
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smspezesha.data.SmsAdapter
import com.example.smspezesha.data.SmsBox
import com.example.smspezesha.viewmodel.SmsViewModel
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    lateinit var recyclerView: RecyclerView

    lateinit var smsList:MutableList<SmsBox>

    lateinit var smsViewModel:SmsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)


        smsList = mutableListOf()


        smsViewModel = ViewModelProvider(this).get(SmsViewModel::class.java)

         requestPermissionLauncher= registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){ isGranted:Boolean->
            if(isGranted){

                smsViewModel.smsCallApi(contentResolver)

                smsViewModel.getSmsList().observe(this, Observer { sList->

                    recyclerView.adapter = SmsAdapter(sList as MutableList<SmsBox>)
                })

                Toast.makeText(this, "Request Permission to access SMS was Granted!!!", Toast.LENGTH_LONG).show()


            }else{

                Toast.makeText(this, "Request Permission to access SMS was Denied!!!", Toast.LENGTH_LONG).show()
            }
        }


        onClickRequestPermission()


       if( ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Read sms permission granted", Toast.LENGTH_LONG).show()

            smsViewModel.smsCallApi(contentResolver)

            smsViewModel.getSmsList().observe(this, Observer { sList->

                recyclerView.adapter = SmsAdapter(sList as MutableList<SmsBox>)
            })
        }

    }

    private fun onClickRequestPermission(){
        when{
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_SMS
            ) == PackageManager.PERMISSION_GRANTED-> {
                Toast.makeText(this, "Read sms permission granted", Toast.LENGTH_LONG).show()

            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.READ_SMS
            )->{
                requestPermissionLauncher.launch(android.Manifest.permission.READ_SMS)
            }else->{

                requestPermissionLauncher.launch(
                    android.Manifest.permission.READ_SMS
                )
            }
        }
    }

}