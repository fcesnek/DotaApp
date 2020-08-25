package com.ferit.filipcesnek.dotaapp.tournamentInfo.teams

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ferit.filipcesnek.dotaapp.R
import com.ferit.filipcesnek.dotaapp.tournamentInfo.Team
import com.ferit.filipcesnek.dotaapp.tournamentInfo.TourneyInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.tournament_standings_row.view.*
import kotlinx.android.synthetic.main.tournament_team_row.view.*

class TourneyTeamsAdapter: RecyclerView.Adapter<TourneyTeamsAdapter.TourneyTeamsViewHolder>() {
    inner class TourneyTeamsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener{
                val position: Int = adapterPosition
                val context = itemView.context
                val standing = tournamentInfo.teams[position]
                Toast.makeText(context, standing.name, Toast.LENGTH_SHORT).show()
            }
        }
        fun bind(result: Team) {
            itemView.teamName.text = result.name
            val url = result.image
            Picasso.get()
                .load(url)
                .placeholder(R.mipmap.ic_launcher)
                .error(android.R.drawable.stat_notify_error)
                .into(itemView.teamLogo)
        }
    }
    lateinit var tournamentInfo: TourneyInfo
    fun refreshData(newResults: TourneyInfo) {
        tournamentInfo = newResults
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourneyTeamsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tournament_team_row, parent, false)
        return TourneyTeamsViewHolder(
            view
        )
    }

    override fun getItemCount() = tournamentInfo.teams.size

    override fun onBindViewHolder(holder: TourneyTeamsViewHolder, position: Int) {
        holder.bind(tournamentInfo.teams[position])
    }
}