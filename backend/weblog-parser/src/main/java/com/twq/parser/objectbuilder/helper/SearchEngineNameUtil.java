package com.twq.parser.objectbuilder.helper;

import com.twq.parser.configuration.ReferUrlAndParams;
import com.twq.parser.configuration.SearchEngineConfig;
import com.twq.parser.configuration.service.SearchEngineConfigService;
import com.twq.parser.dataobject.dim.ReferrerInfo;
import com.twq.parser.utils.UrlParseUtils;

import java.util.Map;

public class SearchEngineNameUtil {
    private static SearchEngineConfigService searchEngineConfigService = SearchEngineConfigService.getInstance();

    public static void populateSearchEngineInfoFromRefUrl(ReferrerInfo referrerInfo) {
        Map<String, String> referParams = UrlParseUtils.getQueryParams(referrerInfo.getQuery());
        ReferUrlAndParams referUrlAndParams = new ReferUrlAndParams(referrerInfo.getUrlWithoutQuery(), referParams);
        SearchEngineConfig searchEngineConfig = searchEngineConfigService.doMatch(referUrlAndParams);

        if (searchEngineConfig != null) {
            referrerInfo.setSearchEngineName(searchEngineConfig.getSearchEngineName());
            if (searchEngineConfig.getSearchKeywordKey() != null) {
                String keyword = referParams.getOrDefault(searchEngineConfig.getSearchKeywordKey(), "-");
                referrerInfo.setKeyword(keyword);
            }
        } else {
            referrerInfo.setSearchEngineName("-");
            referrerInfo.setKeyword("-");
        }

        if (referrerInfo.getQuery() != "-" && referrerInfo.getSearchEngineName().equalsIgnoreCase("baidu")) {
            referrerInfo.setEqId(referParams.getOrDefault("eqid", "-"));
        } else {
            referrerInfo.setEqId("-");
        }
    }
}
