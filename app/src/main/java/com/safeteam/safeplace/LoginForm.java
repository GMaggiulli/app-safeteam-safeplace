package com.safeteam.safeplace;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class LoginForm extends AppCompatActivity {

    // ---------------------------------------------------------------------------------------------

    ProgressBar uploadProgress = null;

    EditText textName = null;

    EditText textSurname = null;

    private StorageReference userStorage = null;


    // ---------------------------------------------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Avvia interfaccia grafica dell'attività.

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_login_form);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = MainActivity.mAuth.getCurrentUser();

        if (user != null) {

            uploadProgress = (ProgressBar) findViewById(R.id.uploadProgress);
            uploadProgress.setVisibility(View.INVISIBLE);

            textName = (EditText) findViewById(R.id.inputName);
            textSurname = (EditText) findViewById(R.id.inputSurname);

            StorageReference userFile = userStorage = MainActivity.storage.getReference()
                .child(String.format("users/%s/user.json", user.getUid()));

            userFile.getDownloadUrl()
                .addOnSuccessListener(this::onUserJsonSuccess)
                .addOnFailureListener(this::onUserJsonFailure);

        } else {

            Log.e("Auth", "Come hai fatto a venire qui?");
            startActivity(new Intent(this, Splash.class));
        }
    }


    public void onUserJsonSuccess (Uri data) {}


    public void onUserJsonFailure (Exception e) {}


    public void onClickContinue (View view) {

        String valueName = textName.getText().toString();
        String valueSurname = textSurname.getText().toString();

        if (valueName.isEmpty() || valueSurname.isEmpty())
        {
            new AlertDialog.Builder(this)
                .setTitle("Errore")
                .setMessage("Non ai inserito i dati necessari per continuare.")
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        }
        else
        {
            HashMap<String, Object> data = new HashMap<>();
            data.put("Name", valueName);
            data.put("Surname", valueSurname);

            JSONObject json = new JSONObject(data);

            uploadProgress.setProgress(0);
            uploadProgress.setVisibility(View.VISIBLE);

            userStorage.putBytes(json.toString().getBytes(StandardCharsets.UTF_8))
                .addOnProgressListener(this::onUploadProgress)
                .addOnCompleteListener(this::onUploadSuccess)
                .addOnFailureListener(this::onUploadFail);
        }
    }


    private void onUploadFail(Exception e) {

        Log.e("UserData", "Upload failed.", e);
        uploadProgress.setVisibility(View.INVISIBLE);
    }


    private void onUploadSuccess(Task<UploadTask.TaskSnapshot> taskSnapshotTask) {

        uploadProgress.setProgress(100);
        startActivity(new Intent(this, Splash.class));
    }


    public void onUploadProgress (UploadTask.TaskSnapshot task) {

        int progress = (int) ((100 * task.getBytesTransferred()) / task.getTotalByteCount());

        uploadProgress.setProgress(progress);
    }


    // ---------------------------------------------------------------------------------------------
}