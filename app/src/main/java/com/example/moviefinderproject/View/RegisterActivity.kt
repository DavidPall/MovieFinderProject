package com.example.moviefinderproject.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.moviefinderproject.Model.User
import com.example.moviefinderproject.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.register_activity.*
import java.security.MessageDigest

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("Users")

        btn_register.setOnClickListener {
            if (TextUtils.isEmpty(edt_usernameReg.text) || TextUtils.isEmpty(edt_pass.text) || TextUtils.isEmpty(edt_confPass.text)) {
                Toast.makeText(this, "Please fill all fields above.", Toast.LENGTH_SHORT).show()
            } else {
                if(edt_pass.text.toString() != edt_confPass.text.toString()){
                    edt_confPass.setError("Not matching passwords.")
                }else{
                    //val hashEmail = toHash(edt_email.text.toString())
                    val username = edt_usernameReg.text.toString()
                    val hashPassword = toHash(edt_pass.text.toString())
                    val ref = myRef!!.child(username)

                    ref.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val u : User? = dataSnapshot.getValue(
                                User::class.java)
                            if (u != null) {
                                edt_usernameReg.setError("This username is already taken.")
                            } else {
                                val user = User(
                                    username,
                                    hashPassword
                                )
                                myRef!!.child(username).setValue(user).addOnSuccessListener {
                                    Toast.makeText(this@RegisterActivity, "User added!", Toast.LENGTH_SHORT).show()
                                }
                                val intent = Intent(this@RegisterActivity,
                                    LoginActivity::class.java)
                                startActivity(intent)
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                }
            }
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

//val hashEmail = toHash(emailEditText.text.toString())
//val hashPassword = toHash(passwordEditText.text.toString())
//val ref = reference!!.child(hashEmail)
//ref.addListenerForSingleValueEvent(object : ValueEventListener {
//    override fun onDataChange(dataSnapshot: DataSnapshot) {
//        val u: User? = dataSnapshot.getValue(User::class.java)
//        if (u != null) {
//            if (u.password?.equals(hashPassword)!!) {
//                val fragmentTransaction =
//                    fragmentManager!!.beginTransaction()
//                fragmentTransaction.replace(R.id.fragment_frameLayout, MainFragment())
//                fragmentTransaction.commit()
//                bottomNavigationView?.setTransitionVisibility(View.VISIBLE)
//            } else {
//                Toast.makeText(context, "Incorrect email or password", Toast.LENGTH_SHORT).show()
//            }
//        } else {
//            val user = User(hashEmail, hashPassword)
//            reference!!.child(hashEmail).setValue(user).addOnSuccessListener {
//                Toast.makeText(view.context, "User added!", Toast.LENGTH_SHORT).show()
//            }
//            val fragmentTransaction =
//                fragmentManager!!.beginTransaction()
//            fragmentTransaction.replace(R.id.fragment_frameLayout, MainFragment())
//            fragmentTransaction.commit()
//            bottomNavigationView?.setTransitionVisibility(View.VISIBLE)
//        }
//    }
//    override fun onCancelled(databaseError: DatabaseError) {}
//})