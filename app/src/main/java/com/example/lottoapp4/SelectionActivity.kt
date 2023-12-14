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
    val db2 = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)
        val intent = intent
        // get name from users firestore database
        var name: String? = null
        db2.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.email.toString())
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    name = document.getString("name")
                }

        //this here is a greeting for user, it uses the "name" from before
        val welcomeText = findViewById<TextView>(R.id.selectNumbersText)
        welcomeText.text = "$name please select your lucky numbers"

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
                welcomeText.text = "Hey $name , you can't pick this number again!"
            } else {
                welcomeText.text = "Good choice $name !"
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
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    val email =  FirebaseAuth.getInstance().currentUser?.email.toString()
                    val listOfNumbers =selectedNumbers.toList()
                    val firebaseData = FirestoreData(email, listOfNumbers, null, 0.0)

                    userId?.let {
                        db2.collection("usersNumbers").document(email)
                            .set(firebaseData)
                            .addOnSuccessListener { documentReference ->
                                // Handle success
                                println("Document added with ID: ${email}")
                            }
                            .addOnFailureListener { e ->
                                // Handle failure
                                println("Error adding document: $e")
                            }
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
}}}

