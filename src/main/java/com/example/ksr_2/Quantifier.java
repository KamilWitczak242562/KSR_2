package com.example.ksr_2;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Quantifier extends Label{
    private boolean isAbsolute;
    public Quantifier(FuzzySet fuzzySet, String labelName, boolean isAbsolute) {
        super(fuzzySet, labelName);
        this.isAbsolute = isAbsolute;
    }

}
