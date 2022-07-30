package com.tp.bmicalculator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    Activity activity;
    ArrayList<String> arrayList;
    double underWeightMaxBMI = 18.4;
    double normalMaxBMI = 24.9;
    double overWeightMaxBMI = 29.9;
    double obese1MaxBMI = 34.9;
    double obese2MaxBMI = 39.9;
    double obese3MinBMI = 40;

    public HistoryAdapter(Activity activity, ArrayList<String> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        holder.bmiView.setText("BMI: " + arrayList.get(position));
        holder.statusView.setText("Status: " + classifyBMI(Double.parseDouble(arrayList.get(position))));
        holder.indicator.setImageResource(getIndicator(Double.parseDouble(arrayList.get(position))));
    }

    public int getIndicator(double BMI) {
        if (BMI <= underWeightMaxBMI) {
            return R.drawable.ic_stat_danger;
        }else if (BMI <= normalMaxBMI) {
            return R.drawable.ic_stat_ok;
        }else {
            return R.drawable.ic_stat_warn;
        }
    }

    @Override
    public int getItemCount() { return arrayList.size(); }

    private String classifyBMI(double BMI) {
        if (Math.ceil(BMI) >= 0 && BMI <= underWeightMaxBMI) {
            return "Underweight";
        }else if (BMI > underWeightMaxBMI && BMI <= normalMaxBMI) {
            return "Normal";
        }else if (BMI > normalMaxBMI && BMI <= overWeightMaxBMI) {
            return "Overweight";
        }else if (BMI > overWeightMaxBMI && BMI <= obese1MaxBMI) {
            return "Obese 1";
        }else if (BMI > obese1MaxBMI && BMI <= obese2MaxBMI) {
            return "Obese 2";
        }else if (BMI >= obese3MinBMI) {
            return "Obese 3";
        }else {
            return "Unknown";
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView bmiView, statusView;
        ImageView indicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bmiView = itemView.findViewById(R.id.bmi_view);
            statusView = itemView.findViewById(R.id.status_view);
            indicator = itemView.findViewById(R.id.item_list_indicator);
        }
    }

}
