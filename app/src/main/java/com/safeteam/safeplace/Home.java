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

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class Home extends AppCompatActivity {

    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Avvia interfaccia grafica dell'attività.

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    @Override
    public void onStart () {
        super.onStart();

        FirebaseUser user = MainActivity.mAuth.getCurrentUser();

        if (user != null) {

            StorageReference userStorage = MainActivity.storage.getReference()
                .child(String.format("users/%s/user.json", user.getUid()));

            userStorage.getBytes(16_000)
                .addOnSuccessListener(this::onUserJsonSuccess)
                .addOnFailureListener(this::onUserJsonFailure);

        } else {

            Log.e("Auth", "Come hai fatto a venire qui?");
            startActivity(new Intent(this, Splash.class));
        }
    }


    public void onClickSignOut (View view) {

        // Esci dall'accesso di firebase ed entra nella schermata di splash.

        MainActivity.mAuth.signOut();
        startActivity(new Intent(this, Splash.class));
    }


    public void onUserJsonSuccess (byte[] data) {

        Log.d("UserData", "Received user data.");

        String jsonRaw = new String(data, StandardCharsets.UTF_8);

        try {

            JSONObject json = new JSONObject(jsonRaw);

            Log.i("Name", (String) json.get("Name"));
            Log.i("Username", (String) json.get("Surname"));

        } catch (JSONException e) {
            onUserJsonFailure(e);
        }
    }


    public void onUserJsonFailure (Exception e) {

        Log.e("UserStorage", "Utente non ha uno storage oppure non è valido.", e);
        startActivity(new Intent(this, LoginForm.class));
    }


    // ---------------------------------------------------------------------------------------------
}