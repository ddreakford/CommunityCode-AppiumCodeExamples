package advancedCommands.appium_self_healing.helpers;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONObject;
import utils.EnvironmentConfig;

public class SelfHealingHelpers {

    public SelfHealingHelpers() {}

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

    private String extractApiBaseUrl(String cloudUrl) {
        if (cloudUrl.endsWith("/wd/hub")) {
            return cloudUrl.substring(0, cloudUrl.length() - 7);
        }
        return cloudUrl;
    }
}