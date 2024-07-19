package com.safeteam.safeplace;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Avvia instanza di firebase, esegue l'autenticazione dell'app.
        mAuth = FirebaseAuth.getInstance();

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

        // Ottieni l'utente autenticato corrente.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Se non è autenticato esegui l'autenticazione.
        if (currentUser != null) {

            // TODO: 19/07/2024 - Da fare, qui le istruzioni:
            // https://firebase.google.com/docs/auth/android/start?hl=it
        }
    }
}