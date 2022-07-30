package com.tp.bmicalculator;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class AboutActivity extends AppCompatActivity {
    CoordinatorLayout coordinatorLayout;
    SimpleDraweeView illus;
    TextView whoMsgText, whatMsgText, whoHeader, whatHeader;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_about);

        coordinatorLayout = findViewById(R.id.about_cor_layout);
        illus = findViewById(R.id.illusAbout);
        whoMsgText = findViewById(R.id.whoMsg);
        whatMsgText = findViewById(R.id.whatMsg);
        whoHeader = findViewById(R.id.whoHeader);
        whatHeader = findViewById(R.id.whatHeader);

        db = FirebaseFirestore.getInstance();
        fetchData();
    }

    private void fetchData() {
        DocumentReference docs = db.collection("abouts").document("Orx2o36tBBOjpwvJWd00");
        docs.get().addOnSuccessListener(docSnapshot -> {
            if (docSnapshot.exists()) {
                whoMsgText.setText(docSnapshot.getString("who"));
                whatMsgText.setText(docSnapshot.getString("who2"));
                whoHeader.setText(R.string.who_are_we);
                whatHeader.setText(R.string.developers);
                Uri uri = Uri.parse(docSnapshot.getString("school_img"));
                illus.setImageURI(uri);
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