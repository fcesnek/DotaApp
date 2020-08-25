package com.ferit.filipcesnek.dotaapp.tournamentInfo.info

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.ferit.filipcesnek.dotaapp.R
import com.ferit.filipcesnek.dotaapp.TournamentInfoViewModel
import com.ferit.filipcesnek.dotaapp.tournamentInfo.TournamentData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_tournament_info.*
import org.jetbrains.anko.doAsync
import java.util.*


class TourneyInfoFragment : Fragment(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private val viewModel by activityViewModels<TournamentInfoViewModel>()
    private lateinit var tourneyName: TextView;
    private lateinit var tourneyBio: TextView;
    private lateinit var prizePoolValue: TextView;
    private lateinit var numOfTeams: TextView;
    private lateinit var organizer: TextView;
    private lateinit var location: TextView;
    private lateinit var tournamentInfo: TournamentData
    lateinit var tourneyInfoProgressBar: ProgressBar
    lateinit var mainScrollView: NestedScrollView
    lateinit var tourneyInfoMainContent: RelativeLayout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_tourney_info, container, false)

        this.tourneyInfoProgressBar = root.findViewById(R.id.tourneyInfoProgressBar)
        this.tourneyName = root.findViewById(R.id.text_tourneyName)
        this.tourneyBio = root.findViewById(R.id.text_tourneyBio)
        this.prizePoolValue = root.findViewById(R.id.value_prizePool)
        this.numOfTeams = root.findViewById(R.id.value_numOfTeams)
        this.organizer = root.findViewById(R.id.value_organizer)
        this.location = root.findViewById(R.id.value_location)
        this.mainScrollView = root.findViewById(R.id.main_scrollview)
        this.tourneyInfoMainContent = root.findViewById(R.id.tourneyInfoMainContent)
        val transparentImageView: ImageView = root.findViewById(R.id.transparent_image)

        tourneyInfoMainContent.visibility = View.GONE
        observeSomeChangingVar()


        transparentImageView.setOnTouchListener(OnTouchListener { v, event ->
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    // Disallow ScrollView to intercept touch events.
                    mainScrollView.requestDisallowInterceptTouchEvent(true)
                    // Disable touch on transparent view
                    false
                }
                MotionEvent.ACTION_UP -> {
                    // Allow ScrollView to intercept touch events.
                    mainScrollView.requestDisallowInterceptTouchEvent(false)
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    mainScrollView.requestDisallowInterceptTouchEvent(true)
                    false
                }
                else -> true
            }
        })

        return root
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!
        val geocoder = Geocoder(this.context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocationName(
                this.tournamentInfo.tourneyInfo.location,
                1
            )
            Log.d("address", addresses.toString())
            if(addresses.size > 0) {
                val address = addresses[0]
                val location = LatLng(address.latitude, address.longitude)
                map.addMarker(MarkerOptions().position(location).title("Marker"))
                map.moveCamera(CameraUpdateFactory.newLatLng(location))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun observeSomeChangingVar() {
        viewModel.someChangingVar.observe(viewLifecycleOwner, Observer { newValue ->
            val mapFragment =
                childFragmentManager.fragments[0] as SupportMapFragment?
            mapFragment!!.getMapAsync(this)
            this.tourneyInfoProgressBar.visibility = View.GONE
            this.tourneyInfoMainContent.visibility = View.VISIBLE
            this.tournamentInfo = newValue
            this.tourneyName.text = newValue.name
            this.tourneyBio.text = newValue.tourneyInfo.bio
            this.prizePoolValue.text = newValue.tourneyInfo.prizePool
            this.organizer.text = newValue.tourneyInfo.organizer
            this.numOfTeams.text = newValue.tourneyInfo.numOfTeams
            this.location.text = newValue.tourneyInfo.location
        })
    }
}