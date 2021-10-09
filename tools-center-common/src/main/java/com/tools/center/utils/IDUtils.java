package com.tools.center.utils;

import java.util.UUID;

public class IDUtils {

    public static String uuId(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
