package com.xvantage.rental.ui.addProperty.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xvantage.rental.R

/**
 * A simple [androidx.fragment.app.Fragment] subclass.
 * Use the [FinancialsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FinancialsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_financials, container, false)
    }

    companion object {
        fun newInstance(propertyId: String): RoomsFragment {
            val fragment = RoomsFragment()
            val args = Bundle()
            args.putString("property_id", propertyId)
            fragment.arguments = args
            return fragment
        }
    }

}