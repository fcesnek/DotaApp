package com.ferit.filipcesnek.dotaapp.tournaments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.ferit.filipcesnek.dotaapp.R
import com.ferit.filipcesnek.dotaapp.TournamentInfoActivity
import kotlinx.android.synthetic.main.tournament_row.view.*

class TournamentsAdapter: RecyclerView.Adapter<TournamentsAdapter.TournamentsViewHolder>() {
    inner class TournamentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener{
                var position: Int = getAdapterPosition()
                val context = itemView.context
                val tournament = tournaments.get(position)
                val intent = Intent(context, TournamentInfoActivity::class.java)

                intent.putExtra("url",tournament.link)
                itemView.context.startActivity(intent)
            }
        }
        fun bind(result: Tournament) {
            itemView.tourneyName.text = result.name
            itemView.tourneyStatus.text = result.status
            when (result.status) {
                "Ongoing" -> {
                    itemView.tourneyStatus.setBackgroundColor(Color.parseColor("#80c8e6c9"))
                }
                "Upcoming" -> {
                    itemView.tourneyStatus.setBackgroundColor(Color.parseColor("#80bbdefb"))
                }
                "Completed" -> {
                    itemView.tourneyStatus.setBackgroundColor(Color.parseColor("#80fff9c4"))
                }
            }
        }
    }
    val tournaments: MutableList<Tournament> = mutableListOf()
    fun refreshData(newResults: List<Tournament>) {
        tournaments.clear()
        tournaments.addAll(newResults)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tournament_row, parent, false)
        return TournamentsViewHolder(
            view
        )
    }

    override fun getItemCount() = tournaments.size

    override fun onBindViewHolder(holder: TournamentsViewHolder, position: Int) {
        holder.bind(tournaments.get(position))
    }
}