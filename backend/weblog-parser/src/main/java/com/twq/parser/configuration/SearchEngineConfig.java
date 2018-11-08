package com.twq.parser.configuration;

import com.twq.parser.configuration.ReferUrlAndParams;
import com.twq.parser.matches.MatchType;
import com.twq.parser.matches.StringMatcher;

public class SearchEngineConfig {
    private int keyId;
    private String searchEngineName;
    private String regexUrl;
    private String searchKeywordKey;

    private StringMatcher stringMatcher;

    public SearchEngineConfig(int keyId, String searchEngineName, String regexUrl, String searchKeywordKey) {
        this.keyId = keyId;
        this.searchEngineName = searchEngineName;
        this.regexUrl = regexUrl;
        this.searchKeywordKey = searchKeywordKey;
        this.stringMatcher = new StringMatcher(MatchType.REGEX_MATCH, this.regexUrl);
    }

    public int getKeyId() {
        return keyId;
    }

    public String getSearchEngineName() {
        return searchEngineName;
    }

    public String getSearchKeywordKey() {
        return searchKeywordKey;
    }

    public StringMatcher getStringMatcher() {
        return stringMatcher;
    }

    public boolean match(ReferUrlAndParams referUrlAndParams) {
        if (stringMatcher.match(referUrlAndParams.getReferUrlWithoutQuery())) {
            if (searchKeywordKey != null) {
                return referUrlAndParams.getParams().containsKey(searchKeywordKey);
            } else {
                return true;
            }
        }
        return false;
    }
}
