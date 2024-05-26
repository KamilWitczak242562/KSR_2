package com.example.ksr_2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OneSummary {
    private String summary;
    private Measures measures;
    private double quality;

    public OneSummary(String summary, Measures measures) {
        this.summary = summary;
        this.measures = measures;
        this.quality = measures.getT1degreeOfTruth();
    }

    @Override
    public String toString() {
        return summary + "[" + measures + "]";
    }
}
