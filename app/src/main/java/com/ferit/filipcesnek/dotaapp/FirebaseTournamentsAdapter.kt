package com.example.recyclerview

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ferit.filipcesnek.dotaapp.TournamentInfoActivity
import com.ferit.filipcesnek.dotaapp.tournaments.FirebaseTournament
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.tournament_row.view.*

class FirebaseTournamentsAdapter(
    val currentUser: FirebaseUser?,
    options: FirebaseRecyclerOptions<FirebaseTournament?>
) : FirebaseRecyclerAdapter<FirebaseTournament, FirebaseTournamentsAdapter.FirebaseTournamentsViewHolder>(options) {
    inner class FirebaseTournamentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var database = FirebaseDatabase.getInstance()
        var firebaseTournamentsRef = database.getReference("tournaments/${currentUser?.uid}")
        var dataSnapshot: Iterable<DataSnapshot> = mutableListOf()
        init {
            itemView.setOnClickListener{
                var position: Int = adapterPosition
                val context = itemView.context
                val tournament = tournaments[position]
                val intent = Intent(context, TournamentInfoActivity::class.java)

                intent.putExtra("url", tournament.tournament.link)
                itemView.context.startActivity(intent)
            }
        }
        fun bind(result: FirebaseTournament, currentUser: FirebaseUser?) {
            itemView.tourneyName.text = result.tournament.name
            itemView.tourneyStatus.text = result.tournament.name
            itemView.add_tournament_btn.setOnClickListener {
                val item = FirebaseTournament(result.tournament)
                val newRef: DatabaseReference = firebaseTournamentsRef.push()
                newRef.setValue(item)
            }

            itemView.remove_tournament_btn.setOnClickListener {
                val refKey = this.dataSnapshot.find {
                        el ->
                    result.tournament.name == el.getValue(FirebaseTournament::class.java)?.tournament!!.name
                }
                firebaseTournamentsRef.child(refKey?.key.toString()).removeValue()
                itemView.add_tournament_btn.visibility = View.VISIBLE
                itemView.remove_tournament_btn.visibility = View.GONE
            }
            val ref = this
            firebaseTournamentsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    ref.dataSnapshot = snapshot.children
                    for (postSnapshot in snapshot.children) {
                        val tournament = postSnapshot.getValue(FirebaseTournament::class.java)
                        if (result.tournament.name == tournament?.tournament!!.name) {
                            itemView.add_tournament_btn.visibility = View.GONE
                            itemView.remove_tournament_btn.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("error", databaseError.message)
                }
            })


            when (result.tournament.status) {
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
            if(currentUser != null) {
                itemView.add_tournament_btn.visibility = View.VISIBLE
            } else {
                itemView.add_tournament_btn.visibility = View.GONE
            }
        }
    }
    val tournaments: MutableList<FirebaseTournament> = mutableListOf()
    fun refreshData(newResults: List<FirebaseTournament>) {
        tournaments.clear()
        tournaments.addAll(newResults)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: FirebaseTournamentsViewHolder,
        position: Int, model: FirebaseTournament
    ) {
        holder.bind(tournaments[position], currentUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirebaseTournamentsAdapter.FirebaseTournamentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            com.ferit.filipcesnek.dotaapp.R.layout.tournament_row,
            parent,
            false
        )
        return FirebaseTournamentsViewHolder(
            view
        )
    }
}