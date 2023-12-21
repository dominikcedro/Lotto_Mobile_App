package com.example.lottoapp4

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.lottoapp4.Firestore.FireStoreClass
import com.example.lottoapp4.Firestore.FirestoreData
import com.example.lottoapp4.Firestore.Games
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SelectionActivity : AppCompatActivity() {
    val db2 = Firebase.firestore
    @RequiresApi(Build.VERSION_CODES.O)
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


                if (selectedNumbers.size == numbersArray.size) {
                    selectButton.isEnabled = false
                    getRichButton.isEnabled = true
                    val userId = FirebaseAuth.getInstance().currentUser!!.uid
                    val email =  FirebaseAuth.getInstance().currentUser?.email.toString()
                    val listOfNumbers =selectedNumbers.toList()
                    var formattedDateTime: String = ""
                    val current = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    formattedDateTime = current.format(formatter)
                    val game = Games(userId,formattedDateTime, email, listOfNumbers, null, 0.0)
                    FireStoreClass().setUsersNumbers(this, game)
                    db2.collection("usersGames")
                        .document("currentGame")
                        .set(game)
                        .addOnSuccessListener {
                            welcomeText.text = "Your numbers are saved!"
                        }
            }
        }

        getRichButton.setOnClickListener{

            val intent4 = Intent(this, DrawingActivity::class.java)
            startActivity(intent4)
        }
    }
}}}

