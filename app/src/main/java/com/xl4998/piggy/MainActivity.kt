package com.xl4998.piggy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.xl4998.piggy.utils.notifications.NotificationHelper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup navigation
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_dashboard, R.id.navigation_expenses, R.id.navigation_subscriptions)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Setup notification channel
        val notificationHelper =
            NotificationHelper()
        notificationHelper.createNotificationChannel(
            this,
            NotificationManagerCompat.IMPORTANCE_HIGH,
            false,
            "Subscriptions",
            "Subscription Notifications"
        )
    }
}
