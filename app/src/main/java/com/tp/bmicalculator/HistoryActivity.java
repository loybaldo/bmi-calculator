package com.tp.bmicalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    SQLiteDatabase db;
    RecyclerView recyclerView;
    ArrayList<String> arrayList = new ArrayList<>();
    HistoryAdapter adapter;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        coordinatorLayout = findViewById(R.id.history_cor_layout);
        recyclerView = findViewById(R.id.history_list);
        db = openOrCreateDatabase("HistoryDB", Context.MODE_PRIVATE, null);
        arrayList.addAll(getHistory());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_history, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint({"NonConstantResourceId", "Recycle", "NotifyDataSetChanged"})
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.del_all) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.delete_history);
            builder.setMessage(R.string.delete_history_msg);
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.delete, (dialog, id) -> {
                arrayList.clear();
                adapter.notifyDataSetChanged();
                Snackbar snackbar = Snackbar.make(coordinatorLayout,R.string.history_deleted,Snackbar.LENGTH_SHORT);
                snackbar.setAction("UNDO", v -> {
                    arrayList.addAll(getHistory());
                    adapter.notifyDataSetChanged();
                }).addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                            db.execSQL("DELETE FROM history");
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                snackbar.show();
            });
            builder.setNegativeButton(R.string.cancel, (dialog, id) -> { });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<String> getHistory() {
        ArrayList<String> data = new ArrayList<>();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM history", null);
        while (cursor.moveToNext()) { data.add(cursor.getString(1)); }
        return data;
    }

}