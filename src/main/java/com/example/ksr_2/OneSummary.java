package com.example.ksr_2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OneSummary {
    private String summary;
    private double membership;

    @Override
    public String toString() {
        return summary + "[" + membership + "]";
    }
}
