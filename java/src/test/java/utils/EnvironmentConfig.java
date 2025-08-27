package utils;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class EnvironmentConfig {
    private static final Dotenv dotenv;
    
    static {
        try {
            dotenv = Dotenv.configure()
                .directory(".")
                .filename(".env")
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();
        } catch (Exception e) {
            System.err.println("Warning: Could not load .env file: " + e.getMessage());
            throw new RuntimeException("Failed to initialize environment configuration", e);
        }
    }
    
    public static String getCloudUrl() {
        String url = getEnvVar("CLOUD_URL", "http://localhost:4723");
        return url.endsWith("/wd/hub") ? url : url + "/wd/hub";
    }
    
    public static String getAccessKey() {
        return getEnvVar("ACCESS_KEY", "");
    }
    
    public static String getAppiumVersion() {
        return getEnvVar("APPIUM_VERSION", "2.0.0");
    }
    
    public static String getAndroidDeviceQuery() {
        return getDeviceQuery("android");
    }
    
    public static String getIOSDeviceQuery() {
        return getDeviceQuery("ios");
    }
    
    private static String getDeviceQuery(String platform) {
        // Check for custom complete query first
        String customQuery = getEnvVar(platform.toUpperCase() + "_DEVICE_QUERY", "");
        if (!customQuery.isEmpty()) {
            validateDeviceQuery(customQuery, platform);
            return customQuery;
        }
        
        // Build query from components
        List<String> queryParts = new ArrayList<>();
        
        // Base OS requirement (always included)
        queryParts.add("@os='" + platform + "'");
        
        // Optional model specification
        String model = getEnvVar(platform.toUpperCase() + "_DEVICE_MODEL", "");
        if (!model.isEmpty()) {
            queryParts.add("@model='" + model + "'");
        }
        
        // Optional OS version specification
        String osVersion = getEnvVar(platform.toUpperCase() + "_OS_VERSION", "");
        if (!osVersion.isEmpty()) {
            queryParts.add("@osVersion='" + osVersion + "'");
        }
        
        String query = String.join(" and ", queryParts);
        validateDeviceQuery(query, platform);
        return query;
    }
    
    private static void validateDeviceQuery(String query, String platform) {
        if (query == null || query.trim().isEmpty()) {
            throw new RuntimeException("Device query cannot be empty for platform: " + platform);
        }
        
        // Basic syntax validation
        if (!query.contains("@os=")) {
            throw new RuntimeException("Device query must contain '@os=' clause for platform: " + platform + 
                                     ". Invalid query: " + query);
        }
        
        // Check for unmatched quotes
        int singleQuoteCount = query.length() - query.replace("'", "").length();
        if (singleQuoteCount % 2 != 0) {
            throw new RuntimeException("Device query has unmatched quotes for platform: " + platform + 
                                     ". Invalid query: " + query);
        }
        
        // Validate basic structure patterns
        Pattern validPattern = Pattern.compile("^@\\w+='[^']*'(\\s+and\\s+@\\w+='[^']*')*$");
        if (!validPattern.matcher(query).matches()) {
            throw new RuntimeException("Device query has invalid syntax for platform: " + platform + 
                                     ". Query must follow pattern '@key='value'' [and @key2='value2']. Invalid query: " + query);
        }
        
        // Platform-specific validation
        String expectedOs = "@os='" + platform + "'";
        if (!query.contains(expectedOs)) {
            throw new RuntimeException("Device query must contain '" + expectedOs + "' for platform: " + platform + 
                                     ". Invalid query: " + query);
        }
        
        System.out.println("Device query validated for " + platform + ": " + query);
    }
    
    private static String getEnvVar(String key, String defaultValue) {
        // First check system environment variables
        String value = System.getenv(key);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }
        
        // Then check .env file
        if (dotenv != null) {
            value = dotenv.get(key);
            if (value != null && !value.trim().isEmpty()) {
                return value.trim();
            }
        }
        
        // Return default value
        return defaultValue;
    }
}