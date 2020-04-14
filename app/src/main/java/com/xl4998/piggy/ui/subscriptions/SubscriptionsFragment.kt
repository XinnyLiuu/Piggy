package com.xl4998.piggy.ui.subscriptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
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
            // Check if an exception is caught
            val exceptionFound = subs.any { it.name == "SQLiteConstraintException" }

            if (exceptionFound) {
                Toast.makeText(
                    context,
                    "Subscriptions cannot have repeating names!",
                    Toast.LENGTH_LONG
                ).show()

                // Remove the Exception sub
                val updated = subs.filter { it.name != "SQLiteConstraintException" }.toMutableList()
                rvAdapter.setSubs(updated)
                return@Observer
            }

            rvAdapter.setSubs(subs)
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Prepare dialog fragment
        val createSubDialog = SubscriptionCreateDialogFragment(viewModel)

        add_sub_button.setOnClickListener {
            createSubDialog.show(parentFragmentManager, "Create Sub Dialog")
        }

        // FAB on scroll
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val fab = view.findViewById<ExtendedFloatingActionButton>(R.id.add_sub_button)

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    fab.hide()
                } else {
                    fab.show()
                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }
}