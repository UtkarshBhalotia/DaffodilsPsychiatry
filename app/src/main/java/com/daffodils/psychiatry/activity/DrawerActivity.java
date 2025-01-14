package com.daffodils.psychiatry.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.fragment.AboutUsFragment;
import com.daffodils.psychiatry.fragment.CartFragment;
import com.daffodils.psychiatry.fragment.ChangePassword;
import com.daffodils.psychiatry.fragment.CoursesFragment;
import com.daffodils.psychiatry.fragment.FacultyFragment;
import com.daffodils.psychiatry.fragment.FeeStructureFragment;
import com.daffodils.psychiatry.fragment.FreeSampleVideosFragment;
import com.daffodils.psychiatry.fragment.HelpSupportFragment;
import com.daffodils.psychiatry.fragment.OfflineVideoFragment;
import com.daffodils.psychiatry.fragment.ProfileFragment;
import com.daffodils.psychiatry.fragment.ReferenceBooksFragment;
import com.daffodils.psychiatry.fragment.SubscribedVideosFragment;
import com.daffodils.psychiatry.fragment.ValidityDetails;
import com.daffodils.psychiatry.helper.DbHelper;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.google.android.material.navigation.NavigationView;

public class DrawerActivity extends AppCompatActivity {

    public static TextView tvName;
    public static DrawerLayout drawer_layout;
    public NavigationView navigationView;
    public ActionBarDrawerToggle drawerToggle;
    public TextView tvMobile;
    protected FrameLayout frameLayout;
    LinearLayout lytProfile;
    DbHelper dbHelper;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ApiConfig.transparentStatusAndNavigation(DrawerActivity.this);
        setContentView(R.layout.activity_drawer);

        dbHelper = new DbHelper(this);
        frameLayout = findViewById(R.id.content_frame);
        navigationView = findViewById(R.id.nav_view);
        drawer_layout = findViewById(R.id.drawer_layout);
        View header = navigationView.getHeaderView(0);
        tvName = header.findViewById(R.id.header_name);
        tvMobile = header.findViewById(R.id.tvMobile);
        lytProfile = header.findViewById(R.id.lytProfile);

        if(GlobalConst.Name.equals("")){

        } else {
            tvName.setText("Welcome " + GlobalConst.Name);
            tvMobile.setText("Mob. " + GlobalConst.Mobile);
        }


        lytProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.closeDrawers();
                startActivity(new Intent(getApplicationContext(), DrawerActivity.class).putExtra("from", "drawer"));
            }
        });
        setupNavigationDrawer();

    }

    private void setupNavigationDrawer() {
        Menu nav_Menu = navigationView.getMenu();
      /*  if (session.isUserLoggedIn()) {
            nav_Menu.findItem(R.id.menu_logout).setVisible(true);
            nav_Menu.setGroupVisible(R.id.group1, true);
            nav_Menu.setGroupVisible(R.id.group2, true);
        } else {
            nav_Menu.findItem(R.id.menu_logout).setVisible(false);
            nav_Menu.setGroupVisible(R.id.group1, false);
            nav_Menu.setGroupVisible(R.id.group2, false);
        }*/

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawer_layout.closeDrawers();
                Fragment fragment;
                Bundle bundle = null;
//                switch (menuItem.getItemId()) {
//                    case R.id.menu_home:
//                        Intent intent = new Intent(DrawerActivity.this, MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.putExtra("from", "home");
//                        startActivity(intent);
//                        finish();
//                        break;
//
//                    case R.id.menu_courses:
//                        fragment = new CoursesFragment();
//                        bundle = new Bundle();
//                        bundle.putString("type", "Courses");
//                        fragment.setArguments(bundle);
//                        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
//                        break;
//
//                    case R.id.menu_sample_vdo:
//                        fragment = new FreeSampleVideosFragment();
//                        bundle = new Bundle();
//                        bundle.putString("type", "SampleVdo");
//                        fragment.setArguments(bundle);
//                        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
//                        break;
//                    case R.id.menu_subs_vdo:
//                        if(GlobalConst.ModuleID.equals("")){
//                            Toast.makeText(DrawerActivity.this, "Please register yourself to view subscribed videos", Toast.LENGTH_LONG).show();
//                        } else {
//                            fragment = new SubscribedVideosFragment();
//                            bundle = new Bundle();
//                            bundle.putString("type", "SubscribedVdo");
//                            fragment.setArguments(bundle);
//                            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
//                        }
//                        break;
//                    case R.id.menu_offline_vdo:
//
//                        fragment = new OfflineVideoFragment();
//                        bundle = new Bundle();
//                        bundle.putString("type", "OfflineVideo");
//                        fragment.setArguments(bundle);
//                        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
//                        break;
//                    case R.id.menu_pricing:
//                        fragment = new FeeStructureFragment();
//                        bundle = new Bundle();
//                        bundle.putString("type", "FeeStructure");
//                        fragment.setArguments(bundle);
//                        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
//                        break;
//                    case R.id.menu_Profile:
//                        fragment = new ProfileFragment();
//                        bundle = new Bundle();
//                        bundle.putString("type", "Profile");
//                        fragment.setArguments(bundle);
//                        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
//                        break;
//                    case R.id.menu_Validity:
//                        fragment = new ValidityDetails();
//                        bundle = new Bundle();
//                        bundle.putString("type", "Validity");
//                        fragment.setArguments(bundle);
//                        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
//                        break;
//
//                    case R.id.menu_cart:
//
//                        if (GlobalConst.User_id.equals("")){
//                           Toast.makeText(getApplicationContext(), "Kindly Login to view cart details.", Toast.LENGTH_LONG).show();
//                        } else {
//                            fragment = new CartFragment();
//                            bundle = new Bundle();
//                            bundle.putString("type", "Cart");
//                            fragment.setArguments(bundle);
//                            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
//                        }
//                        break;
//
//                    case R.id.menu_faculty:
//                        fragment = new FacultyFragment();
//                        bundle = new Bundle();
//                        bundle.putString("type", "Faculty");
//                        fragment.setArguments(bundle);
//                        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
//                        break;
//                    case R.id.menu_books:
//                        fragment = new ReferenceBooksFragment();
//                        bundle = new Bundle();
//                        bundle.putString("type", "Books");
//                        fragment.setArguments(bundle);
//                        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
//                        break;
//
//                    case R.id.menu_AboutUs:
//                        fragment = new AboutUsFragment();
//                        bundle = new Bundle();
//                        bundle.putString("type", "AboutUs");
//                        fragment.setArguments(bundle);
//                        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
//                        break;
//
//                    case R.id.menu_help:
//                        fragment = new HelpSupportFragment();
//                        bundle = new Bundle();
//                        bundle.putString("type", "Help");
//                        fragment.setArguments(bundle);
//                        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
//                        break;
//
//                    case R.id.menu_changepass:
//                        fragment = new ChangePassword();
//                        bundle = new Bundle();
//                        bundle.putString("type", "ChangePass");
//                        fragment.setArguments(bundle);
//                        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
//                        break;
//
//                    case R.id.menu_share:
//                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
//                        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.take_a_look) + "\"" + getString(R.string.app_name) + "\" - " + GlobalConst.PLAY_STORE_LINK + getPackageName());
//                        shareIntent.setType("text/plain");
//                        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)));
//                        break;
//                    case R.id.menu_logout:
//
//                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DrawerActivity.this);
//                        // Setting Dialog Message
//                        alertDialog.setTitle("Exit Application");
//                        alertDialog.setMessage("Do you want to close this application ?");
//                        alertDialog.setCancelable(false);
//                        final AlertDialog alertDialog1 = alertDialog.create();
//
//                        // Setting OK Button
//                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//
//                                try {
//
//                                    dbHelper.openToWrite();
//                                    dbHelper.deleteAllLoginData();
//                                    dbHelper.close();
//                                    Intent i = new Intent(DrawerActivity.this, LoginActivity.class);
//                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(i);
//                                    finish();
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                alertDialog1.dismiss();
//                            }
//                        });
//                        // Showing Alert Message
//                        alertDialog.show();
//
//                       /* session.logoutUser(DrawerActivity.this);
//                        ApiConfig.clearFCM(DrawerActivity.this, session);*/
//                        break;
//                }
                int id = menuItem.getItemId();

                if (id == R.id.menu_home) {
                    Intent intent = new Intent(DrawerActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("from", "home");
                    startActivity(intent);
                    finish();

                } else if (id == R.id.menu_courses) {
                    fragment = new CoursesFragment();
                    bundle = new Bundle();
                    bundle.putString("type", "Courses");
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

                } else if (id == R.id.menu_sample_vdo) {
                    fragment = new FreeSampleVideosFragment();
                    bundle = new Bundle();
                    bundle.putString("type", "SampleVdo");
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

                } else if (id == R.id.menu_subs_vdo) {
                    if (GlobalConst.ModuleID.equals("")) {
                        Toast.makeText(DrawerActivity.this, "Please register yourself to view subscribed videos", Toast.LENGTH_LONG).show();
                    } else {
                        fragment = new SubscribedVideosFragment();
                        bundle = new Bundle();
                        bundle.putString("type", "SubscribedVdo");
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
                    }

                } else if (id == R.id.menu_offline_vdo) {
                    fragment = new OfflineVideoFragment();
                    bundle = new Bundle();
                    bundle.putString("type", "OfflineVideo");
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

                } else if (id == R.id.menu_pricing) {
                    fragment = new FeeStructureFragment();
                    bundle = new Bundle();
                    bundle.putString("type", "FeeStructure");
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

                } else if (id == R.id.menu_Profile) {
                    fragment = new ProfileFragment();
                    bundle = new Bundle();
                    bundle.putString("type", "Profile");
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

                } else if (id == R.id.menu_Validity) {
                    fragment = new ValidityDetails();
                    bundle = new Bundle();
                    bundle.putString("type", "Validity");
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

                } else if (id == R.id.menu_cart) {
                    if (GlobalConst.User_id.equals("")) {
                        Toast.makeText(getApplicationContext(), "Kindly Login to view cart details.", Toast.LENGTH_LONG).show();
                    } else {
                        fragment = new CartFragment();
                        bundle = new Bundle();
                        bundle.putString("type", "Cart");
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
                    }

                } else if (id == R.id.menu_faculty) {
                    fragment = new FacultyFragment();
                    bundle = new Bundle();
                    bundle.putString("type", "Faculty");
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

                } else if (id == R.id.menu_books) {
                    fragment = new ReferenceBooksFragment();
                    bundle = new Bundle();
                    bundle.putString("type", "Books");
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

                } else if (id == R.id.menu_AboutUs) {
                    fragment = new AboutUsFragment();
                    bundle = new Bundle();
                    bundle.putString("type", "AboutUs");
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

                } else if (id == R.id.menu_help) {
                    fragment = new HelpSupportFragment();
                    bundle = new Bundle();
                    bundle.putString("type", "Help");
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

                } else if (id == R.id.menu_changepass) {
                    fragment = new ChangePassword();
                    bundle = new Bundle();
                    bundle.putString("type", "ChangePass");
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

                } else if (id == R.id.menu_share) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.take_a_look) + "\"" + getString(R.string.app_name) + "\" - " + GlobalConst.PLAY_STORE_LINK + getPackageName());
                    shareIntent.setType("text/plain");
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)));

                } else if (id == R.id.menu_logout) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DrawerActivity.this);
                    alertDialog.setTitle("Exit Application");
                    alertDialog.setMessage("Do you want to close this application?");
                    alertDialog.setCancelable(false);
                    final AlertDialog alertDialog1 = alertDialog.create();

                    alertDialog.setPositiveButton("Yes", (dialog, which) -> {
                        try {
                            dbHelper.openToWrite();
                            dbHelper.deleteAllLoginData();
                            dbHelper.close();
                            Intent i = new Intent(DrawerActivity.this, LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    alertDialog.setNegativeButton("No", (dialog, which) -> alertDialog1.dismiss());
                    alertDialog.show();
                }


                return true;
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }
}


