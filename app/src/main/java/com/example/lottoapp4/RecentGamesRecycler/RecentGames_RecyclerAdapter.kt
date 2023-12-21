package com.example.lottoapp4.RecentGamesRecycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lottoapp4.R

class RecentGames_RecyclerAdapter(var dataset: ArrayList<RecentGameModel>):RecyclerView.Adapter<RecentGames_RecyclerAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.tvUsername)
        val selectedNumbers: TextView = view.findViewById(R.id.tvSelectedNumbers)
        val randomNumbers: TextView = view.findViewById(R.id.tvRandomNumbers)
        val date: TextView = view.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_row, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recentGame = dataset[position]
        holder.username.text = recentGame.username
        holder.selectedNumbers.text = recentGame.selectedNumbers
        holder.randomNumbers.text = recentGame.randomNumbers
        holder.date.text = recentGame.date
    }
}