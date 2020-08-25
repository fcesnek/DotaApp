package com.ferit.filipcesnek.dotaapp.tournamentInfo.standings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferit.filipcesnek.dotaapp.R
import com.ferit.filipcesnek.dotaapp.TournamentInfoViewModel
import com.ferit.filipcesnek.dotaapp.tournamentInfo.TourneyInfo
import kotlinx.android.synthetic.main.fragment_tourney_standings.*

class TourneyStandingsFragment : Fragment() {
    private val viewModel by activityViewModels<TournamentInfoViewModel>()
    private lateinit var tournamentInfo: TourneyInfo
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tourney_standings, container, false)
        observeSomeChangingVar()
        return root
    }
    private fun observeSomeChangingVar() {
        viewModel.someChangingVar.observe(viewLifecycleOwner, Observer { newValue->
            this.tournamentInfo = newValue.tourneyInfo
            recyclerViewTournamentStandings.adapter = TourneyStandingsAdapter()
            recyclerViewTournamentStandings.layoutManager = LinearLayoutManager(
                activity,
                RecyclerView.VERTICAL,
                false
            )
            recyclerViewTournamentStandings.addItemDecoration(
                DividerItemDecoration(activity, RecyclerView.VERTICAL)
            )
            recyclerViewTournamentStandings.itemAnimator = DefaultItemAnimator()
            (recyclerViewTournamentStandings.adapter as TourneyStandingsAdapter).refreshData(tournamentInfo)
        })
    }
}