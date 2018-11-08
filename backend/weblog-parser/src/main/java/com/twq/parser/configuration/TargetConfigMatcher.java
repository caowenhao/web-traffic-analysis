package com.twq.parser.configuration;

import com.twq.parser.matches.UrlStringMatcher;

public class TargetConfigMatcher {
    private String key;
    private String targetName;
    private boolean isActive;

    private UrlStringMatcher urlStringMatcher;

    public TargetConfigMatcher(String key, String targetName, boolean isActive, UrlStringMatcher urlStringMatcher) {
        this.key = key;
        this.targetName = targetName;
        this.isActive = isActive;
        this.urlStringMatcher = urlStringMatcher;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean match(String url) {
        return urlStringMatcher.match(url);
    }
}
