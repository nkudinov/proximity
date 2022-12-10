package com.proximity.proximity.service;

import org.springframework.stereotype.Service;

@Service
public class GeoHashService {
    static String base32 = "0123456789bcdefghjkmnpqrstuvwxyz";

    double getMid(double[] range) {
        return range[0] + (range[1] - range[0]) / 2.0;
    }

    public String encode(double lat, double lon, int len) {
        boolean isEvenBit = true;
        double[] lonRange = new double[]{-180.0D, 180.0D};
        double[] latRange = new double[]{-90.0D, 90.0D};
        int val = 0;
        int cnt = 0;
        StringBuilder sb = new StringBuilder();
        while (sb.length() < len) {
            if (!isEvenBit) {
                double mid = getMid(latRange);
                if (lat >= mid) {

                    val = val * 2 + 1;
                    latRange[0] = mid;
                } else {
                    val *= 2;
                    latRange[1] = mid;
                }
            } else {
                double mid = getMid(lonRange);
                if (lon >= mid) {
                    val = val * 2 + 1;
                    lonRange[0] = mid;
                } else {
                    val *= 2;
                    lonRange[1] = mid;
                }
            }
            isEvenBit = !isEvenBit;
            cnt++;
            if (cnt == 5) {
                sb.append(base32.charAt(val));
                val = 0;
                cnt = 0;
            }
        }
        return sb.toString();
    }
}
