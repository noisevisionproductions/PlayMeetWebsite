package com.noisevisionproduction.playmeetwebsite.service.validation;

import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class NicknameValidationService {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 30;
    private static final String REGEX_PATTERN = "^[a-zA-Z0-9]+$";

    private final FirebaseDatabase firebaseDatabase;

    @Autowired
    public NicknameValidationService(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public void validateNicknameFormat(String nickname) {
        if (nickname.length() < MIN_LENGTH || nickname.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Nazwa użytkownika musi mieć od " + MIN_LENGTH + " do " + MAX_LENGTH + " znaków.");
        } else if (!nickname.matches(REGEX_PATTERN)) {
            throw new IllegalArgumentException("Nazwa użytkownika może zawierać tylko litery i cyfry.");
        }
    }

    public CompletableFuture<Boolean> isNicknameAvailable(String nickname) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DatabaseReference userReference = firebaseDatabase.getReference().child("UserModel");

        Query query = userReference.orderByChild("nickname").equalTo(nickname);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    future.complete(false);
                } else {
                    future.complete(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(new RuntimeException("Błąd bazy danych Firebase: " + error.getMessage()));
            }
        });
        return future;
    }
}
