package com.example.ksr_2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ClassicSet<T> {
    private List<T> set;

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public void add(T value) {
        set.add(value);
    }

    public void remove(T value) {
        set.remove(value);
    }

    public boolean isIn(T value) {
        return set.contains(value);
    }
}
