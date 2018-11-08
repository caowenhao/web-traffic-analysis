package com.twq.parser.configuration.service;

import com.twq.parser.configuration.ReferUrlAndParams;
import com.twq.parser.configuration.SearchEngineConfig;
import com.twq.parser.configuration.loader.SearchEngineConfigLoader;
import com.twq.parser.configuration.loader.impl.FileSearchEngineConfigLoader;

import java.util.List;

public class SearchEngineConfigService {

    private static SearchEngineConfigService searchEngineConfigManager = new SearchEngineConfigService();

    private SearchEngineConfigLoader loader = new FileSearchEngineConfigLoader();

    private List<SearchEngineConfig> searchEngineConfigs = loader.getSearchEngineConfigs();

    private SearchEngineConfigService(){
    }

    public static SearchEngineConfigService getInstance() {
        return searchEngineConfigManager;
    }

    public SearchEngineConfig doMatch(ReferUrlAndParams referUrlAndParams) {
        for (SearchEngineConfig searchEngineConfig : searchEngineConfigs) {
            if (searchEngineConfig.match(referUrlAndParams)) {
                return searchEngineConfig;
            }
        }
        return null;
    }
}
