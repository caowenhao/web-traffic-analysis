package com.twq.parser.objectbuilder;

import com.twq.iplocation.IpLocationParser;
import com.twq.parser.dataobject.BaseDataObject;
import com.twq.parser.utils.ColumnReader;
import com.twq.preparse.PreParsedLog;
import eu.bitwalker.useragentutils.UserAgent;

import java.util.List;

public abstract class AbstractDataObjectBuilder {

    public abstract String getCommand();

    public abstract List<BaseDataObject> doBuildDataObjects(PreParsedLog preParsedLog);

    public void fillCommonBaseDataObjectValue(BaseDataObject baseDataObject,
                                              PreParsedLog preParsedLog, ColumnReader columnReader) {
        baseDataObject.setProfileId(preParsedLog.getProfileId());
        baseDataObject.setServerTimeString(preParsedLog.getServerTime().toString());

        baseDataObject.setUserId(columnReader.getStringValue("gsuid"));
        baseDataObject.setTrackerVersion(columnReader.getStringValue("gsver"));
        baseDataObject.setPvId(columnReader.getStringValue("pvid"));
        baseDataObject.setCommand(columnReader.getStringValue("gscmd"));

        baseDataObject.setClientIp(preParsedLog.getClientIp().toString());
        baseDataObject.setIpLocation(IpLocationParser.parse(preParsedLog.getClientIp().toString()));

        baseDataObject.setUserAgent(preParsedLog.getUserAgent().toString());
        baseDataObject.setUserAgentInfo(UserAgent.parseUserAgentString(preParsedLog.getUserAgent().toString()));
    }
}
