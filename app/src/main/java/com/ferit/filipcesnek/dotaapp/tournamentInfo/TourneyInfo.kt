package com.ferit.filipcesnek.dotaapp.tournamentInfo

data class TourneyInfo(
    val bio: String,
    val series: List<String>,
    val organizer: String,
    val sponsors: List<String>,
    val location: String,
    val dates: String,
    val numOfTeams: String,
    val format: List<String>,
    val prizePool: String,
    val links: List<String>,
    val standings: List<Standing>,
    val teams: List<Team>,
    val streamLinks: List<String>
)

data class Team (
    val name: String,
    val link: String,
    val image: String
)

data class Standing (
    val name: String,
    val position: Int
)
