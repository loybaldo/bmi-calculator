package com.tp.bmicalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class IntroAdapter extends PagerAdapter {
    Context context;

    int[] images = {
            R.drawable.res_illus_girl_smile,
            R.drawable.res_illus_bmi,
            R.drawable.illustration_3,
    };

    int[] headings = {
            R.string.intro_title_1,
            R.string.intro_title_2,
            R.string.intro_title_3,
    };

    int[] descriptions = {
            R.string.intro_desc_1,
            R.string.intro_desc_2,
            R.string.intro_desc_3,
    };

    public IntroAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return  headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_slider,container,false);

        ImageView slideImg = view.findViewById(R.id.onBoardingImg);
        TextView slideHeading = view.findViewById(R.id.onBoardingTitle);
        TextView slideDesc = view.findViewById(R.id.onBoardingDesc);

        slideImg.setImageResource(images[position]);
        slideHeading.setText(headings[position]);
        slideDesc.setText(descriptions[position]);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}