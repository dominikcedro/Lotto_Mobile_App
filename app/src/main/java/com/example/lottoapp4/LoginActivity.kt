package com.example.lottoapp4

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private fun isPasswordValid(password: String): Boolean {
        // Check if password is at least 6 characters long
        if (password.length < 6) {
            return false
        }
        val specialCharacterRegex = Pattern.compile("[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+")
        return specialCharacterRegex.matcher(password).find()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val userEmail = intent.getStringExtra("email_id")
        val password = intent.getStringExtra("password")

        val LogEmailText = findViewById<EditText>(R.id.LogEmailText)
        val LogPassText = findViewById<EditText>(R.id.LogPassText)


        val toRegisterText = findViewById<TextView>(R.id.toRegisterText)
        val fullText = "Not a registered user? Sign up"
        val clickableWord = "Sign up"
        val spannableString = SpannableString(fullText)
        val startIndex = fullText.indexOf(clickableWord)
        // check if password and email are filled



        // if spannable string is clicked move to register activity
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
        spannableString.setSpan(clickableSpan, startIndex, startIndex + clickableWord.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        toRegisterText.text = spannableString
        toRegisterText.movementMethod = LinkMovementMethod.getInstance()

        //if login button is clicked move to register activity
        val loginButton: Button = findViewById<Button>(R.id.loginUserButton)
        loginButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, SelectionActivity::class.java)
            when{
                TextUtils.isEmpty(LogEmailText.text.toString().trim{it <= ' '}) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(LogPassText.text.toString().trim{it <= ' '}) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                !isPasswordValid(LogPassText.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Password must be at least 6 characters long and contain a special character.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val email: String = LogEmailText.text.toString().trim{it <= ' '}
                    val password: String = LogPassText.text.toString().trim{it <= ' '}
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                Toast.makeText(
                                    this@LoginActivity,
                                    "You are logged in successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent =
                                    Intent(this@LoginActivity, SelectionActivity::class.java)
                                startActivity(intent)
                            }
                        }
                }}}}}