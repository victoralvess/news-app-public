package com.github.victoralvess.newz.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.github.victoralvess.newz.R;
import com.github.victoralvess.newz.activities.helpers.InMemoryState;
import com.github.victoralvess.newz.notifications.NotificationReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        int INTERVAL = 1000 * 60 * 1;
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), INTERVAL, pendingIntent);

//        Intent intent = new Intent(this, NotificationReceiver.class);
//        sendBroadcast(intent);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigation, navController);

        HashMap<Integer, Integer> pageMap = new HashMap();
        pageMap.put(R.id.searchFragment, R.id.news_page);
        pageMap.put(R.id.topHeadlinesFragment, R.id.top_page);
        pageMap.put(R.id.bookmarksFragment, R.id.bookmark_page);

        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            NavOptions navOptions = new NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_right)
                    .setExitAnim(R.anim.slide_out_left)
                    .setPopEnterAnim(R.anim.slide_in_left)
                    .setPopExitAnim(R.anim.slide_out_right)
                    .build();

            switch (item.getItemId()) {
                case R.id.news_page:
                    navController.navigate(R.id.searchFragment, InMemoryState.getInstance().getState(R.id.searchFragment), navOptions);
                    return true;
                case R.id.top_page:
                    navController.navigate(R.id.topHeadlinesFragment, InMemoryState.getInstance().getState(R.id.topHeadlinesFragment), navOptions);
                    return true;
                case R.id.bookmark_page:
                    navController.navigate(R.id.bookmarksFragment, InMemoryState.getInstance().getState(R.id.bookmarksFragment), navOptions);
                    return true;
                default:
                    return false;
            }
        });

        bottomNavigation.setOnNavigationItemReselectedListener(item -> {});

        navController.addOnDestinationChangedListener((controller, destination, argument) -> {
            Integer itemId = pageMap.get(destination.getId());
            if (itemId == null) return;

            MenuItem item = bottomNavigation.getMenu().findItem(itemId);
            if (item != null) item.setChecked(true);
        });
    }

    @Override
    public void finish() {
        super.finish();
        ActivityNavigator.applyPopAnimationsToPendingTransition(this);
    }
}