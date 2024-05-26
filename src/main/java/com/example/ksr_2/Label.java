package com.example.ksr_2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Label {
    private FuzzySet fuzzySet;
    private String labelName;
    private String linguisticVariable;
    public double getMembership(double value) {
        return fuzzySet.getMembership(value);
    }

    public Label(FuzzySet fuzzySet, String labelName) {
        this.fuzzySet = fuzzySet;
        this.labelName = labelName;
    }

    @Override
    public String toString() {
        return labelName;
    }
}
