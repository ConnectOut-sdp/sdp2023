package com.sdpteam.connectout.common;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

public class EntityID {

    protected final String value;

    protected EntityID(String value) {
        this.value = requireNonNull(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return value.equals(((EntityID) o).value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
