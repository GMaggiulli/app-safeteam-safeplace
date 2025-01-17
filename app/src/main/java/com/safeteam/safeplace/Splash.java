package com.safeteam.safeplace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Avvia interfaccia grafica dell'attività.

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_splash);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    @Override
    public void onStart() {
        super.onStart();

        // Controlla se è presente un login dell'utente, se presente passa alla home.
        // Se non cè un login passa alla schermata di accesso.

        FirebaseUser user = MainActivity.mAuth.getCurrentUser();

        if (user != null)
        {
            Log.d("Auth", "Accesso effettuato.");

            startActivity(new Intent(this, Home.class));
        }
        else
        {
            Log.w("Auth", "Necessita accesso.");

            startActivity(new Intent(this, Login.class));
        }
    }


    // ---------------------------------------------------------------------------------------------
}