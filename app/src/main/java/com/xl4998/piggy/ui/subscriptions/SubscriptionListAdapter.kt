package com.xl4998.piggy.ui.subscriptions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.xl4998.piggy.R
import com.xl4998.piggy.data.db.entities.Subscription

/**
 * RecyclerView adapter for items in the SubscriptionsFragment's RecyclerView
 */
class SubscriptionListAdapter (
    private var subList: MutableList<Subscription>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Reference to each item to be generated in RecyclerView
    internal class DetailViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.sub_name)
        var cost: TextView = view.findViewById(R.id.sub_cost)
        var interval: TextView = view.findViewById(R.id.sub_due)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // Prepare card to be returned on RecyclerView creation
        val card = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_subscription_detail, parent, false) as MaterialCardView

        return DetailViewHolder(card)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Setup subscription detail view
        val subscription = subList[position]
        val viewHolder = holder as DetailViewHolder
        viewHolder.name.text = subscription.name
        viewHolder.cost.text = subscription.cost.toString()
        // TODO: due date

    }

    override fun getItemCount(): Int {
        return subList.size
    }

    /**
     * Sets the list of subscription data to be populated in RecyclerView
     */
    fun setSubs(subList: MutableList<Subscription>) {
        this.subList = subList
        notifyDataSetChanged()
    }
}