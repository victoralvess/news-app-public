package com.github.victoralvess.newz.adapters;

import android.content.SharedPreferences;

import com.github.victoralvess.newz.domain.entities.User;
import com.github.victoralvess.newz.domain.port.UserRepository;

public class SharedPreferencesUserRepository implements UserRepository {
    final private SharedPreferences sharedPref;
    final String subjectKey;
    private User user = null;

    public SharedPreferencesUserRepository(SharedPreferences sharedPref, String subjectKey) {
        this.sharedPref = sharedPref;
        this.subjectKey = subjectKey;
    }

    @Override
    public User getUser() {
        if (user == null)
            user = new User(null);

        user.setSubject(sharedPref.getString(subjectKey, null));

        return user;
    }

    @Override
    public void saveSubject(String subject) {
        User user = getUser();
        user.setSubject(subject);

        sharedPref.edit().putString(subjectKey, subject).apply();
    }
}
