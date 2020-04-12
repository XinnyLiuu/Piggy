package com.xl4998.piggy.ui.subscriptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xl4998.piggy.R
import com.xl4998.piggy.data.db.SubscriptionRepository
import kotlinx.android.synthetic.main.fragment_subscriptions.*

/**
 * Fragment that displays the list of all the user's subscriptions
 */
class SubscriptionsFragment : Fragment() {

    // ViewModel
    private lateinit var viewModel: SubscriptionsViewModel

    // RecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var rvAdapter: SubscriptionListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Repository
        val subscriptionRepository = SubscriptionRepository(activity!!.application)

        // ViewModel
        viewModel = SubscriptionsViewModel(subscriptionRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_subscriptions, container, false)

        // Prepare RecyclerView
        recyclerView = view.findViewById(R.id.sub_list)
        recyclerView.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        // RecyclerView adapter
        rvAdapter = SubscriptionListAdapter(
            parentFragmentManager,
            viewModel,
            mutableListOf(),
            recyclerView
        )
        recyclerView.adapter = rvAdapter

        // Setup observers
        viewModel.liveAllSubs.observe(viewLifecycleOwner, Observer { subs ->
            rvAdapter.setSubs(subs)
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Prepare dialog fragment
        val createSubDialog = SubscriptionCreateDialogFragment(viewModel)

        // Setup listeners for button
        add_sub_button.setOnClickListener {
            createSubDialog.show(parentFragmentManager, "Create Sub Dialog")
        }
    }
}