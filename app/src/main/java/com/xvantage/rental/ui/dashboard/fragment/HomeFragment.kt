package com.xvantage.rental.ui.dashboard.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.xvantage.rental.data.source.sample.PropertyDataRepository
import com.xvantage.rental.databinding.FragmentHomeBinding
import com.xvantage.rental.ui.dashboard.DashboardActivity
import com.xvantage.rental.ui.dashboard.fragment.adapter.PropertiesAdapter
import com.xvantage.rental.ui.dashboard.fragment.adapter.TenantsAdapter
import com.xvantage.rental.ui.takeRent.activity.TakeRentActivity
import com.xvantage.rental.utils.AppPreference
import com.xvantage.rental.utils.CommonFunction
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var appPreference: AppPreference
    private lateinit var propertiesAdapter: PropertiesAdapter
    private lateinit var tenantsAdapter: TenantsAdapter
    private lateinit var dashboardActivity: DashboardActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dashboardActivity = context as DashboardActivity
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        setupClickListeners()
        setupRecyclerViews()
    }

    private fun initializeViews() {
        appPreference = AppPreference(requireContext())
    }

    private fun setupClickListeners() {
        binding.cvTakeRent.root.setOnClickListener {
            navigateToTakeRent()
        }
    }

    private fun navigateToTakeRent() {
        CommonFunction().navigation(requireContext(), TakeRentActivity::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRecyclerViews() {
        setupPropertiesRecyclerView()
        setupTenantsRecyclerView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupPropertiesRecyclerView() {
        propertiesAdapter = PropertiesAdapter(requireContext()).apply {
            addItems(PropertyDataRepository.getProperties())
        }

        binding.horizontalRecyclerView1.apply {
            adapter = propertiesAdapter
            layoutManager = createHorizontalLayoutManager()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupTenantsRecyclerView() {
        tenantsAdapter = TenantsAdapter(requireContext()).apply {
            addItems(PropertyDataRepository.getAllTenants())
        }

        binding.horizontalRecyclerView2.apply {
            adapter = tenantsAdapter
            layoutManager = createHorizontalLayoutManager()
        }
    }

    private fun createHorizontalLayoutManager() = LinearLayoutManager(
        requireContext(),
        LinearLayoutManager.HORIZONTAL,
        false
    )

    companion object {
        const val SERVER_KEY = "PushNotificationServerKey"
    }
}