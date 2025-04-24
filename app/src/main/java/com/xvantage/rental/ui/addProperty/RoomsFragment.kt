package com.xvantage.rental.ui.addProperty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xvantage.rental.R
import com.xvantage.rental.databinding.FragmentRoomsBinding

class RoomsFragment : Fragment() {

    private var _binding: FragmentRoomsBinding? = null
    private val binding get() = _binding!!

    private lateinit var roomAdapter: RoomAdapter
    private var propertyId: String = ""
    private val rooms = mutableListOf<Room>()

    companion object {
        fun newInstance(propertyId: String): RoomsFragment {
            val fragment = RoomsFragment()
            val args = Bundle()
            args.putString("property_id", propertyId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            propertyId = it.getString("property_id", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoomsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadRooms()
    }

    private fun setupRecyclerView() {
        roomAdapter = RoomAdapter { room ->
            // Handle room click (show details, edit, etc.)
            showRoomDetails(room)
        }
        binding.rvRooms.adapter = roomAdapter
    }

    private fun loadRooms() {
        // TODO: Load rooms from database or API based on propertyId
        // For now, show empty state or sample data

        // Sample data for testing
        if (rooms.isEmpty()) {
            // Show empty state
            showEmptyState(true)
        } else {
            // Show room list
            showEmptyState(false)
            roomAdapter.submitList(rooms)
        }
    }

    private fun showEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.emptyState.visibility = View.VISIBLE
            binding.rvRooms.visibility = View.GONE
        } else {
            binding.emptyState.visibility = View.GONE
            binding.rvRooms.visibility = View.VISIBLE
        }
    }

    fun addRoom(room: Room) {
        // Add new room to the list
        rooms.add(room)
        roomAdapter.submitList(rooms.toList())

        // Hide empty state if this is the first room
        if (rooms.size == 1) {
            showEmptyState(false)
        }
    }

    private fun showRoomDetails(room: Room) {
        // TODO: Implement room details dialog or navigation
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}