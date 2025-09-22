package advancedCommands.appium_self_healing.helpers;

import utils.ApplicationUploadUtil;

public class SelfHealingHelpers {

    private ApplicationUploadUtil uploadUtil;

    public SelfHealingHelpers() {
        this.uploadUtil = new ApplicationUploadUtil();
    }

    @Deprecated
    public void uploadApplicationApi(String filePath, String uniqueName) {
        uploadUtil.uploadApplicationApi(filePath, uniqueName);
    }
}