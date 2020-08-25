package com.ferit.filipcesnek.dotaapp.matches

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ferit.filipcesnek.dotaapp.R
import com.ferit.filipcesnek.dotaapp.TournamentInfoActivity
import kotlinx.android.synthetic.main.match_row.view.*

class MatchesAdapter: RecyclerView.Adapter<MatchesAdapter.MatchesViewHolder>() {
    inner class MatchesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener{
                val position: Int = adapterPosition
                val context = itemView.context
                val match = matches[position]
                val intent = Intent(context, TournamentInfoActivity::class.java)

                intent.putExtra("url", match.tournament.link)
                itemView.context.startActivity(intent)
            }
        }
        fun bind(result: Match) {
            val score = result.team1score + ":" + result.team2score
            itemView.team1Name.text = result.team1.name
            itemView.team2Name.text = result.team2.name
            itemView.score.text = score
            itemView.matchStatus.text = result.status
            itemView.matchTourneyName.text = result.tournament.name
        }
    }
    val matches: MutableList<Match> = mutableListOf()
    fun refreshData(newResults: List<Match>) {
        matches.clear()
        matches.addAll(newResults)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.match_row, parent, false)
        return MatchesViewHolder(
            view
        )
    }

    override fun getItemCount() = matches.size

    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {
        holder.bind(matches[position])
    }
}