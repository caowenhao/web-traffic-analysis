package com.twq.parser.objectbuilder.helper;

import com.twq.parser.configuration.TargetConfigMatcher;
import com.twq.parser.configuration.loader.ProfileConfigLoader;
import com.twq.parser.configuration.service.TargetPageConfigService;

import java.util.ArrayList;
import java.util.List;

public class TargetPageAnalyzer {
    private TargetPageConfigService targetPageConfigService;

    public TargetPageAnalyzer(ProfileConfigLoader profileConfigLoader) {
        this.targetPageConfigService = new TargetPageConfigService(profileConfigLoader.getTargetPages());
    }

    public List<TargetConfigMatcher> getTargetHits(int profileId, String pvUrl) {
        List<TargetConfigMatcher> targetConfigMatchers = this.targetPageConfigService.getByProfileId(profileId);

        List<TargetConfigMatcher> targetConfigHits = new ArrayList<>();
        for (TargetConfigMatcher targetConfigMatcher : targetConfigMatchers) {
            if (targetConfigMatcher.match(pvUrl)) {
                targetConfigHits.add(targetConfigMatcher);
            }
        }
        return targetConfigHits;
    }
}
