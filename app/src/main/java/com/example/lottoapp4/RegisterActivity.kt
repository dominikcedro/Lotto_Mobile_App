package com.example.lottoapp4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity(){

    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val btn_register = findViewById<Button>(R.id.registerUserButton)
        val et_register_email = findViewById<EditText>(R.id.RegEmailText)
        val et_register_password = findViewById<EditText>(R.id.RegPassText)
        val et_register_name = findViewById<EditText>(R.id.nameEditText)

        btn_register.setOnClickListener{
            when{
                TextUtils.isEmpty(et_register_email.text.toString().trim{it <= ' '}) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(et_register_password.text.toString().trim{it <= ' '}) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                !isPasswordValid(et_register_password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Password must be at least 6 characters long and contain a special character.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val name: String = et_register_name.text.toString().trim{it <= ' '}
                    val email: String = et_register_email.text.toString().trim{it <= ' '}
                    val password: String = et_register_password.text.toString().trim{it <= ' '}
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                                if (task.isSuccessful) {
                                    val firebaseUser: FirebaseUser = task.result!!.user!!
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "You are registered successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    // create collection users in firestore, add name, mail and password
                                    val user = hashMapOf(
                                        "name" to name,
                                        "email" to email,
                                        "password" to password
                                    )
                                    db.collection("users").document(FirebaseAuth.getInstance().currentUser!!.email.toString()).set(user)


                                    val intent =
                                        Intent(this@RegisterActivity, LoginActivity::class.java)
                                   startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
                }
            }
        }
    }
    private fun isPasswordValid(password: String): Boolean {
        // Check if password is at least 6 characters long
        if (password.length < 6) {
            return false
        }
        val specialCharacterRegex = Pattern.compile("[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+")
        return specialCharacterRegex.matcher(password).find()
}
}
