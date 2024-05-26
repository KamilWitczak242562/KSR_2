package com.example.ksr_2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ClassicSet {
    private List<Double> set;

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public void add(double value) {
        set.add(value);
    }

    public void remove(double value) {
        set.remove(value);
    }

    public boolean isIn(double value) {
        return set.contains(value);
    }

}
