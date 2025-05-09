package com.xvantage.rental.ui.addProperty.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xvantage.rental.databinding.FragmentTenantsBinding
import com.xvantage.rental.ui.addProperty.tempFiles.Room
import com.xvantage.rental.ui.addProperty.adapter.RoomAdapter
import com.xvantage.rental.ui.addProperty.bmsheet.Tenant

class TenantsFragment : Fragment() {

    private var _binding: FragmentTenantsBinding? = null
    private val binding get() = _binding!!
    private val rooms = mutableListOf<Room>()

    private lateinit var roomAdapter: RoomAdapter
    private var propertyId: String = ""

    companion object {
        fun newInstance(propertyId: String): TenantsFragment {
            val fragment = TenantsFragment()
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
        _binding = FragmentTenantsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadRooms()
    }

    private fun setupRecyclerView() {
//        roomAdapter = RoomAdapter { room ->
//            // Handle room click (show details, edit, etc.)
//            showRoomDetails(room)
//        }
//        binding.rvRooms.adapter = roomAdapter
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

    fun addRoom(room: Tenant) {
//        // Add new room to the list
//        rooms.add(room)
//        roomAdapter.submitList(rooms.toList())
//
//        // Hide empty state if this is the first room
//        if (rooms.size == 1) {
//            showEmptyState(false)
//        }
    }

    private fun showRoomDetails(room: Room) {
        // TODO: Implement room details dialog or navigation
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}