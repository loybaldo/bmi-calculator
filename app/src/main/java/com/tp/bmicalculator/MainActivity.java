package com.tp.bmicalculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.tp.bmicalculator.databinding.ActivityMainBinding;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

public class
MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    CoordinatorLayout coordinatorLayout;
    SQLiteDatabase db;
    Button showTipsBtn;
    ImageButton heightMinusBtn, heightAddBtn;
    ImageButton weightMinusBtn, weightAddBtn;
    TextView bmiClassificationDisplay, bmiDisplay;
    TextView heightDisplay, weightDisplay;
    LinearLayout forHeight, forWeight;
    Button saveBtn;
    double BMI;
    int height, weight;
    double meterConversion;
    double underWeightMaxBMI,
            normalMaxBMI,
            overWeightMaxBMI,
            obese1MaxBMI,
            obese2MaxBMI,
            obese3MinBMI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.tp.bmicalculator.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Para sa Nav ug Action bar
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.navHome).setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        // Para pag calculate sa BMI
        BMI = 0;
        height = 0;
        weight = 0;
        meterConversion = 0.01;
        underWeightMaxBMI = 18.4;
        normalMaxBMI = 24.9;
        overWeightMaxBMI = 29.9;
        obese1MaxBMI = 34.9;
        obese2MaxBMI = 39.9;
        obese3MinBMI = 40;
        coordinatorLayout = findViewById(R.id.home_cor_layout);
        showTipsBtn = findViewById(R.id.showTipsBtn);
        // Para sa dialog
        forHeight = findViewById(R.id.forHeight);
        forWeight = findViewById(R.id.forWeight);
        // Para sa mga controls
        heightMinusBtn = findViewById(R.id.heightMinusBtn);
        heightAddBtn = findViewById(R.id.heightAddBtn);
        weightMinusBtn = findViewById(R.id.weightMinusBtn);
        weightAddBtn = findViewById(R.id.weightAddBtn);
        // Para sa mga displays
        heightDisplay = findViewById(R.id.heightText);
        weightDisplay = findViewById(R.id.weightText);
        bmiClassificationDisplay = findViewById(R.id.bmiClassificationText);
        bmiDisplay = findViewById(R.id.bmiText);
        // Create database
        saveBtn = findViewById(R.id.save_btn);





        db = openOrCreateDatabase("HistoryDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS history(bmi_id VARCHAR, bmi_score INT);");

        



        saveBtn.setOnClickListener(v -> {
            Snackbar snackbar = Snackbar.make(coordinatorLayout,"Successfully saved.",Snackbar.LENGTH_SHORT);
            snackbar.setAction("UNDO", v2 -> Toast.makeText(this, "Cancelled.", Toast.LENGTH_SHORT).show()).addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT || event == Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE) {
                        String query = "INSERT INTO history VALUES('" + Math.random() + "','" + BMI + "');";
                        db.execSQL(query);
                    }
                }
            });
            snackbar.show();
        });

        // Initialized
        handleControls();
        // Function para sa dialog input
        forHeight.setOnClickListener(v -> showHeightDialog());
        forWeight.setOnClickListener(v -> showWeightDialog());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_home, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navFeedback:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:juanitobaldoaspler@gmail.com?subject=" + Uri.encode("BMI Calculator"));
                intent.setData(data);
                startActivity(intent);
                break;
            case R.id.navSettings:
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                break;
            default:
                System.out.println("Unknown nav clicked!");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    // Para sa height dialog input
    private void showHeightDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.input_dialog, null);
        builder.setTitle(R.string.enter_height);
        builder.setView(view);
        ((EditText) view.findViewById(R.id.userInput)).setHint(R.string.height);
        if (height <= 0) {
            ((EditText) view.findViewById(R.id.userInput)).setText("");
        }else {
            ((EditText) view.findViewById(R.id.userInput)).setText(String.valueOf(height));
        }
        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.enterBtn).setOnClickListener(v -> {
            if (((EditText) view.findViewById(R.id.userInput)).getText().toString().matches("")) {
                height = 0;
                heightDisplay.setText(R.string.placeholder_undefined);
            }else {
                height = Integer.parseInt(String.valueOf(((EditText) view.findViewById(R.id.userInput)).getText()));
                heightDisplay.setText(String.valueOf(height));
            }
            calculateBMI();
            classifyBMI();
            alertDialog.hide();
        });
        alertDialog.show();
    }

    // Para sa weight dialog input
    private void showWeightDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.input_dialog, null);
        builder.setTitle(R.string.enter_weight);
        builder.setView(view);
        ((EditText) view.findViewById(R.id.userInput)).setHint(R.string.weight);
        if (weight <= 0) {
            ((EditText) view.findViewById(R.id.userInput)).setText("");
        }else {
            ((EditText) view.findViewById(R.id.userInput)).setText(String.valueOf(weight));
        }
        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.enterBtn).setOnClickListener(v -> {
            if (((EditText) view.findViewById(R.id.userInput)).getText().toString().matches("")) {
                weight = 0;
                weightDisplay.setText(R.string.placeholder_undefined);
            }else {
                weight = Integer.parseInt(String.valueOf(((EditText) view.findViewById(R.id.userInput)).getText()));
                weightDisplay.setText(String.valueOf(weight));
            }
            calculateBMI();
            classifyBMI();
            alertDialog.hide();
        });
        alertDialog.show();
    }

    // Pag calculate sa BMI
    private void calculateBMI() {
        if (height != 0 && weight != 0) {
            double heightConverted = height * meterConversion;
            BMI = Math.round(weight / (heightConverted * heightConverted));
            bmiDisplay.setText(String.valueOf(BMI));
            showTipsBtn.setEnabled(true);
            saveBtn.setEnabled(true);
        }else {
            BMI = 0;
            bmiDisplay.setText(R.string.placeholder_undefined);
            showTipsBtn.setEnabled(false);
            saveBtn.setEnabled(false);
        }
    }

    // Pag classify sa BMI
    private void classifyBMI() {
        if (Math.ceil(BMI) >= 0 && BMI <= underWeightMaxBMI) {
            if (height != 0 && weight != 0) {
                bmiClassificationDisplay.setText(R.string.under_weight); // Underweight
            }else {
                bmiClassificationDisplay.setText(R.string.placeholder_undefined);
            }
        }else if (BMI > underWeightMaxBMI && BMI <= normalMaxBMI) {
            bmiClassificationDisplay.setText(R.string.normal); // Normal
        }else if (BMI > normalMaxBMI && BMI <= overWeightMaxBMI) {
            bmiClassificationDisplay.setText(R.string.over_weight); // Overweight
        }else if (BMI > overWeightMaxBMI && BMI <= obese1MaxBMI) {
            bmiClassificationDisplay.setText(R.string.obese_1); // Obese 1
        }else if (BMI > obese1MaxBMI && BMI <= obese2MaxBMI) {
            bmiClassificationDisplay.setText(R.string.obese_2); // Obese 2
        }else if (BMI >= obese3MinBMI) {
            bmiClassificationDisplay.setText(R.string.obese_3); // Obese 3
        }
    }

    // Temporary lang ni na method... kay wala nako na isip dayon nga akong method kay dili diay reusable.
    private String classifyBMI2() {
        if (BMI <= underWeightMaxBMI) {
            return "underweight";
        }else if (BMI <= normalMaxBMI) {
            return "normal";
        }else if (BMI <= overWeightMaxBMI) {
            return "overweight";
        }else if (BMI <= obese1MaxBMI) {
            return "obese1";
        }else if (BMI <= obese2MaxBMI) {
            return "obese2";
        }else if (BMI >= obese3MinBMI) {
            return "obese3";
        }else {
            return "Bug yoooh!";
        }
    }

    // Pag handle sa mga controls
    private void handleControls() {
        // Para sa tips button
        showTipsBtn.setOnClickListener(v -> {
            if (height != 0 && weight != 0) {
                String classification = classifyBMI2();
                Intent intent = new Intent(MainActivity.this, TipsActivity.class);
                intent.putExtra("classification", classification);
                startActivity(intent);
            }
        });

        // Para sa pag sulat sa input
        forHeight.setOnClickListener(v -> Toast.makeText(this, "Height selected", Toast.LENGTH_SHORT).show());
        forWeight.setOnClickListener(v -> Toast.makeText(this, "Weight selected", Toast.LENGTH_SHORT).show());
        // Para sa button controls
        heightMinusBtn.setOnClickListener(v -> {
            if (height > 0) {
                --height;
                if (height > 0) {
                    heightDisplay.setText(String.valueOf(height));
                }else {
                    heightDisplay.setText(R.string.placeholder_undefined);
                }
            }
            calculateBMI();
            classifyBMI();
        });
        heightAddBtn.setOnClickListener(v -> {
            ++height;
            heightDisplay.setText(String.valueOf(height));
            calculateBMI();
            classifyBMI();
        });
        weightMinusBtn.setOnClickListener(v -> {
            if (weight > 0) {
                --weight;
                if (weight > 0) {
                    weightDisplay.setText(String.valueOf(weight));
                }else {
                    weightDisplay.setText(R.string.placeholder_undefined);
                }
            }
            calculateBMI();
            classifyBMI();
        });
        weightAddBtn.setOnClickListener(v -> {
            ++weight;
            weightDisplay.setText(String.valueOf(weight));
            calculateBMI();
            classifyBMI();
        });
    }



}