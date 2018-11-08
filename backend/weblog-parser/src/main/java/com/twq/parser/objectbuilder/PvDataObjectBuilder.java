package com.twq.parser.objectbuilder;

import com.twq.parser.configuration.TargetConfigMatcher;
import com.twq.parser.dataobject.BaseDataObject;
import com.twq.parser.dataobject.PvDataObject;
import com.twq.parser.dataobject.TargetPageDataObject;
import com.twq.parser.dataobject.dim.*;
import com.twq.parser.objectbuilder.helper.SearchEngineNameUtil;
import com.twq.parser.objectbuilder.helper.TargetPageAnalyzer;
import com.twq.parser.utils.ColumnReader;
import com.twq.parser.utils.ParserUtils;
import com.twq.parser.utils.UrlInfo;
import com.twq.parser.utils.UrlParseUtils;
import com.twq.preparse.PreParsedLog;
import sun.net.www.ParseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PvDataObjectBuilder extends AbstractDataObjectBuilder{

    private TargetPageAnalyzer targetPageAnalyzer;

    public PvDataObjectBuilder(TargetPageAnalyzer targetPageAnalyzer) {
        this.targetPageAnalyzer = targetPageAnalyzer;
    }

    @Override
    public String getCommand() {
        return "pv";
    }

    @Override
    public List<BaseDataObject> doBuildDataObjects(PreParsedLog preParsedLog) {
        List<BaseDataObject> dataObjects = new ArrayList<>();
        PvDataObject pvDataObject = new PvDataObject();
        ColumnReader columnReader = new ColumnReader(preParsedLog.getQueryString());
        fillCommonBaseDataObjectValue(pvDataObject, preParsedLog, columnReader);

        //1、网站信息的解析
        pvDataObject.setSiteResourceInfo(createSiteResourceInfo(columnReader));
        //2、广告信息的解析
        pvDataObject.setAdInfo(createAdInfo(pvDataObject.getSiteResourceInfo()));
        //3、浏览器信息的解析
        pvDataObject.setBrowserInfo(createBrowserInfo(columnReader));
        //4、来源信息的解析
        pvDataObject.setReferrerInfo(createReferrerInfo(columnReader));

        //6、来源类型和来源渠道的解析
        resolveReferrerDerivedColumns(pvDataObject.getReferrerInfo(), pvDataObject.getAdInfo());

        dataObjects.add(pvDataObject);

        //7、目标页面数据对象的解析
        TargetPageDataObject targetPageDataObject = populateTargetPageObject(preParsedLog, pvDataObject, columnReader);
        if (targetPageDataObject != null) {
            dataObjects.add(targetPageDataObject);
        }

        return dataObjects;
    }

    private TargetPageDataObject populateTargetPageObject(PreParsedLog preParsedLog, PvDataObject pvDataObject, ColumnReader columnReader) {
        List<TargetConfigMatcher> targetConfigMatchers =
                targetPageAnalyzer.getTargetHits(preParsedLog.getProfileId(), pvDataObject.getSiteResourceInfo().getUrl());
        if (targetConfigMatchers != null && !targetConfigMatchers.isEmpty()) {
            List<TargetPageInfo> targetPageInfos =
                    targetConfigMatchers.stream()
                    .map(tm -> new TargetPageInfo(tm.getKey(), tm.getTargetName(), tm.isActive()))
                    .collect(Collectors.toList());

            TargetPageDataObject targetPageDataObject = new TargetPageDataObject();
            fillCommonBaseDataObjectValue(targetPageDataObject, preParsedLog, columnReader);

            targetPageDataObject.setTargetPageInfos(targetPageInfos);
            targetPageDataObject.setPvDataObject(pvDataObject);
            return targetPageDataObject;
        }
        return null;
    }

    private void resolveReferrerDerivedColumns(ReferrerInfo referrerInfo, AdInfo adInfo) {
        String adChannel = adInfo.getUtmChannel();
        if (!ParserUtils.isNullOrEmptyOrDash(adChannel)) {
            referrerInfo.setChannel(adChannel);
        } else if (!ParserUtils.isNullOrEmptyOrDash(referrerInfo.getSearchEngineName())) {
            referrerInfo.setChannel(referrerInfo.getSearchEngineName());
        } else {
            referrerInfo.setChannel(referrerInfo.getDomain());
        }

        if (!ParserUtils.isNullOrEmptyOrDash(referrerInfo.getSearchEngineName())){
            if (adInfo.isPaid()) {
                referrerInfo.setReferType("paid search");
            } else {
                referrerInfo.setReferType("organic search");
            }
        } else if (!ParserUtils.isNullOrEmptyOrDash(referrerInfo.getDomain())) {
            referrerInfo.setReferType("referral");
        } else {
            referrerInfo.setReferType("direct");
        }

    }
    private SiteResourceInfo createSiteResourceInfo(ColumnReader columnReader) {
        SiteResourceInfo siteResourceInfo = new SiteResourceInfo();
        siteResourceInfo.setPageTitle(columnReader.getStringValue("gstl"));
        String gsurl = columnReader.getStringValue("gsurl");
        UrlInfo urlInfo = UrlParseUtils.getInfoFromUrl(gsurl);
        siteResourceInfo.setUrl(urlInfo.getFullUrl());
        siteResourceInfo.setDomain(urlInfo.getDomain());
        siteResourceInfo.setQuery(urlInfo.getQuery());
        siteResourceInfo.setOriginalUrl(columnReader.getStringValue("gsorurl"));
        return siteResourceInfo;
    }

    private AdInfo createAdInfo(SiteResourceInfo siteResourceInfo) {
        Map<String, String> landingParams = UrlParseUtils.getQueryParams(siteResourceInfo.getQuery());
        AdInfo adInfo = new AdInfo();
        adInfo.setUtmCampaign(landingParams.getOrDefault("utm_campaign", "-"));
        adInfo.setUtmMedium(landingParams.getOrDefault("utm_medium", "-"));
        adInfo.setUtmContent(landingParams.getOrDefault("utm_content", "-"));
        adInfo.setUtmChannel(landingParams.getOrDefault("utm_channel", "-"));
        adInfo.setUtmTerm(landingParams.getOrDefault("utm_term", "-"));
        adInfo.setUtmSource(landingParams.getOrDefault("utm_source", "-"));
        adInfo.setUtmAdGroup(landingParams.getOrDefault("utm_adgroup", "-"));
        return adInfo;
    }

    private BrowserInfo createBrowserInfo(ColumnReader columnReader) {
        BrowserInfo browserInfo = new BrowserInfo();
        browserInfo.setAlexaToolBar(ParserUtils.parseBoolean(columnReader.getStringValue("gsalexaver")));
        browserInfo.setBrowserLanguage(columnReader.getStringValue("gsbrlang"));
        browserInfo.setColorDepth(columnReader.getStringValue("gsclr"));
        browserInfo.setCookieEnable(ParserUtils.parseBoolean(columnReader.getStringValue("gsce")));
        browserInfo.setDeviceName(columnReader.getStringValue("dvn"));
        browserInfo.setDeviceType(columnReader.getStringValue("dvt"));
        browserInfo.setFlashVersion(columnReader.getStringValue("gsflver"));
        browserInfo.setJavaEnable(ParserUtils.parseBoolean(columnReader.getStringValue("gsje")));
        browserInfo.setOsLanguage(columnReader.getStringValue("gsoslang"));
        browserInfo.setResolution(columnReader.getStringValue("gsscr"));
        browserInfo.setSilverlightVersion(columnReader.getStringValue("gssil"));
        return browserInfo;
    }

    private ReferrerInfo createReferrerInfo(ColumnReader columnReader) {
        ReferrerInfo referrerInfo = new ReferrerInfo();
        String referUrl = columnReader.getStringValue("gsref");
        if (referUrl == "-") {
            referrerInfo.setChannel("-");
            referrerInfo.setDomain("-");
            referrerInfo.setEqId("-");
            referrerInfo.setSearchEngineName("-");
            referrerInfo.setUrl("-");
            referrerInfo.setQuery("-");
            referrerInfo.setUrlWithoutQuery("-");
            referrerInfo.setKeyword("-");
        } else {
            UrlInfo urlInfo = UrlParseUtils.getInfoFromUrl(referUrl);
            referrerInfo.setDomain(urlInfo.getDomain());
            referrerInfo.setUrl(urlInfo.getFullUrl());
            referrerInfo.setQuery(urlInfo.getQuery());
            referrerInfo.setUrlWithoutQuery(urlInfo.getUrlWithoutQuery());

            SearchEngineNameUtil.populateSearchEngineInfoFromRefUrl(referrerInfo);
        }
        return referrerInfo;
    }
}
