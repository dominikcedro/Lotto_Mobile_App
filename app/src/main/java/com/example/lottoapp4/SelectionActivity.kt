package com.example.lottoapp4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.firestore

class SelectionActivity : AppCompatActivity() {
    val db1 = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)
        val intent = intent


        //this here is a greeting for user, it uses the "name" from before
        val welcomeText = findViewById<TextView>(R.id.selectNumbersText)
        welcomeText.text = " please select your lucky numbers"

        val numbersText = findViewById<TextView>(R.id.selectedNumbersView)

        //this here defines numberPicker that I found in the internet
        val numbersPicker = findViewById<NumberPicker>(R.id.numbersPicker)
        numbersPicker.maxValue = 6
        numbersPicker.minValue=1

        //defining the buttons on this screen
        //getRichButton is disabled in instant, so the user
        //cannot
        val selectButton = findViewById<Button>(R.id.selectButton)
        val getRichButton = findViewById<Button>(R.id.getRichButton)
        getRichButton.isEnabled = false


        //here array is defined, the range for it is 6
        val numbersArray = IntArray(6)
        var text = ""
        val selectedNumbers = mutableSetOf<Int>()


        selectButton.setOnClickListener {
            val selectedNumber = numbersPicker.value
            //I used if/else statement to check if the number is contained in set of INTs
            if (selectedNumbers.contains(selectedNumber)) {
                //if it's already contained nothing happens, I think that's simpler
                welcomeText.text = "Hey , you can't pick this number again!"
            } else {
                welcomeText.text = "Good choice!"
                //if it's not contained in the set the number will be added to set
                selectedNumbers.add(selectedNumber)
                //and transformed to string with blank space on side
                text += selectedNumber.toString() + " "
                //after that it will be added to "numbersText" textView
                numbersText.text = text

                //if we add 6 numbers button select will be disabled
                // and button getRich will be enabled
                if (selectedNumbers.size == numbersArray.size) {
                    selectButton.isEnabled = false
                    getRichButton.isEnabled = true
                    val id = FirebaseAuth.getInstance().currentUser!!.uid
                    val email = FirebaseAuth.getInstance().currentUser!!.email.toString()
//                    val numbers = hashMapOf(
//                        "user_id" to id,
//                        "email" to email,
//                        "selected_numbers" to selectedNumbers,
//                        "random_numbers" to mutableSetOf<Int>()
//                    )
//                    db1.collection("usersNumbers")
//                        .document(FirebaseAuth.getInstance().currentUser!!.email.toString())
//                        .set(numbers)
                }
            }
        }
        //if the user clicks on getRichButton next activity will be started
        //also selected numbers will be transfered to NumbDrawingActivity as a INTArray
        getRichButton.setOnClickListener{

            val intent4 = Intent(this, DrawingActivity::class.java)

            startActivity(intent4)
        }
    }
}

