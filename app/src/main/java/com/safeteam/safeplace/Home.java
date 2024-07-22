package com.safeteam.safeplace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    // ---------------------------------------------------------------------------------------------

    private FirebaseAuth mAuth;


    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ottieni interazione con firebase.
        mAuth = FirebaseAuth.getInstance();

        // Avvia interfaccia grafica dell'attività.

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void onClickSignOut (View view) {

        // Esci dall'accesso di firebase ed entra nella schermata di splash.

        mAuth.signOut();
        startActivity(new Intent(this, Splash.class));
    }


    // ---------------------------------------------------------------------------------------------
}