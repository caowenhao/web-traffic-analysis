package com.twq.parser.configuration.service;

import com.twq.metadata.model.TargetPage;
import com.twq.parser.configuration.TargetConfigMatcher;
import com.twq.parser.matches.MatchType;
import com.twq.parser.matches.UrlStringMatcher;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TargetPageConfigService {

    private Map<Integer, List<TargetPage>> targetPageMap = null;

    public TargetPageConfigService(Map<Integer, List<TargetPage>> targetPageMap) {
        this.targetPageMap = targetPageMap;
    }

    public List<TargetConfigMatcher> getByProfileId(int profileId) {
        List<TargetPage> targetPages = targetPageMap.getOrDefault(profileId, Collections.emptyList());

        List<TargetConfigMatcher> targetConfigMatchers = targetPages.stream().map(targetPage -> {
          return new TargetConfigMatcher(targetPage.getId(), targetPage.getName(), targetPage.isEnable(),
                  new UrlStringMatcher(MatchType.valueOf(targetPage.getMatchType()),
                          targetPage.getMatchPattern(), targetPage.isMatchWithoutQueryString()));
        }).collect(Collectors.toList());
        return targetConfigMatchers;
    }
}
