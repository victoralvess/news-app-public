package com.github.victoralvess.newz.activities.helpers;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

public class InMemoryState {
    private final Map<Integer, Bundle> state;
    private static InMemoryState instance = null;

    private InMemoryState() {
        state = new HashMap<>();
    }

    public static InMemoryState getInstance() {
        if (instance == null) {
            instance = new InMemoryState();
        }

        return instance;
    }

    public Bundle getState(Integer id) {
        return state.get(id);
    }

    public void setState(Integer id, Bundle bundle) {
        state.put(id, bundle);
    }
}
