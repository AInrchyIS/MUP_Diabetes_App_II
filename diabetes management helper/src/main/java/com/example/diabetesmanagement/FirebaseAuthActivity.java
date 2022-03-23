package com.example.diabetesmanagement;
//package com.google.firebase.quickstart.auth;

import android.content.Intent;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;

import com.example.diabetesmanagement.constatntClass.ConstantClass;
import com.example.diabetesmanagement.data.User;
import com.example.diabetesmanagement.service.UserStorage;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class FirebaseAuthActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_auth);
        createSignInIntent();
    }

    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());
        //new AuthUI.IdpConfig.PhoneBuilder().build(),
        //new AuthUI.IdpConfig.GoogleBuilder().build());
        //new AuthUI.IdpConfig.FacebookBuilder().build(),
        //new AuthUI.IdpConfig.TwitterBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.buddy)
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }

    // [START auth_fui_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String authUserId = FirebaseAuth.getInstance().getUid();
                //ConstantClass.authUserId = authUserId;
                Log.d("FIREBASELOGIN", user.getEmail());
                if (user != null) {
                    UserStorage.storeActivity(UserStorage.LOGIN);
                    Intent intent = new Intent(FirebaseAuthActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
    // [END auth_fui_result]

    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_signout]
    }
}
