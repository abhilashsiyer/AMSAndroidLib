package com.ams.amsandroidlib.helper;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Suite {
    private String suiteName;
    private ArrayList<String> testName;
    private String testResult;
}