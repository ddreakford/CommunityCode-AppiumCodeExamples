"""iOS Quick Start Test - Python equivalent of IOSQuickStartTest.java"""
import pytest
from appium import webdriver
from appium.options.ios import XCUITestOptions


@pytest.mark.ios
class TestIOSQuickStart:
    """iOS quick start test examples."""
    
    @pytest.fixture(autouse=True)
    def setup_driver(self):
        """Set up iOS driver for each test."""
        options = XCUITestOptions()
        options.platform_name = "iOS"
        options.automation_name = "XCUITest"
        options.device_name = "iPhone Simulator"
        
        self.driver = webdriver.Remote(
            command_executor="http://localhost:4723",
            options=options
        )
        
        yield
        
        self.driver.quit()
    
    def test_basic_ios_interaction(self):
        """Basic iOS interaction test."""
        # This is a template - implement specific test logic
        assert self.driver is not None
        
        # Example: Get device info
        device_name = self.driver.capabilities.get("deviceName")
        assert device_name is not None
    
    def test_ios_app_launch(self):
        """Test iOS app launch."""
        # This is a template - implement specific app launch logic
        assert self.driver is not None
        
        # Example: Verify driver session is active
        session_id = self.driver.session_id
        assert session_id is not None