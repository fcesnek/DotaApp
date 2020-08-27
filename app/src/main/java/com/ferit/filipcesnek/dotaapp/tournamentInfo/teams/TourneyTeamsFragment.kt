package com.ferit.filipcesnek.dotaapp.tournamentInfo.teams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferit.filipcesnek.dotaapp.R
import com.ferit.filipcesnek.dotaapp.TournamentInfoViewModel
import com.ferit.filipcesnek.dotaapp.tournamentInfo.TourneyInfo
import com.ferit.filipcesnek.dotaapp.tournamentInfo.standings.TourneyStandingsAdapter
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_tourney_standings.*
import kotlinx.android.synthetic.main.fragment_tourney_teams.*

class TourneyTeamsFragment : Fragment() {
    private val viewModel by activityViewModels<TournamentInfoViewModel>()
    private lateinit var tournamentInfo: TourneyInfo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tourney_teams, container, false)
        observeTournamentData()
        return root
    }

    private fun observeTournamentData() {
        viewModel.mutableTournamentData.observe(viewLifecycleOwner, Observer { newValue->
            this.tournamentInfo = newValue.tourneyInfo
            recyclerViewTournamentTeams.adapter = TourneyTeamsAdapter()
            recyclerViewTournamentTeams.layoutManager = LinearLayoutManager(
                activity,
                RecyclerView.VERTICAL,
                false
            )
            recyclerViewTournamentTeams.addItemDecoration(
                DividerItemDecoration(activity, RecyclerView.VERTICAL)
            )
            recyclerViewTournamentTeams.itemAnimator = DefaultItemAnimator()
            (recyclerViewTournamentTeams.adapter as TourneyTeamsAdapter).refreshData(tournamentInfo)
        })
    }
}