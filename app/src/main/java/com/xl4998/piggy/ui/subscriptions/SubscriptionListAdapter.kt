package com.xl4998.piggy.ui.subscriptions

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.xl4998.piggy.R
import com.xl4998.piggy.data.db.entities.Subscription

/**
 * RecyclerView adapter for items in the SubscriptionsFragment's RecyclerView
 */
class SubscriptionListAdapter(
    private val parentFragmentManager: FragmentManager,
    private val viewModel: SubscriptionsViewModel,
    private var subList: MutableList<Subscription>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Reference to each item to be generated in RecyclerView
    internal class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.sub_name)
        var cost: TextView = view.findViewById(R.id.sub_cost)
        var date: TextView = view.findViewById(R.id.sub_due)
        val removeBtn: Button = view.findViewById(R.id.sub_remove)
        val editBtn: Button = view.findViewById(R.id.sub_edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // Prepare card to be returned on RecyclerView creation
        val card = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_subscription_detail, parent, false) as MaterialCardView

        return DetailViewHolder(card)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Setup subscription detail view
        val subscription = subList[position]
        val viewHolder = holder as DetailViewHolder
        viewHolder.name.text = subscription.name
        viewHolder.cost.text = "%.2f".format(subscription.cost)
        viewHolder.date.text = String.format(
            "Next Payment - %s month(s) from %s",
            subscription.interval,
            subscription.dateSubscribed
        ) // TODO: Calculate the actual date

        // Prepare buttons listener
        viewHolder.removeBtn.setOnClickListener {
            viewModel.removeSub(subscription)
        }

        viewHolder.editBtn.setOnClickListener {
            // Show dialog fragment
            val dialog = SubscriptionUpdateDialogFragment(viewModel)

            val bundle = Bundle()
            bundle.putString("name", subscription.name)
            bundle.putString("cost", subscription.cost.toString())
            bundle.putString("date", subscription.dateSubscribed)
            bundle.putString("interval", subscription.interval.toString())

            dialog.arguments = bundle
            dialog.show(parentFragmentManager, "Update Subscription Dialog")
        }
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