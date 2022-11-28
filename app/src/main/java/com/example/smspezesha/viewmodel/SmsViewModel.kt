package com.example.smspezesha.viewmodel

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smspezesha.data.SmsBox
import java.text.SimpleDateFormat
import java.util.*

class SmsViewModel(): ViewModel() {
    
     val _smslivedata:MutableLiveData<List<SmsBox>>

    var smsList:MutableList<SmsBox>
    
    init {
         _smslivedata = MutableLiveData<List<SmsBox>>()



        smsList = mutableListOf()
    }

    fun smsCallApi( contentResolver:ContentResolver){
            val cursor: Cursor =
                contentResolver.query(Uri.parse("content://sms"), null, null, null, null, null)!!
            var totalSmsCounts = cursor.count
            var i:Int =0;
            if(cursor.moveToFirst()){
                while (i < totalSmsCounts){

                    var id:String = cursor.getString(cursor.getColumnIndexOrThrow("_id"))
                    var address:String = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                    var body:String = cursor.getString(cursor.getColumnIndexOrThrow("body"))
                    var read:String = cursor.getString(cursor.getColumnIndexOrThrow("read"))
                    var date:String = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                    var type = if(cursor.getString(cursor.getColumnIndexOrThrow("type")).contains("1")){

                        "inbox"
                    }else{

                        "sent"
                    }

                    if(address.equals("Mpesa", true)) {
                        val smsBox: SmsBox = SmsBox(address, id, read, getDateTime(date), body, type)

                        smsList.add(smsBox)
 smsBox
                    }

                    cursor.moveToNext()

                    i++
                }
            }

            cursor.close()

            _smslivedata.postValue(smsList)

            Log.i("logchar", "read_sms: "+ smsList)

    }

    fun getSmsList(): LiveData<List<SmsBox>>{
        return _smslivedata as LiveData<List<SmsBox>>
    }

    fun getDateTime(s:String):String{

        try{
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val netDate = java.sql.Date(s.toLong() * 1000)

            return sdf.format(netDate)
        }catch (e:java.lang.Exception){
            return "Not recognized Date Format"
        }
    }
}