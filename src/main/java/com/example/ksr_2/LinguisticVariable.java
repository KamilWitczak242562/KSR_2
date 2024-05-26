package com.example.ksr_2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LinguisticVariable {
    private List<Label> labels;
    private ClassicSet universe;
    private String name;

}
