package utils;

import io.github.cdimascio.dotenv.Dotenv;

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