package com.safeteam.safeplace;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // ---------------------------------------------------------------------------------------------

    private FirebaseAuth mAuth;


    // ---------------------------------------------------------------------------------------------

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

        // Ottieni l'autenticazione attiva corrente dell'utente.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            // L'utente ha già un accesso attivo.

            onSignedIn(currentUser);
        } else {

            // L'utente deve accedere o registrarsi.

            // TODO: 19/07/2024 - Credenziali temporanee per test.
            // https://firebase.google.com/docs/auth/android/start?hl=it

            String email = "gmaggiulli@studenti.apuliadigitalmaker.it";
            String password = "test-auth!";

            userSignIn(email, password);
        }
    }


    private void userSignIn (String email, String password) {

        // Esegue un login ad un account già esistente per l'utente.

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, task -> {

                if (task.isSuccessful()) {

                    // Evviva! Abbiamo eseguito l'accesso all'account dell'utente.

                    Log.d("Auth", "SignIn success.");

                    onSignedIn(mAuth.getCurrentUser());
                } else {

                    // Ops! Accesso fallito, credenziali errate o account non esistente.

                    Log.e("Auth", "SignIn failed.", task.getException());

                    // TODO: 19/07/2024 - Test, se account non esiste lo creiamo immediatamente.
                    // Questo poi verrà spostato nella schermata di registrazione.

                    userCreateAccount(email, password);
                }
            });
    }


    private void userCreateAccount (String email, String password) {

        // Esegue una nuova registrazione per l'utente.

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, task -> {

                if (task.isSuccessful()) {

                    // Account creato con successo!

                    Log.d("Auth", "Registered new account.");

                    onSignedIn(mAuth.getCurrentUser());
                } else {

                    // Errore nella registrazione dell'account.

                    Log.e("Auth", "Failed to register new account.");
                }
            });
    }


    private void onSignedIn (FirebaseUser account) {

        Log.i("Auth", "Logged in account: " + account.getEmail());
    }


    // ---------------------------------------------------------------------------------------------
}