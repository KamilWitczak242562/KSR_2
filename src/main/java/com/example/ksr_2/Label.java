package com.example.ksr_2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Label<T> {
    private FuzzySet<T> fuzzySet;
    private String labelName;

    public double getMembership(double value) {
        return 0;
    }
}
