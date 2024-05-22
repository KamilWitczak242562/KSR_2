package com.example.ksr_2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LinguisticVariable<T> {
    private List<Label<T>> labels;
    private ClassicSet<T> universe;
    private String name;

}
