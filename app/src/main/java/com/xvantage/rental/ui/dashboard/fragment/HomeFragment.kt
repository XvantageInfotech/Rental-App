package com.xvantage.rental.ui.dashboard.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
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

    private lateinit var layoutBinding: FragmentHomeBinding
    lateinit var appPreference: AppPreference
    lateinit var propertiesAdapter: PropertiesAdapter
    lateinit var tenantsAdapter: TenantsAdapter

    private lateinit var landingActivity: DashboardActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        landingActivity = context as DashboardActivity
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layoutBinding = FragmentHomeBinding.inflate(inflater, container, false)
        intiView()
        onClickEvents()
        return layoutBinding.root
    }

    private fun onClickEvents() {
        layoutBinding.cvTakeRent.root.setOnClickListener {
            CommonFunction().navigation(requireContext(), TakeRentActivity::class.java)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun intiView() {
        appPreference = AppPreference(requireContext())
        val sampleCars = PropertyDataRepository.getProperties()

        propertiesAdapter = PropertiesAdapter(requireContext())

        propertiesAdapter.addItems(sampleCars)

        layoutBinding.horizontalRecyclerView1.apply {
            adapter = propertiesAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        val sampleTEnant = PropertyDataRepository.getAllTenants()

        tenantsAdapter = TenantsAdapter(requireContext())
        tenantsAdapter.addItems(sampleTEnant)

        layoutBinding.horizontalRecyclerView2.apply {
            adapter = tenantsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }


    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
    }

    companion object {
        // Remote Config keys
        const val SERVER_KEY = "PushNotificationServerKey"
    }
}
