package com.laxmifinance.util;

import jakarta.servlet.http.HttpServletRequest;

public final class BrowserParser {

    private BrowserParser() {
    }

    public static String describe(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null || userAgent.isBlank()) {
            return " ";
        }
        try {
            if (userAgent.contains("Firefox")) {
                return "Mozilla FireFox " + partAfter(userAgent, "Firefox/");
            }
            if (userAgent.contains("Chrome")) {
                return "Google Chrome " + firstToken(partAfter(userAgent, "Chrome/"));
            }
            if (userAgent.contains("MSIE")) {
                return "Internet Explorer " + firstToken(partAfter(userAgent, "MSIE "));
            }
            if (userAgent.contains("Safari")) {
                if (userAgent.contains("Maxthon")) {
                    return "Maxthon " + partAfter(userAgent, " Maxthon/");
                }
                return "Apple Safari " + partBefore(partAfter(userAgent, "Version/"), " Safari/");
            }
            if (userAgent.contains("Opera Mobi") || userAgent.contains("Opera")) {
                String version = partAfter(userAgent, "Version/");
                return version.isEmpty() ? " " : "Opera " + version;
            }
        } catch (RuntimeException e) {
            return " ";
        }
        return " ";
    }

    public static String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-FORWARDED-FOR");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private static String partAfter(String value, String token) {
        int index = value.indexOf(token);
        if (index < 0) {
            return "";
        }
        return value.substring(index + token.length());
    }

    private static String partBefore(String value, String token) {
        int index = value.indexOf(token);
        return index < 0 ? value : value.substring(0, index);
    }

    private static String firstToken(String value) {
        return value.split(" ")[0];
    }
}
