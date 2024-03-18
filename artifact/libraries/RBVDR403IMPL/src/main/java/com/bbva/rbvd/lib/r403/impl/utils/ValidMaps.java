package com.bbva.rbvd.lib.r403.impl.utils;

import java.util.Map;

public class ValidMaps {
    public static boolean mapIsNullOrEmpty(Map<?,?> mapa){
        return mapa == null || mapa.isEmpty();
    }
}
