package com.cretueusebiu.taskmanager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;

import com.cretueusebiu.taskmanager.models.AbstractModel;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class AbstractActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_CREATE_TASK = 1;
    public static final int REQUEST_CREATE_REMINDER = 4;
    protected static final String SEARCH_OPENED = "search_opened";
    protected static final String SEARCH_QUERY = "search_query";

    protected FloatingActionMenu floatingMenu;
    protected Menu menu;

    protected Drawable iconOpenSearch;
    protected Drawable iconCloseSearch;
    protected EditText searchInput;
    protected MenuItem searchMenuItem;
    protected boolean searchOpened = false;
    protected String searchQuery = "";

    protected void initialize() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        addFloatingMenu();

        AbstractModel.setContext(this);

        iconOpenSearch = ContextCompat.getDrawable(this, R.drawable.ic_search);
        iconCloseSearch = ContextCompat.getDrawable(this, R.drawable.ic_clear);
    }

    protected void openSearchBar(String queryText) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.search_bar);

        searchInput = (EditText) actionBar.getCustomView().findViewById(R.id.search_input);
        searchInput.addTextChangedListener(searchWatcher);
        searchInput.setText(queryText);
        searchInput.requestFocus();

        searchMenuItem.setIcon(iconCloseSearch);
        searchOpened = true;

        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    protected void closeSearchBar() {
        getSupportActionBar().setDisplayShowCustomEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        searchMenuItem.setIcon(iconOpenSearch);
        searchOpened = false;
        searchQuery = "";
        onSearchTextChanged(searchQuery);
    }

    protected boolean isSearchOpened() {
        return  searchOpened;
    }

    private void addFloatingMenu() {
        floatingMenu = (FloatingActionMenu) findViewById(R.id.floating_menu);
        floatingMenu.setClosedOnTouchOutside(true);
        floatingMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (floatingMenu.isOpened()) {
                    floatingMenu.close(false);
                    closeSearchBar();
                    Intent intent = new Intent(v.getContext(), CreateTask.class);
                    startActivityForResult(intent, REQUEST_CREATE_TASK);
                } else {
                    floatingMenu.toggle(true);
                }
            }
        });

        animateFab(floatingMenu, R.drawable.fab_add, R.drawable.ic_action_list);

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.create_reminder);
        fab2.setOnClickListener(fabClickListener);
    }

    protected  View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            floatingMenu.toggle(false);

            switch (view.getId()) {
                case R.id.create_reminder:
                    closeSearchBar();
                    Intent intent = new Intent(view.getContext(), CreateReminder.class);
                    startActivityForResult(intent, REQUEST_CREATE_REMINDER);
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        searchMenuItem = menu.findItem(R.id.search_menu_item);

        if (searchOpened) {
            openSearchBar(searchQuery);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.search_menu_item:
                if (searchOpened) {
                    closeSearchBar();
                } else {
                    openSearchBar(searchQuery);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.nav_tasks:
                intent = new Intent(this, MainActivity.class);
                break;

            case R.id.nav_reminders:
                intent = new Intent(this, RemindersActivity.class);
                break;
        }

        if (intent != null ) {
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SEARCH_OPENED, searchOpened);
        outState.putString(SEARCH_QUERY, searchQuery);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        searchOpened = savedInstanceState.getBoolean(SEARCH_OPENED);
        searchQuery = savedInstanceState.getString(SEARCH_QUERY);
    }


    protected TextWatcher searchWatcher =  new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            searchQuery = searchInput.getText().toString();
            onSearchTextChanged(searchQuery);
        }
    };

    protected void onSearchTextChanged(String query) {}

    protected void animateFab(final FloatingActionMenu menu, final int opened, final int closed) {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(menu.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(menu.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(menu.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(menu.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                menu.getMenuIconView().setImageResource(menu.isOpened() ? opened : closed);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        menu.setIconToggleAnimatorSet(set);
    }
}
