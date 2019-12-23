package com.example.moviefinderproject.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.moviefinderproject.Model.User
import com.example.moviefinderproject.Model.userName
import com.example.moviefinderproject.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.android.synthetic.main.login_activity.btn_register
import kotlinx.android.synthetic.main.login_activity.edt_pass
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("Users")

        btn_login.setOnClickListener {
            if (TextUtils.isEmpty(edt_username.text) || TextUtils.isEmpty(edt_pass.text)) {
                Toast.makeText(this, "Please fill all fields above.", Toast.LENGTH_SHORT).show()
            } else {
                //val hashEmail = toHash(edt_email.text.toString())
                val username = edt_username.text.toString()
                val hashPassword = toHash(edt_pass.text.toString())
                val ref = myRef!!.child(username)

                ref.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(databaseError: DatabaseError) {
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot){
                        val u : User? = dataSnapshot.getValue(
                            User::class.java)
                        if(u != null){
                            if(hashPassword == u.password){
                                userName = username
                                val intent = Intent(this@LoginActivity,
                                    MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                edt_pass.error = "Incorrect password."
                            }
                        } else {
                            edt_username.error = "Invalid username."
                        }
                    }
                })
            }
        }



        btn_register.setOnClickListener {
            val intent = Intent(this@LoginActivity,
                RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun byteArrayToHexString(array: Array<Byte>): String {

        var result = StringBuilder(array.size * 2)

        for (byte in array) {

            val toAppend =
                String.format("%2X", byte).replace(" ", "0") // hexadecimal
            result.append(toAppend).append("-")
        }
        result.setLength(result.length - 1) // remove last '-'

        return result.toString()
    }

    private fun toHash(text: String): String {
        var result = ""

        val md5 = MessageDigest.getInstance("MD5")
        val md5HashBytes = md5.digest(text.toByteArray()).toTypedArray()
        result = byteArrayToHexString(md5HashBytes)
        return result
    }
}

