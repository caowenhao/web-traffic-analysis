package com.twq.parser.dataobject.dim;

public class TargetPageInfo {
    private String key;
    private String targetName;
    private boolean isActive;

    public TargetPageInfo(String key, String targetName, boolean isActive) {
        this.key = key;
        this.targetName = targetName;
        this.isActive = isActive;
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


}
