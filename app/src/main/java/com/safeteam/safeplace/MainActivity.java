package com.safeteam.safeplace;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

public class MainActivity extends AppCompatActivity {

    // ---------------------------------------------------------------------------------------------

    public static FirebaseAuth mAuth = null;

    public static FirebaseStorage storage = null;


    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ottieni interazione con firebase.

        FirebaseApp.initializeApp(this);

        MainActivity.mAuth = FirebaseAuth.getInstance();
        MainActivity.storage = FirebaseStorage.getInstance();

        // Avvia interfaccia grafica dell'attività.

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    @Override
    public void onStart() {
        super.onStart();

        // Appena le variabili dell'applicazione sono configurate, noi entriamo nel loop dell'app utente.

        startActivity(new Intent(this, Splash.class));
    }


    // ---------------------------------------------------------------------------------------------
}