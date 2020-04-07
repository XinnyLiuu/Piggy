package com.xl4998.piggy.ui.subscriptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.xl4998.piggy.R
import com.xl4998.piggy.data.db.SubscriptionRepository

class SubscriptionsFragment : Fragment() {
    private var viewModel: SubscriptionsViewModel? = null
    private var subscriptionRepository: SubscriptionRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Prepare repository
        subscriptionRepository = SubscriptionRepository(activity!!.application)

        // Link ViewModel
        viewModel = SubscriptionsViewModel(subscriptionRepository!!)

        // Setup observers
        viewModel!!.liveAllSubs.observe(this, Observer {
            // TODO:
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_subscriptions, container, false)
    }
}