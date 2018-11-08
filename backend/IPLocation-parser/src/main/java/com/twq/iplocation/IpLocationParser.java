package com.twq.iplocation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.*;

public class IpLocationParser {

    private static Map<IpRange, Long> ip2CityIdMap = new HashMap<>();
    private static Map<String, String> regionCode2RegionName = new HashMap<>();
    private static Map<Long, IpLocation> cityId2CityMap = new HashMap<>();

    static {
        try {
            InputStream cityBlocks = IpLocationParser.class.getClassLoader()
                    .getResourceAsStream("GeoLiteCity-Blocks.csv");
            BufferedReader cityBlocksReader = new BufferedReader(new InputStreamReader(cityBlocks));
            String line = null;
            while ((line = cityBlocksReader.readLine()) != null) {
                if (line.startsWith("#")){
                    continue;
                }
                String[] temps = line.replace("\"","").split(",");

                ip2CityIdMap.put(new IpRange(Long.parseLong(temps[0]), Long.parseLong(temps[1])), Long.parseLong(temps[2]));
            }

            InputStream regionCodes = IpLocationParser.class.getClassLoader()
                    .getResourceAsStream("region_codes.csv");
            BufferedReader regionCodesReader = new BufferedReader(new InputStreamReader(regionCodes));
            while ((line = regionCodesReader.readLine()) != null) {
                if (line.startsWith("#")){
                    continue;
                }
                String[] temps = line.replace("\"", "").split(",");
                regionCode2RegionName.put(temps[0] + "-" + temps[1], temps[2]);
            }

            InputStream cityLocation = IpLocationParser.class.getClassLoader()
                    .getResourceAsStream("GeoLiteCity-Location.csv");
            BufferedReader cityLocationReader = new BufferedReader(new InputStreamReader(cityLocation));
            while ((line = cityLocationReader.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                String[] temps = line.replace("\"", "").split(",");
                IpLocation ipLocation = new IpLocation();
                ipLocation.setCountry(temps[1]);
                ipLocation.setRegion(regionCode2RegionName.getOrDefault(temps[1] + "-" + temps[2], "-"));
                ipLocation.setCity(temps[3]);
                ipLocation.setPostalCode(temps[4]);
                ipLocation.setLatitude(temps[5]);
                ipLocation.setLongitude(temps[6]);
                cityId2CityMap.put(Long.parseLong(temps[0]), ipLocation);
            }
        } catch (Exception e) {
            throw new RuntimeException("init ip database error", e);
        }
    }

    public static IpLocation parse(String ip) {
        Long score = ip2Score(ip);
        for (Map.Entry<IpRange, Long> entry: ip2CityIdMap.entrySet() ) {
            if (score >= entry.getKey().getStartIp() && score <= entry.getKey().getEndIp()) {
                return cityId2CityMap.get(entry.getValue());
            }
        }
        return null;
    }

    public static Long ip2Score(String ip){
        String[] temps = ip.split("\\.");
        Long score = 256 * 256 * 256 * Long.parseLong(temps[0]) +
                256 * 256 * Long.parseLong(temps[1]) +
                256 * Long.parseLong(temps[2]) + Long.parseLong(temps[3]);
        return score;
    }
}
