package com.twq.parser.utils;


import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static com.twq.parser.utils.ParserUtils.isNullOrEmptyOrDash;

public class UrlParseUtils {

    public static String decode(String encodedStr) {
        if (isNullOrEmptyOrDash(encodedStr)) {
            return encodedStr;
        }
        String decodeStr = "-";
        try {
            decodeStr = decodeTwice(encodedStr);
        } catch (Exception e) {
            e.printStackTrace();

            int lastPercentIndex = encodedStr.lastIndexOf("%");
            if (encodedStr.length() - lastPercentIndex == 2) {
                try {
                    decodeStr = decodeTwice(encodedStr.substring(0, lastPercentIndex));
                } catch (Exception e1) {
                    e1.printStackTrace();
                    return "-";
                }
            }
        }
        return decodeStr;
    }

    private static String decodeTwice(String str) throws Exception {
        String decodeStr = URLDecoder.decode(str, "utf-8");

        if (decodeStr.indexOf("%") > 0) {
            decodeStr = URLDecoder.decode(decodeStr, "utf-8");
        }
        return decodeStr;
    }

    public static UrlInfo getInfoFromUrl(String url) {
        String trimedUrl = url.trim();
        if (isNullOrEmptyOrDash(trimedUrl)) {
            return new UrlInfo("-", "-", "-", "-", "-", "-");
        } else {
            int firstQuestionMarkIndex = trimedUrl.indexOf("?");
            int firstPoundMarkIndex = trimedUrl.indexOf("#");
            try {
                URI uri = new URI(trimedUrl).normalize();
                int port = uri.getPort();
                String hostport;
                if (port != -1) {
                    hostport = uri.getHost() + ":" + port;
                } else {
                    hostport = uri.getHost();
                }
                String query;
                String fragment;
                if (firstPoundMarkIndex > 0 && firstQuestionMarkIndex > firstPoundMarkIndex) {
                    query = trimedUrl.substring(firstQuestionMarkIndex + 1);
                    fragment = trimedUrl.substring(firstPoundMarkIndex + 1, firstQuestionMarkIndex);
                } else {
                    query = uri.getRawQuery();
                    fragment = uri.getRawFragment();
                }
                return new UrlInfo(trimedUrl, uri.getScheme(), hostport, uri.getRawPath(), query, fragment);
            } catch (URISyntaxException e){
                try {
                    if (firstQuestionMarkIndex == -1) {
                        return parseUrlWithoutQuery(trimedUrl, firstPoundMarkIndex, false);
                    } else {
                        return parseUrlWithQuery(trimedUrl, firstQuestionMarkIndex, firstPoundMarkIndex);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    return new UrlInfo("-","-","-","-","-","-");
                }
            }
        }
    }

    private static UrlInfo parseUrlWithQuery(String url, int firstQuestionMarkIndex, int firstPoundMarkIndex) {
        QueryAndFragment queryAndFragment = getQueryAndFragment(url, firstQuestionMarkIndex, firstPoundMarkIndex);
        String urlWithoutQuery = url.substring(0, firstQuestionMarkIndex);
        UrlInfo uriInfo = parseUrlWithoutQuery(urlWithoutQuery, firstPoundMarkIndex, true);
        return new UrlInfo(url, uriInfo.getScheme(), uriInfo.getDomain(),
                uriInfo.getPath(), queryAndFragment.getQuery(), queryAndFragment.getFrament());
    }

    private static QueryAndFragment getQueryAndFragment(String url, int firstQuestionMarkIndex, int firstPoundMarkIndex) {
        if (firstQuestionMarkIndex > 0) {
            if (firstQuestionMarkIndex > firstPoundMarkIndex) {
                return new QueryAndFragment(url.substring(firstQuestionMarkIndex + 1), url.substring(firstPoundMarkIndex + 1, firstQuestionMarkIndex));
            } else {
                return new QueryAndFragment(url.substring(firstQuestionMarkIndex + 1, firstPoundMarkIndex), url.substring(firstPoundMarkIndex + 1));
            }
        } else {
            return new QueryAndFragment(url.substring(firstQuestionMarkIndex + 1), "");
        }
    }

    private static UrlInfo parseUrlWithoutQuery(String trimedUrl, int firstPoundMarkIndex, boolean hasQuestionMark) {
        String decoderUrl = decode(trimedUrl);
        int colonIndex = decoderUrl.indexOf(":");
        String scheme = decoderUrl.substring(0, colonIndex);
        String hostport = decoderUrl.substring(colonIndex + 3, decoderUrl.indexOf("/", colonIndex + 3));
        String path = decoderUrl.substring(decoderUrl.indexOf("/", colonIndex + 3));
        String fragment = "-";
        if (firstPoundMarkIndex > 0 && !hasQuestionMark) fragment = trimedUrl.substring(firstPoundMarkIndex + 1);
        return new UrlInfo(trimedUrl, scheme, hostport, path, "-", fragment);
    }

    public static Map<String, String> getQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (ParserUtils.isNullOrEmptyOrDash(query)) {
            return params;
        }
        String[] temps = query.split("&");
        for (String str : temps) {
            String[] kv = str.split("=");
            if (kv.length == 2) {
                params.put(kv[0], kv[1]);
            } else if (kv.length == 1) {
                params.put(kv[0], "-");
            }
        }
        return params;
    }
    public static void main(String[] args) {
        String str = "https%3A%2F%2Fwww.underarmour.cn%2F%3Futm_source%3Dbaidu%26utm_term%3D%25E6%25A0%2587%25E9%25A2%2598%26utm_medium%3DBrandZonePC%26utm_channel%3DSEM";
        String str2 = "https://www.underarmour.cn/?utm_source=baidu&utm_term=标题&utm_medium=BrandZonePC&utm_channel=SEM#32";
        System.out.println(decode(str));
        System.out.println(decode(" "));
        System.out.println(getInfoFromUrl(str).getDomain());
        System.out.println(getInfoFromUrl(str).getFragment());
        System.out.println(getInfoFromUrl(str).getPath());
        System.out.println(getInfoFromUrl(str).getPathQueryFragment());
        System.out.println(getInfoFromUrl(str).getQuery());
        System.out.println(getInfoFromUrl(str).getScheme());
        System.out.println(getInfoFromUrl(str).getUrlWithoutQuery());
        System.out.println("fullurl=====>" + getInfoFromUrl(str).getFullUrl());
        System.out.println(getInfoFromUrl(" ").getDomain());
    }
}
