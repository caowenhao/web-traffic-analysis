package com.twq.parser.utils;

import java.util.HashMap;
import java.util.Map;

public class ColumnReader {
    private Map<String, String> keyvalues = new HashMap<>();

    public ColumnReader(String line) {
        String[] temps = line.split("&");
        for (String kvStr : temps) {
            String[] kv = kvStr.split("=");
            if (kv.length == 2) {
                keyvalues.put(kv[0], kv[1]);
            }
        }
    }
    public String getStringValue(String key) {
        return UrlParseUtils.decode(keyvalues.getOrDefault(key, "-"));
    }
}
