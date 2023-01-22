package com.example.poc_media_play.auth;

import android.app.Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthAnonymous {

    private final FirebaseAuth mAuth;
    private FirebaseUser userAnonymous;

    public AuthAnonymous() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void signInAnonymous(Activity activity) {
        mAuth.signInAnonymously().addOnCompleteListener(activity, task -> {
            if (task.isSuccessful()) {
                userAnonymous = task.getResult().getUser();
            }
        });
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public FirebaseUser getUserAnonymous() {
        return userAnonymous;
    }
}
