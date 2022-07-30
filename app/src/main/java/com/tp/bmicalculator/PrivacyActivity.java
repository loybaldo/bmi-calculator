package com.tp.bmicalculator;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PrivacyActivity extends AppCompatActivity {
    CoordinatorLayout coordinatorLayout;
    FirebaseFirestore db;
    TextView privacyHeaderText, privacyMsgText;
    TextView infoUsedHeaderText, infoUsedMsgText;
    TextView logDataHeaderText, logDataMsgText;
    TextView spHeaderText, spMsgText;
    TextView secHeaderText, secMsgText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        coordinatorLayout = findViewById(R.id.privacy_cor_layout);
        privacyHeaderText = findViewById(R.id.privacyHeader);
        infoUsedHeaderText = findViewById(R.id.infoUsedHeader);
        logDataHeaderText = findViewById(R.id.logDataHeader);
        spHeaderText = findViewById(R.id.spHeader);
        secHeaderText = findViewById(R.id.securityHeader);

        privacyMsgText = findViewById(R.id.privacyMsg);
        infoUsedMsgText = findViewById(R.id.infoUsedMsg);
        logDataMsgText = findViewById(R.id.logDataMsg);
        spMsgText = findViewById(R.id.spMsg);
        secMsgText = findViewById(R.id.securityMsg);

        db = FirebaseFirestore.getInstance();
        fetchData();
    }

    private void fetchData() {
        DocumentReference docs = db.collection("privacies").document("AyGMgdldT5FIDqEDr4Lw");
        docs.get().addOnSuccessListener(docSnapshot -> {
            if (docSnapshot.exists()) {
                privacyMsgText.setText(docSnapshot.getString("policy"));
                infoUsedMsgText.setText(docSnapshot.getString("info_used"));
                logDataMsgText.setText(docSnapshot.getString("log_data"));
                spMsgText.setText(docSnapshot.getString("service_prov"));
                secMsgText.setText(docSnapshot.getString("security"));

                privacyHeaderText.setText(R.string.g2tp_privacy_policy);
                infoUsedHeaderText.setText(R.string.info_used);
                logDataHeaderText.setText(R.string.log_data);
                spHeaderText.setText(R.string.service_providers);
                secHeaderText.setText(R.string.security);
            }else {
                Snackbar snackbar = Snackbar.make(coordinatorLayout,"Can't connect to the server.",Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }).addOnFailureListener(err -> {
            Snackbar snackbar = Snackbar.make(coordinatorLayout,"Can't connect to the server.",Snackbar.LENGTH_SHORT);
            snackbar.show();
        });

    }

}