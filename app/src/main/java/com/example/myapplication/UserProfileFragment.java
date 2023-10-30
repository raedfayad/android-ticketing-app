package com.example.myapplication;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.myapplication.data.LoginDataSource;
import com.example.myapplication.data.LoginRepository;

public class UserProfileFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

//        LoginRepository.getInstance(new LoginDataSource()).login("raed", "test");
        System.out.println("userID" + LoginRepository.getInstance(new LoginDataSource()).user.getDisplayName());

        findPreference("signature").setTitle(LoginRepository.getInstance(new LoginDataSource()).user.getDisplayName());
    }
}