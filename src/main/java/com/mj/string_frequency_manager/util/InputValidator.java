package com.mj.string_frequency_manager.util;

import java.util.List;
import java.util.Map;

public class InputValidator {

    public static boolean isInvalid(List page){
        return page == null ||  page.isEmpty();
    }

    public static boolean isInvalid(Boolean bool){
        return bool == null ||  bool == false;
    }

    public static boolean isNull(Object object){
        return object == null;
    }

    public static boolean isInvalid(String sort) {
        return sort == null || sort.isEmpty();
    }

    public static boolean isInvalid(String[] data) {
        return data == null || data.length < 1;
    }

    public static boolean isInvalid(Map map) {
        return map == null || map.size() < 1;
    }

    public static boolean isInvalid(Object object) {
        return object == null;
    }

}
