package com.example.melkysqliteapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    lateinit var edtName: EditText
    lateinit var edtEmail: EditText
    lateinit var edtIdNumber: EditText
    lateinit var btnSave : Button
    lateinit var btnView : Button
    lateinit var btnDelete : Button
    lateinit var db:SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtName = findViewById(R.id.mEdtName)
        edtEmail = findViewById(R.id.mEdtEmail)
        edtIdNumber = findViewById(R.id.mEdtId)
        btnSave = findViewById(R.id.mBtnSave)
        btnDelete =findViewById(R.id.mBtnDelete)
        btnView = findViewById(R.id.mBtnView)

        //Create a database called eMobilisDB it's name is eMobilis and it is a private database when the phone boots up the database is not restored to factory settings(factory null)
        db = openOrCreateDatabase("eMobilisDB", Context.MODE_PRIVATE, null)
        db.execSQL("CREATE TABLE IF NOT EXISTS users(jina VARCHAR,arafa VARCHAR, kitambulisho VARCHAR)")//jina is the column for name,arafa is the email column and so on

        //set onclick listeners to buttons
        btnSave.setOnClickListener {
            //Receive data from User
            var name = edtName.text.toString().trim()
            var email =  edtEmail.text.toString().trim()
            var idNumber = edtIdNumber.text.toString().trim()

            //Check if user is submitting empty fields
            if (name.isEmpty()|| email.isEmpty()||idNumber.isEmpty()) {
                //Display an error message using the defined message function
                message("Empty Fields!!!", "Please fill all inputs")
            }else{
                //Proceed to save
                db.execSQL("Insert Into users Values('"+name+"','"+email+"','"+idNumber+"')")
                clear()
                message("SUCCESS","USER SAVED SUCCESSFULLY")
            }
        }
        btnView.setOnClickListener {
            //Use cursor to select all the records
            var cursor = db.rawQuery("SELECT * FROM users",null )
            //Check if there is any record found
            if (cursor.count==0) {
                message("No RECORDS!!!", "Sorry,no users were found!!")
            }else{
                //use string buffer to append all records to be displayed using a loop
                var buffer = StringBuffer()
                while (cursor.moveToNext()){
                    var retrievedName = cursor.getString(0)
                    var retrievedEmail = cursor.getString(1)
                    var retrievedIdNumber = cursor.getString(2)
                    buffer.append(retrievedName+"\n")
                    buffer.append(retrievedEmail+"\n")
                    buffer.append(retrievedIdNumber+"\n\n")
                }
                message("USERS",buffer.toString())
            }

        }
        btnDelete.setOnClickListener {
            val IdNumber =edtIdNumber.text.toString().trim()
            if(IdNumber.isEmpty()) {
                message("Empty Fields!!!", "Please fill all inputs")
            }else{
                var cursor = db.rawQuery("SELECT * FROM users WHERE kitambulisho = '"+IdNumber+"' ",null )
                if (cursor.count == 0) {
                    message("NO RECORD FOUND!!!", "Sorry, ther's no user with provided ID")
                }else{
                    db.execSQL("DELETE FROM users WHERE kitambulisho = '"+IdNumber+"'")
                }
            }

        }

    }
    fun message (title:String,message:String){
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("Cancel", null )
        alertDialog.create().show()
    }
    fun clear(){
        edtName.setText("")
        edtName.setText("")
        edtIdNumber.setText("")
    }
}