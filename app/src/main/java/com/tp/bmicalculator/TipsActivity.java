package com.tp.bmicalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class TipsActivity extends AppCompatActivity {
    FirebaseFirestore db;
    TextView tipsHeaderText, tipsMsgText;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        db = FirebaseFirestore.getInstance();

        tipsHeaderText = findViewById(R.id.tipsHeader);
        tipsMsgText = findViewById(R.id.tipsMsg);

        Intent intent = getIntent();
        String classification = intent.getStringExtra("classification");
        tipsHeaderText.setText(classification);

        fetchData();

    }

    private void fetchData() {
        DocumentReference docs = db.collection("tips").document("llqiSfoI9r5srnIs6nfo");
        docs.get().addOnSuccessListener(docSnapshot -> {
            Intent intent = getIntent();
            String classification = intent.getStringExtra("classification");
            if (docSnapshot.exists()) {
                switch (classification) {
                    case "underweight":
                        tipsMsgText.setText(docSnapshot.getString("underweight"));
                        break;
                    case "normal":
                        tipsMsgText.setText(docSnapshot.getString("normal"));
                        break;
                    case "overweight":
                        tipsMsgText.setText(docSnapshot.getString("overweight"));
                        break;
                    case "obese1":
                        tipsMsgText.setText(docSnapshot.getString("obese1"));
                        break;
                    case "obese2":
                        tipsMsgText.setText(docSnapshot.getString("obese2"));
                        break;
                    case "obese3":
                        tipsMsgText.setText(docSnapshot.getString("obese3"));
                        break;
                    default:
                        Toast.makeText(this, R.string.there_was_err, Toast.LENGTH_SHORT).show();
                }
                tipsHeaderText.setText(classification);
            }else {
                Toast.makeText(this, R.string.err_get_data_msg, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(err -> Toast.makeText(this, R.string.err_get_data_msg, Toast.LENGTH_SHORT).show());

    }


}