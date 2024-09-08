package com.noisevisionproduction.playmeetwebsite.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {

    private final FirebaseAuth firebaseAuth;

    @Autowired
    public FirebaseService(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public FirebaseToken verifyToken(String idToken) throws FirebaseAuthException {
        return firebaseAuth.verifyIdToken(idToken);
    }

    public UserRecord createUser(UserRecord.CreateRequest createRequest) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().createUser(createRequest);
    }
}
