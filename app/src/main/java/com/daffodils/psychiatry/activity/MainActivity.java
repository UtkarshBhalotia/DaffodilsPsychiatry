package com.daffodils.psychiatry.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.fragment.AboutUsFragment;
import com.daffodils.psychiatry.fragment.HelpSupportFragment;
import com.daffodils.psychiatry.fragment.HomeFragment;
import com.daffodils.psychiatry.fragment.ProfileFragment;
import com.daffodils.psychiatry.helper.DbHelper;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

public class MainActivity extends DrawerActivity {

    private static final String TAG = "MAIN ACTIVITY";
    public static Toolbar toolbar;
    public static BottomNavigationView bottomNavigationView;
    public static Fragment active;
    public static FragmentManager fm = null;
    public static Fragment homeFragment, profileFragment, aboutusFragment, helpFragment;
    public static boolean homeClicked = false, profileClicked = false, aboutClicked = false, helpClicked = false;
    public Activity activity;
    boolean doubleBackToExitPressedOnce = false;
    Menu menu;
    DbHelper databaseHelper;
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        activity = MainActivity.this;
        from = getIntent().getStringExtra("from");
        databaseHelper = new DbHelper(activity);

        //    BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);//disable BottomNavigationView shift mode
        //   bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottomNavigationView.getMenu().findItem(R.id.navigation_home).setTitle("Home");

        fm = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        active = homeFragment;
        homeClicked = true;
        fm.beginTransaction().add(R.id.container, homeFragment).commit();

        profileFragment = new ProfileFragment();
        aboutusFragment = new AboutUsFragment();
        helpFragment = new HelpSupportFragment();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        if (!homeClicked) {
                            fm.beginTransaction().add(R.id.container, homeFragment).show(homeFragment).hide(active).commit();
                            homeClicked = true;
                        } else {
                            fm.beginTransaction().show(homeFragment).hide(active).commit();
                        }
                        active = homeFragment;
                        return true;

                    case R.id.navigation_profile:
                        if (!profileClicked) {
                            fm.beginTransaction().add(R.id.container, profileFragment).show(profileFragment).hide(active).commit();
                            profileClicked = true;
                        } else {
                            fm.beginTransaction().show(profileFragment).hide(active).commit();
                        }
                        active = profileFragment;
                        return true;

                    case R.id.navigation_about:
                        if (!aboutClicked) {
                            fm.beginTransaction().add(R.id.container, aboutusFragment).show(aboutusFragment).hide(active).commit();
                            aboutClicked = true;
                        } else {
                            fm.beginTransaction().show(aboutusFragment).hide(active).commit();
                        }
                        active = aboutusFragment;
                        return true;

                    case R.id.navigation_support:
                        if (!helpClicked) {
                            fm.beginTransaction().add(R.id.container, helpFragment).show(helpFragment).hide(active).commit();
                            helpClicked = true;
                        } else {
                            fm.beginTransaction().show(helpFragment).hide(active).commit();
                        }
                        active = helpFragment;
                        return true;



                }
                return false;
            }
        });

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.navigation_home:
                    case R.id.navigation_profile:
                    case R.id.navigation_about:
                    case R.id.navigation_support:
                        break;
                }
            }
        });

        drawerToggle = new ActionBarDrawerToggle
                (
                        this,
                        drawer_layout, toolbar,
                        R.string.drawer_open,
                        R.string.drawer_close
                ) {
        };

        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment currentFragment = fm.findFragmentById(R.id.container);
                currentFragment.onResume();
            }
        });

        //  GetSettings(activity);

    }

//    public void setAppLocal(String languageCode){
//        Resources resources = getResources ();
//        DisplayMetrics dm = resources.getDisplayMetrics ();
//        Configuration configuration = resources.getConfiguration ();
//        configuration.setLocale (new Locale(languageCode.toLowerCase ()));
//        resources.updateConfiguration (configuration,dm);
//    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(navigationView))
            drawer_layout.closeDrawers();
        else
            doubleBack();
    }

    public void doubleBack() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        if (fm.getBackStackEntryCount() == 0) {
            if (fm.findFragmentById(R.id.container) != homeFragment) {
                bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                homeClicked = true;
                fm.beginTransaction().hide(active).show(homeFragment).commit();
                active = homeFragment;
            } else {
                Toast.makeText(this, getString(R.string.exit_msg), Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
           /* case R.id.toolbar_cart:
                Constant.OFFSET_CART = 0;
                MainActivity.fm.beginTransaction().add(R.id.container, new CartFragment()).addToBackStack(null).commit();
                break;
            case R.id.toolbar_search:
                Fragment fragment = new SearchFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "search");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
                break;
            case R.id.toolbar_logout:
                session.logoutUser(activity);
                ApiConfig.clearFCM(activity, session);
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
       /* menu.findItem(R.id.toolbar_cart).setVisible(true);
        menu.findItem(R.id.toolbar_search).setVisible(true);
        menu.findItem(R.id.toolbar_cart).setIcon(ApiConfig.buildCounterDrawable(Constant.TOTAL_CART_ITEM, R.drawable.ic_cart, activity));
        invalidateOptionsMenu();
*/
        if (fm.getBackStackEntryCount() > 0) {

            bottomNavigationView.setVisibility(View.GONE);

            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setTitle(GlobalConst.TOOLBAR_TITLE);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fm.popBackStack();
                }
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    DrawerActivity.drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
            }, 500);
        } else {
            DrawerActivity.drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            bottomNavigationView.setVisibility(View.VISIBLE);
            toolbar.setNavigationIcon(R.drawable.ic_menu);
            toolbar.setTitle(getString(R.string.app_name));
            drawerToggle = new ActionBarDrawerToggle
                    (
                            this,
                            drawer_layout, toolbar,
                            R.string.drawer_open,
                            R.string.drawer_close
                    ) {
            };
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        fragment.onActivityResult(requestCode, resultCode, data);

    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
}