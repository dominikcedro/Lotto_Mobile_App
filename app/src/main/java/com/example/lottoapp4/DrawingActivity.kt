package com.example.lottoapp4

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.util.Random
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.lottoapp4.Firestore.Games
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DrawingActivity : AppCompatActivity() {
    val db3 = Firebase.firestore
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawing)


        val numbersGeneratedButton = findViewById<TextView>(R.id.generateNumbersButton)
        val number_good = ContextCompat.getColor(this, R.color.victory_green)
        val number_bad = ContextCompat.getColor(this, R.color.no_win_yellow)
        val winmessage = findViewById<TextView>(R.id.winmessage)

        //get selNum from firestore
        val email =  FirebaseAuth.getInstance().currentUser?.email.toString()
        db3.collection("usersGames").document("currentGame")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val selectedNumbers = document.get("selNumb") as List<Int>
                }
        //it's a function that generated a set of 6 random numbers with no repetitions
        fun generateRandomNumbers(): MutableSet<Int> {
            val random = Random()
            val numbers = mutableSetOf<Int>()

            while (numbers.size < 6) {
                val randomNumber = random.nextInt(6) + 1
                numbers.add(randomNumber)}
            return numbers
        }
        fun getButtonID(index:Int):Int{
            val buttonIDs = arrayOf(
                R.id.randomnumber1, R.id.randomnumber2,
                R.id.randomnumber3, R.id.randomnumber4,
                R.id.randomnumber5, R.id.randomnumber6
            )
            return buttonIDs[index]
        }
        fun updateButtonLabels(randomNumbers: Set<Int>) {
            // Update each button with a random number
            val buttonIds = arrayOf(
                R.id.randomnumber1, R.id.randomnumber2,
                R.id.randomnumber3, R.id.randomnumber4,
                R.id.randomnumber5, R.id.randomnumber6
            )
            for ((index, buttonId) in buttonIds.withIndex()) {
                val button = findViewById<Button>(buttonId)
                button.text = "${randomNumbers.elementAt(index)}"
            }
        }
        fun checkNumbersChangeButtons(RandomNumbers:MutableSet<Int>, selectedNumbers:MutableSet<Int>){
            val buttonIds = arrayOf(
                R.id.randomnumber1, R.id.randomnumber2,
                R.id.randomnumber3, R.id.randomnumber4,
                R.id.randomnumber5, R.id.randomnumber6
            )
            for ((index, RandomNumber) in RandomNumbers.withIndex()){
                if (selectedNumbers.contains(RandomNumber)){

                    val button = findViewById<Button>(getButtonID(index))
                    button.setBackgroundColor(number_good)
                }else{
                    val button = findViewById<Button>(getButtonID(index))
                    button.setBackgroundColor(number_bad)
                }
            }
        }
        fun showButtons() {
            for (number in 0 until 6) {
                val button = findViewById<Button>(getButtonID(number))
                Handler(Looper.getMainLooper()).postDelayed({
                    button.visibility = View.VISIBLE
                }, (number + 1) * 1000L)
            }
            Handler(Looper.getMainLooper()).postDelayed({
                numbersGeneratedButton.isEnabled = true
            }, (7) * 1000L)
        }
        fun hideButtons() {
            for (number in 0 until 6) {
                val button = findViewById<Button>(getButtonID(number))
                button.visibility = View.INVISIBLE
            }
        }
        fun checkWin(RandomNumbers:MutableSet<Int>, recievedNumbers:MutableSet<Int>){
            val commonNumbers = RandomNumbers.intersect(recievedNumbers)
            winmessage.visibility = View.VISIBLE
            if (commonNumbers.size < 3){
                winmessage.text = "You had less then 3 common numbers! Maybe next time!"
            }else if (commonNumbers.size == 3){
                winmessage.text = "You had 3 common numbers! You fig with poppy seeds!"
            } else if (commonNumbers.size == 4){
                winmessage.text = "You had 4 common numbers! You win a broken penny!"
            }else if (commonNumbers.size == 5){
                winmessage.text = "You had 5 common numbers! Cha-ching!"
            }else if (commonNumbers.size == 6){
                winmessage.text = "You had 6 common numbers! One way ticket to rich-ville!"

            }}


        numbersGeneratedButton.setOnClickListener {
            val selectedNumbers = document.get("selNumb") as MutableSet<Int>
            val RandomNumbers = generateRandomNumbers()
            numbersGeneratedButton.isEnabled = false
            hideButtons()
            updateButtonLabels(RandomNumbers)
            if (selectedNumbers != null) {
                checkNumbersChangeButtons(RandomNumbers, selectedNumbers)

            }
            showButtons()
            if (selectedNumbers != null) {
                checkWin(RandomNumbers, selectedNumbers)
            }

            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            val email =  FirebaseAuth.getInstance().currentUser?.email.toString()
            val randomNumbers =RandomNumbers.toList()
            // create formatedd time
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            val formatted = current.format(formatter)
            val selectedNumbersEdit = selectedNumbers.toList()
            val game = Games(userId,formatted, email, selectedNumbersEdit,randomNumbers, 0.0 )

            userId?.let {
                db3.collection("usersGames")
                    .document()
                    .set(game)
                    .addOnSuccessListener {
                        winmessage.text = "Your numbers are saved!"
                    }

            }
        }

    }
}}