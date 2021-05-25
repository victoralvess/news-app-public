package com.github.victoralvess.newz.domain.entities;

import java.io.Serializable;

public class Source implements Serializable {
    private final String id;
    private final String name;

    public Source(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
