package com.ferit.filipcesnek.dotaapp.tournamentInfo.standings

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ferit.filipcesnek.dotaapp.R
import com.ferit.filipcesnek.dotaapp.tournamentInfo.Standing
import com.ferit.filipcesnek.dotaapp.tournamentInfo.TourneyInfo
import kotlinx.android.synthetic.main.tournament_standings_row.view.*

class TourneyStandingsAdapter: RecyclerView.Adapter<TourneyStandingsAdapter.TourneyStandingsViewHolder>() {
    inner class TourneyStandingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener{
                val position: Int = adapterPosition
                val context = itemView.context
                val standing = tournamentInfo.standings[position]
                Toast.makeText(context, standing.name, Toast.LENGTH_SHORT).show()
            }
        }
        fun bind(result: Standing) {
            itemView.teamStanding.text = result.position.toString()
            itemView.teamNameStandings.text = result.name

            if(result.position == 1) {
                itemView.setBackgroundColor(Color.parseColor("#FFD700"))
            }
            if(result.position == 2) {
                itemView.setBackgroundColor(Color.parseColor("#C0C0C0"))
            }
            if(result.position == 3) {
                itemView.setBackgroundColor(Color.parseColor("#cd7f32"))
            }
        }
    }
    lateinit var tournamentInfo: TourneyInfo
    fun refreshData(newResults: TourneyInfo) {
        tournamentInfo = newResults
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourneyStandingsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tournament_standings_row, parent, false)
        return TourneyStandingsViewHolder(
            view
        )
    }

    override fun getItemCount() = tournamentInfo.standings.size

    override fun onBindViewHolder(holder: TourneyStandingsViewHolder, position: Int) {
        holder.bind(tournamentInfo.standings[position])
    }
}