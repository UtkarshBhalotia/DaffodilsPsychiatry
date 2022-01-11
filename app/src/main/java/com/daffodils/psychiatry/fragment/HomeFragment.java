package com.daffodils.psychiatry.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.daffodils.psychiatry.R;
import com.daffodils.psychiatry.activity.MainActivity;
import com.daffodils.psychiatry.adapter.SliderAdapter;
import com.daffodils.psychiatry.helper.ApiConfig;
import com.daffodils.psychiatry.helper.AppController;
import com.daffodils.psychiatry.helper.GlobalConst;
import com.daffodils.psychiatry.ui.CircleImageView;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import static android.content.Context.INPUT_METHOD_SERVICE;

public class HomeFragment extends Fragment {

    Activity activity;
    NestedScrollView nestedScrollView;
    RelativeLayout progressBar;
    SwipeRefreshLayout swipeLayout;
    View root;
    int timerDelay = 0, timerWaiting = 0;
    private RecyclerView categoryRecyclerView, sectionView, offerView;
    private ArrayList<Slider> sliderArrayList;
    public static Integer [] images = {R.drawable.banner1, R.drawable.banner2};
    private ViewPager mPager;
    private LinearLayout mMarkersLayout;
    private int size = 5;
    private Timer swipeTimer;
    private Handler handler;
    private Runnable Update;
    private int currentPage = 0;
    CircleImageView imgSampleVdo, imgSubsVdo, imgProfile, imgPricing;
    private LinearLayout llSampleVdo, llSubsVdo, llUpdateProfile, llPricing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.home_fragment, container, false);
        timerDelay = 3000;
        timerWaiting = 3000;

        activity = getActivity();
        setHasOptionsMenu(true);

        progressBar = root.findViewById(R.id.progressBar);
        swipeLayout = root.findViewById(R.id.swipeLayout);
        llPricing = root.findViewById(R.id.llPricing);
        llUpdateProfile = root.findViewById(R.id.llUpdateProfile);
        llSampleVdo = root.findViewById(R.id.llSampleVdo);
        llSubsVdo = root.findViewById(R.id.llSubsVdo);

        imgPricing = root.findViewById(R.id.imgPricing);
        imgProfile = root.findViewById(R.id.imgUpdateProf);
        imgSampleVdo = root.findViewById(R.id.imgSampleVdo);
        imgSubsVdo = root.findViewById(R.id.imgSubscribeVdo);

        imgSampleVdo.setDefaultImageResId(R.drawable.sample_vdo);
        imgSubsVdo.setDefaultImageResId(R.drawable.subscribe_vdo);
        imgProfile.setDefaultImageResId(R.drawable.profile);
        imgPricing.setDefaultImageResId(R.drawable.pricing);

        nestedScrollView = root.findViewById(R.id.nestedScrollView);
        mMarkersLayout = root.findViewById(R.id.layout_markers);
        mPager = root.findViewById(R.id.pager);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                ApiConfig.addMarkers(position, images, mMarkersLayout, getContext());
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        swipeLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipeTimer != null) {
                    swipeTimer.cancel();
                }
                if (AppController.isConnected(getActivity())) {
                    GetSlider();
                }
                swipeLayout.setRefreshing(false);
            }
        });

        llSampleVdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FreeSampleVideosFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "SampleVdos");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
            }
        });

        llSubsVdo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(GlobalConst.ModuleID.equals("")){
                    Toast.makeText(activity, "Please register yourself to view subscribed videos", Toast.LENGTH_LONG).show();
                } else {
                    Fragment fragment = new SubscribedVideosFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("from", "SubsVdo");
                    fragment.setArguments(bundle);
                    MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
                }
            }
        });

        llUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new ProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "Profile");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

            }
        });

        llPricing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new FeeStructureFragment();
                Bundle bundle = new Bundle();
                bundle.putString("from", "FeeStructure");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();

            }
        });


        if (AppController.isConnected(getActivity())) {
            GetSlider();
        }

        return root;
    }

    private void GetSlider() {

        SliderAdapter viewPagerAdapter = new SliderAdapter(getContext());
        mPager.setAdapter(viewPagerAdapter);

        //  mPager.setAdapter(new SliderAdapter(sliderArrayList, getActivity(), R.layout.lyt_slider, "home"));
        ApiConfig.addMarkers(0, images, mMarkersLayout, getContext());
        handler = new Handler();
        Update = new Runnable() {
            public void run() {
                if (currentPage == size) {
                    currentPage = 0;
                }
                try {
                    mPager.setCurrentItem(currentPage++, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, timerDelay, timerWaiting);
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalConst.TOOLBAR_TITLE = getString(R.string.app_name);
        getActivity().invalidateOptionsMenu();
        hideKeyboard();
    }

    public void hideKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(root.getApplicationWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


