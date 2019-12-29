package com.example.moviefinderproject.View

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.moviefinderproject.Model.User
import com.example.moviefinderproject.Model.userName
import com.example.moviefinderproject.R
import com.google.firebase.database.*
import java.security.MessageDigest

class ChangePassFragment : DialogFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "Change Password Fragment")
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.change_password, container, false)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("Users")

        val oldPassEdt : EditText = view.findViewById(R.id.oldPass_edt)
        val newPassEdt : EditText = view.findViewById(R.id.newPass_edt)
        val newPass2Edt : EditText = view.findViewById(R.id.newPass2_edt)
        val changeBtn : Button = view.findViewById(R.id.changePass_btn)

        changeBtn.setOnClickListener {
            if(TextUtils.isEmpty(oldPassEdt.toString()) || TextUtils.isEmpty(newPassEdt.toString())
                || TextUtils.isEmpty(newPass2Edt.toString())){
                Toast.makeText(context,"Please fill all the fields above.",Toast.LENGTH_SHORT).show()
            } else {
                val oldPassHash = toHash(oldPassEdt.text.toString())
                val ref = myRef!!.child(userName)

                ref.addListenerForSingleValueEvent(object  : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val currentUser : User? = p0.getValue(User::class.java)


                        if(oldPassHash == currentUser!!.password){

                            Log.i("new1: ", newPassEdt.text.toString())
                            Log.i("new2: ", newPass2Edt.text.toString())
                            if(newPassEdt.text.toString() == newPass2Edt.text.toString()){
                                ref.child("password").setValue(toHash(newPassEdt.text.toString()))
                                Toast.makeText(context,"Password changed.",Toast.LENGTH_SHORT).show()
                                dismiss()
                            } else {
                                newPass2Edt.error = "Not matching passwords."
                            }
                        } else {
                            oldPassEdt.error = "Incorrect password."
                        }

                    }


                })


            }



        }



        return view
    }


//    override fun onStart() {
//        super.onStart()
//        if(dialog != null){
//            val width = ViewGroup.LayoutParams.MATCH_PARENT
//            val height = ViewGroup.LayoutParams.MATCH_PARENT
//            dialog!!.window?.setLayout(width,height)
//        }
//    }


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


