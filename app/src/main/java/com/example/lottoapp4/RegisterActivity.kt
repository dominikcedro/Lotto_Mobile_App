package com.example.lottoapp4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import android.text.TextUtils

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var inputEmail: EditText? = null
    private var inputName: EditText? = null
    private var inputPassword: EditText? = null
    private var inputRepPass: EditText? = null
    private var registerButton: Button? = null


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        val inputEmail = findViewById<EditText>(R.id.RegEmailText)
        val inputName = findViewById<EditText>(R.id.nameEditText)
        val inputPassword = findViewById<EditText>(R.id.RegPassText)
        val inputPasswordRepeat = findViewById<EditText>(R.id.RegPassText2)
        val registerButton: Button = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener{
            validateRegisterDetails()
            registerUser()

        }

    }
    private fun validateRegisterDetails(): Boolean {
    // if any of input fields are empty show snackbar message "incorrect" and return false if all the fields are filled return true
        if (TextUtils.isEmpty(inputEmail?.text.toString())) {
            Toast.makeText(applicationContext, "Please enter email...", Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(inputName?.text.toString())) {
            Toast.makeText(applicationContext, "Please enter name...", Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(inputPassword?.text.toString())) {
            Toast.makeText(applicationContext, "Please enter password...", Toast.LENGTH_LONG).show()
            return false
        }
        if (TextUtils.isEmpty(inputRepPass?.text.toString())) {
            Toast.makeText(applicationContext, "Please repeat password...", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
    // here create users
    private fun registerUser() {
        if (validateRegisterDetails()) {
            auth.createUserWithEmailAndPassword(
                inputEmail?.text.toString(),
                inputPassword?.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            "Registration successful!",
                            Toast.LENGTH_LONG
                        ).show()
                        val user = auth.currentUser
                        user?.sendEmailVerification()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Verification email sent!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Registration failed! Please try again later",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    fun  userRegistrationSuccess(){

        Toast.makeText(this@RegisterActivity, resources.getString(R.string.register_success),
            Toast.LENGTH_LONG).show()
    }


    }