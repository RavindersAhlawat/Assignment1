package com.ravinder.taskproject;

public class StaticClass {
    public static String nullChecker(String string) {

        if (string == null) {
            string = "";
        }
        return string;
    }
}
