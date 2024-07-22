package com.safeteam.safeplace;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    // ---------------------------------------------------------------------------------------------

    // Accesso autenticazione Firebase.
    private FirebaseAuth mAuth;

    // Accesso login tramite login.
    private GoogleSignInClient mGoogleSignInClient;

    private static final int RC_SIGN_IN = 9001;


    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Avvia Firebase.
        mAuth = FirebaseAuth.getInstance();

        // Avvia Google Login.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Configurazione interfaccia view.
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

        FirebaseUser user = mAuth.getCurrentUser();

        viewUpdateSignInText();

        if (user != null) {
            onSignedIn(user);
        }
    }


    public void onClickSignOut (View view) {

        mAuth.signOut();
        viewUpdateSignInText();
    }


    public void onClickGoogleAuth (View view) {
        // Chiamato quando l'utente preme il pulsante di autenticazione Google.

        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                Log.d("Auth", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {

                // Google Sign In failed, update UI appropriately
                Log.w("Auth", "Google sign in failed", e);
            }
        }
    }


    private void onSignedIn (FirebaseUser account) {
        // Chiamato dopo aver seguito l'accesso a firebase.

        Log.i("Auth", "Logged in account: " + account.getEmail());

        viewUpdateSignInText();
    }


    private void taskGoogleAuth (Task<AuthResult> task) {

        if (task.isSuccessful()) {

            // Sign in success, update UI with the signed-in user's information
            Log.d("Auth", "signInWithCredential:success");

            onSignedIn(mAuth.getCurrentUser());
        } else {

            // If sign in fails, display a message to the user.
            Log.w("Auth", "signInWithCredential:failure", task.getException());
        }

        viewUpdateSignInText();
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential).addOnCompleteListener(this, this::taskGoogleAuth);
    }


    public void viewUpdateSignInText () {

        TextView elText = findViewById(R.id.test_auth);
        Button elButton = findViewById(R.id.auth_out);
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            elText.setText("Accesso effettuato");
            elButton.setEnabled(true);
        } else {
            elText.setText("Disconnesso");
            elButton.setEnabled(false);
        }
    }


    // ---------------------------------------------------------------------------------------------
}