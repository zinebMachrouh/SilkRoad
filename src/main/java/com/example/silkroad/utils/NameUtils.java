package com.example.silkroad.utils;

public class NameUtils {
    public static String getInitials(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return "";
        }
        String[] names = fullName.split(" ");
        StringBuilder initials = new StringBuilder();
        for (String name : names) {
            if (!name.isEmpty()) {
                initials.append(Character.toUpperCase(name.charAt(0)));
            }
        }
        return initials.toString();
    }
}
