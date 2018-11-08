package com.twq.parser.configuration.loader;

import com.twq.parser.configuration.SearchEngineConfig;

import java.util.List;

public interface SearchEngineConfigLoader {
    public List<SearchEngineConfig> getSearchEngineConfigs();
}
