package com.example.application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public interface IdChecker {
    default boolean checkId(String id, ArrayList<String > idArray){
        int count = 0;
        for (String tempId: idArray){
            if (tempId.equals(id)) {
                count++;
                if (count > 1)
                    return false;
            }
        }
        return true;
    }
}
