package utils;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONObject;

public class ApplicationUploadUtil {

    public static final String SEETEST_UNMODIFIED = "seetest-unmodified";
    public static final String SEETEST_MODIFIED = "seetest-modified";
    public static final String SEETEST_FULLNAME = "cloud:com.experitest.ExperiBank";

    public ApplicationUploadUtil() {}

    public void uploadApplicationApi(String filePath, String uniqueName) {
        try {
            String cloudUrl = EnvironmentConfig.getCloudUrl();
            String apiBaseUrl = extractApiBaseUrl(cloudUrl);

            HttpResponse<String> response = Unirest.post(apiBaseUrl + "/api/v1/applications/new-from-url")
                    .header("Authorization", "Bearer " + EnvironmentConfig.getAccessKey())
                    .field("url", filePath)
                    .field("uniqueName", uniqueName)
                    .asString();

            JSONObject responseBody = new JSONObject(response.getBody());

            if (!responseBody.has("message")) {
                System.out.println("uploadApplicationApi() - Android APK Build Uploaded to the Digital.ai Continuous Testing Platform.");
            } else {
                String message = responseBody.getString("message");

                if ("Application already exists".equals(message)) {
                    System.out.println("uploadApplicationApi() - Android APK Build Already exists. Avoiding Upload of Duplicate Application.");
                } else {
                    try {
                        System.out.println("uploadApplicationApi() - " + message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("uploadApplicationApi() - Error uploading application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void uploadUnmodifiedApplication() {
        String unmodifiedUrl = EnvironmentConfig.getUnmodifiedBuildUrl();
        System.out.println(String.format("Uploading APK for unmodified: %s", unmodifiedUrl));
        uploadApplicationApi(unmodifiedUrl, SEETEST_UNMODIFIED);
    }

    public void uploadModifiedApplication() {
        String modifiedUrl = EnvironmentConfig.getModifiedBuildUrl();
        System.out.println(String.format("Uploading APK for modified: %s", modifiedUrl));
        uploadApplicationApi(modifiedUrl, SEETEST_MODIFIED);
    }

    public void uploadApplicationByType(String applicationType) {
        if (SEETEST_UNMODIFIED.equalsIgnoreCase(applicationType)) {
            uploadUnmodifiedApplication();
        } else if (SEETEST_MODIFIED.equalsIgnoreCase(applicationType)) {
            uploadModifiedApplication();
        } else {
            System.err.println("uploadApplicationByType() - Unknown application type: " + applicationType +
                             ". Supported types: " + SEETEST_UNMODIFIED + ", " + SEETEST_MODIFIED);
        }
    }

    private String extractApiBaseUrl(String cloudUrl) {
        if (cloudUrl.endsWith("/wd/hub")) {
            return cloudUrl.substring(0, cloudUrl.length() - 7);
        }
        return cloudUrl;
    }
}