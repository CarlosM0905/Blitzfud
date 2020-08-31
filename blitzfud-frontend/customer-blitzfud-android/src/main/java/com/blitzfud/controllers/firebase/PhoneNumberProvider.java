package com.blitzfud.controllers.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PhoneNumberProvider {
    DatabaseReference databaseReference;

    public PhoneNumberProvider(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Client");
    }

    public Task<Void> create(String phoneNumber){
        Map<String, Object> map = new HashMap<>();
        map.put("role", "USER");

        return databaseReference.child(phoneNumber).setValue(map);
    }

    public DatabaseReference getPhoneNumber(String phoneNUmber){
        return databaseReference.child(phoneNUmber);
    }
}
