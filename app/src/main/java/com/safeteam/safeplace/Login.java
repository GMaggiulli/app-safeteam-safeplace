package com.safeteam.safeplace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity {

    // ---------------------------------------------------------------------------------------------

    private GoogleSignInClient mGoogleSignInClient;

    private static final int RC_SIGN_IN = 9001;


    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Costruisce le informazioni inviate alla richiesta di accesso tramite Google.
        // Queste verranno usate DOPO per inviare la richiesta.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Avvia interfaccia grafica dell'attività.

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                // Questa linea risolve la richiesta di accesso a Google, se fallisce il resto dopo non
                // viene eseguito.
                GoogleSignInAccount account = task.getResult(ApiException.class);

                Log.d("Auth", String.format("Accesso Google: %s", account.getId()));

                String accessToken = account.getIdToken();

                // Dopo aver fatto accesso all'account Google, noi eseguiamo l'accesso a Firebase con
                // le credenziali di Google.
                MainActivity.mAuth.signInWithCredential(GoogleAuthProvider.getCredential(accessToken, null))
                    .addOnCompleteListener(this, this::taskGoogleAuth);

            } catch (ApiException e) {

                Log.w("Auth", "Google sign in failed", e);
            }
        }
    }



    public void onClickGoogleAuth (View view) {
        // Chiamato quando l'utente preme il pulsante di autenticazione Google.

        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }


    // ---------------------------------------------------------------------------------------------

    private void taskGoogleAuth (Task<AuthResult> task) {

        if (task.isSuccessful()) {

            Log.d("Auth", "signInWithCredential:success");

            // Solo per essere sicuri che l'accesso è stato eseguito correttamente tra le attività, qui
            // riportiamo l'utente allo splash.
            // Se tutto è ok lo splash poi porterà alla home.
            startActivity(new Intent(this, Splash.class));
        } else {

            Log.w("Auth", "signInWithCredential:failure", task.getException());
        }
    }


    // ---------------------------------------------------------------------------------------------
}