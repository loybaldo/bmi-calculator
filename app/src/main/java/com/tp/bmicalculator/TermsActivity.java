package com.tp.bmicalculator;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class TermsActivity extends AppCompatActivity {
    CoordinatorLayout coordinatorLayout;
    FirebaseFirestore db;
    TextView termsHeaderText, termsMsgText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        coordinatorLayout = findViewById(R.id.terms_cor_layout);
        db = FirebaseFirestore.getInstance();
        termsHeaderText = findViewById(R.id.termsHeader);
        termsMsgText = findViewById(R.id.termsMsg);

        fetchData();

    }

    private void fetchData() {
        DocumentReference docs = db.collection("terms").document("0FQ8q1YRL7WcU9GHNZ1y");
        docs.get().addOnSuccessListener(docSnapshot -> {
            if (docSnapshot.exists()) {
                termsMsgText.setText(docSnapshot.getString("terms"));
                termsHeaderText.setText(R.string.terms_conditions);
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