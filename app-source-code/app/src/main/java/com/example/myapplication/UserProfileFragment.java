package com.example.myapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.myapplication.data.LoginDataSource;
import com.example.myapplication.data.LoginRepository;
import com.example.myapplication.ui.login.LoginFormState;
import com.example.myapplication.ui.login.LoginViewModel;

import java.util.Objects;

public class UserProfileFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        findPreference("username").setTitle("Username: " + LoginRepository.getInstance(new LoginDataSource()).user.getDisplayName());

        System.out.println("userID" + LoginRepository.getInstance(new LoginDataSource()).user.getDisplayName());

        findPreference("logout").setDefaultValue(false);

        getPreferenceManager().findPreference("new_password").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                System.out.println("New suggested password is: " + newValue);

                if (!LoginViewModel.isPasswordValid((String) newValue)) {
                    Toast.makeText(
                            getContext().getApplicationContext(),
                            R.string.invalid_password,
                            Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("Setting password to: " + newValue);

                    changePassword((String) newValue);

                    Toast.makeText(
                            getContext().getApplicationContext(),
                            "Successfully Changed Password",
                            Toast.LENGTH_LONG).show();

                }

                return false;
            }
        });

        getPreferenceManager().findPreference("logout").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                System.out.println("Initiating logout");
                LoginRepository.getInstance(new LoginDataSource()).logout();

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(
                        getContext().getApplicationContext(),
                        "Logged Out",
                        Toast.LENGTH_LONG).show();
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.nav_browse_events);
                return false;
            }
        });
    }

    public void changePassword(String newPassd){
        return;
    }
}