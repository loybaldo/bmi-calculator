package com.tp.bmicalculator;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IntroActivity extends AppCompatActivity {
    ViewPager mSlideViewPager;
    LinearLayout mDotLayout;
    Button backBtn, nextBtn, skipBtn;
    TextView[] dots;
    IntroAdapter introAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Mga controls sa screen
        backBtn = findViewById(R.id.backBtn);
        nextBtn = findViewById(R.id.nextBtn);
        skipBtn = findViewById(R.id.skipBtn);

        // Mga event listener sa mga controls
        backBtn.setOnClickListener(v -> {
            if (getItem(0) > 0) {
                mSlideViewPager.setCurrentItem(getItem(-1), true);
            }
        });

        nextBtn.setOnClickListener(v -> {
            if (getItem(0) < 2) {
                mSlideViewPager.setCurrentItem(getItem(1), true);
            }else {
                Intent i = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        skipBtn.setOnClickListener(v -> {
            Intent i = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });

        mSlideViewPager = findViewById(R.id.slideViewPager);
        mDotLayout = findViewById(R.id.indicatorLayout);
        introAdapter = new IntroAdapter(this);
        mSlideViewPager.setAdapter(introAdapter);
        setupIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);
    }

    // I-load ang theme kung unsa ang naka save sa settings.
    public void setupIndicator(int position){
        dots = new TextView[3];
        mDotLayout.removeAllViews();
        for (int i = 0 ; i < dots.length ; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.indicator_inactive,getApplicationContext().getTheme()));
            mDotLayout.addView(dots[i]);
        }
        dots[position].setTextColor(getResources().getColor(R.color.indicator_active,getApplicationContext().getTheme()));
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setupIndicator(position);
            // NEXT BUTTON: Mag change ang text sa next button to "finish" kung naa na ang user sa last page.
            if (getItem(0) == 2) {
                nextBtn.setText(R.string.finish);
            }else {
                nextBtn.setText(R.string.next);
            }

            // BACK BUTTON: Mag hide ang back button kung naa ang user sa first page.
            if (position > 0){
                backBtn.setVisibility(View.VISIBLE);
            }else {
                backBtn.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getItem(int i) {
        return mSlideViewPager.getCurrentItem() + i;
    }

}