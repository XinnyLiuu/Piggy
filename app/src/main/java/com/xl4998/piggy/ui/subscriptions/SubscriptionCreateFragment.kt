package com.xl4998.piggy.ui.subscriptions

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment

import com.xl4998.piggy.R

/**
 * A full screen dialog that will assist in creating a new subscription
 *
 * Code taken from https://medium.com/alexander-schaefer/implementing-the-new-material-design-full-screen-dialog-for-android-e9dcc712cb38
 */
class SubscriptionCreateFragment : DialogFragment() {

    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_subscription_create, container, false)

        // Get the toolbar
        toolbar = view.findViewById(R.id.toolbar)

        return view
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup listeners for the toolbar
        toolbar!!.setNavigationOnClickListener { dismiss() }
        toolbar!!.title = "Add a Subscription"
        toolbar!!.inflateMenu(R.menu.fragment_subscription_create_menu)
        toolbar!!.setOnMenuItemClickListener {
            dismiss()
            true
        }
    }

    override fun onStart() {
        super.onStart()

        // Make the dialog appear fullscreen
        val dialog = dialog
        if (dialog != null) dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
}
