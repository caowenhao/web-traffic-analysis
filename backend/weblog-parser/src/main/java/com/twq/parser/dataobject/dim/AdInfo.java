package com.twq.parser.dataobject.dim;

import com.twq.parser.utils.ParserUtils;


public class AdInfo {
    private String utmCampaign = "-";
    private String utmContent = "-";
    private String utmTerm = "-";
    private String utmSource = "-";
    private String utmMedium = "-";
    private String utmAdGroup = "-";
    private String utmChannel = "-";

    public boolean isPaid() {
        return !ParserUtils.isNullOrEmptyOrDash(utmCampaign) || !ParserUtils.isNullOrEmptyOrDash(utmContent) ||
                !ParserUtils.isNullOrEmptyOrDash(utmTerm) || !ParserUtils.isNullOrEmptyOrDash(utmSource) ||
                !ParserUtils.isNullOrEmptyOrDash(utmAdGroup) || !ParserUtils.isNullOrEmptyOrDash(utmChannel) ||
                !ParserUtils.isNullOrEmptyOrDash(utmMedium);
    }

    public String getUtmCampaign() {
        return utmCampaign;
    }

    public void setUtmCampaign(String utmCampaign) {
        this.utmCampaign = utmCampaign;
    }

    public String getUtmContent() {
        return utmContent;
    }

    public void setUtmContent(String utmContent) {
        this.utmContent = utmContent;
    }

    public String getUtmTerm() {
        return utmTerm;
    }

    public void setUtmTerm(String utmTerm) {
        this.utmTerm = utmTerm;
    }

    public String getUtmSource() {
        return utmSource;
    }

    public void setUtmSource(String utmSource) {
        this.utmSource = utmSource;
    }

    public String getUtmMedium() {
        return utmMedium;
    }

    public void setUtmMedium(String utmMedium) {
        this.utmMedium = utmMedium;
    }

    public String getUtmAdGroup() {
        return utmAdGroup;
    }

    public void setUtmAdGroup(String utmAdGroup) {
        this.utmAdGroup = utmAdGroup;
    }

    public String getUtmChannel() {
        return utmChannel;
    }

    public void setUtmChannel(String utmChannel) {
        this.utmChannel = utmChannel;
    }
}
