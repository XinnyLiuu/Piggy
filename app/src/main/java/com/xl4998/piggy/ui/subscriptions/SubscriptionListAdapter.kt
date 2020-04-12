package com.xl4998.piggy.ui.subscriptions

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
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
    private var subList: MutableList<Subscription>,
    private val recyclerView: RecyclerView
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Reference to each item to be generated in RecyclerView
    internal class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.sub_name)
        var cost: TextView = view.findViewById(R.id.sub_cost)
        var subbedDate: TextView = view.findViewById(R.id.sub_date)
        var dueDate: TextView = view.findViewById(R.id.sub_due)
        val removeBtn: Button = view.findViewById(R.id.sub_remove)
        val editBtn: Button = view.findViewById(R.id.sub_edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // Prepare card to be returned on RecyclerView creation
        val card = LayoutInflater.from(parent.context)
            .inflate(R.layout.subscription_detail, parent, false) as MaterialCardView

        return DetailViewHolder(card)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Setup subscription detail view
        val subscription = subList[position]
        val viewHolder = holder as DetailViewHolder
        viewHolder.name.text = subscription.name
        viewHolder.cost.text = "Cost: %.2f".format(subscription.cost)

        // Fix date to MM/dd/yyyy
        var times = subscription.dateSubscribed.split("-")
        viewHolder.subbedDate.text = "Date Subscribed: %s/%s/%s".format(
            times[1],
            times[2],
            times[0]
        )
        times = subscription.nextPaymentDate.split("-")
        viewHolder.dueDate.text = "Next Payment: %s/%s/%s".format(
            times[1],
            times[2],
            times[0]
        )

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
        runLayoutAnimation(recyclerView)
    }

    /**
     * Notify RecyclerView about data change and animate
     */
    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context: Context = recyclerView.context
        val controller: LayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)

        recyclerView.layoutAnimation = controller
        recyclerView.adapter!!.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }
}