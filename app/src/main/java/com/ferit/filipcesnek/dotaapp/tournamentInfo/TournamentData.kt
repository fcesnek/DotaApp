package com.ferit.filipcesnek.dotaapp.tournamentInfo

data class TournamentData (
    val image: String,
    val name: String,
    val tourneyInfo: TourneyInfo,
    val teams: List<TeamFromApi>,
    val matches: List<MatchFromApi>
)

data class MatchFromApi (
    val matchId: String,
    val team1: String,
    val team1Id: String,
    val team2: String,
    val team2Id: String,
    val winner: String,
    val winnerId: String,
    val duration: Int,
    val tournamentId: String
)

data class TeamFromApi (
    val id: String,
    val name: String
)
