package com.example.lottoapp4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lottoapp4.RecentGamesRecycler.RecentGameModel
import com.example.lottoapp4.RecentGamesRecycler.RecentGames_RecyclerAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class RecentGamesAcitivity : AppCompatActivity() {
    val db = Firebase.firestore
    private lateinit var recentGamesRecyclerView: RecyclerView
    private lateinit var recentGamesAdapter: RecentGames_RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent_games_acitivity)

        lateinit var recentGamesRecyclerView: RecyclerView
        lateinit var recentGamesAdapter: RecentGames_RecyclerAdapter

        recentGamesRecyclerView = findViewById(R.id.recyclerViewRecent)
        recentGamesRecyclerView.layoutManager = LinearLayoutManager(this)
        recentGamesAdapter = RecentGames_RecyclerAdapter(ArrayList<RecentGameModel>())
        recentGamesRecyclerView.adapter = recentGamesAdapter


        // get all the games from database where user is the current user
        // display the games in a recycler view
        db.collection("games")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.getString("username") == FirebaseAuth.getInstance().currentUser?.email) {
                        val username = document.getString("username")
                        val selectedNumbers = document.getString("selNumb")
                        val randomNumbers = document.getString("drawNumb")
                        val date = document.getString("date")

                    }

    }
}}}