import pytest
from appium import webdriver
from appium.options.android import UiAutomator2Options
from appium.options.ios import XCUITestOptions
import os
from dotenv import load_dotenv

# Load environment variables from .env file
load_dotenv()


@pytest.fixture(scope="session")
def android_driver():
    """Android driver fixture for tests."""
    options = UiAutomator2Options()
    options.platform_name = "Android"
    options.automation_name = "UiAutomator2"
    
    # Common Android capabilities - override in individual tests as needed
    options.device_name = os.getenv("ANDROID_DEVICE_NAME", "Android Emulator")
    options.platform_version = os.getenv("ANDROID_PLATFORM_VERSION", "11")
    
    # Get cloud URL and add /wd/hub if not present
    cloud_url = os.getenv("CLOUD_URL", "http://localhost:4723")
    if not cloud_url.endswith("/wd/hub"):
        cloud_url += "/wd/hub"
    
    # Add cloud-specific capabilities if provided
    access_key = os.getenv("ACCESS_KEY")
    if access_key:
        options.set_capability("digitalai:accessKey", access_key)
        
    appium_version = os.getenv("APPIUM_VERSION")
    if appium_version:
        options.set_capability("digitalai:appiumVersion", appium_version)
    
    driver = webdriver.Remote(
        command_executor=cloud_url,
        options=options
    )
    
    yield driver
    
    driver.quit()


@pytest.fixture(scope="session")
def ios_driver():
    """iOS driver fixture for tests."""
    options = XCUITestOptions()
    options.platform_name = "iOS"
    options.automation_name = "XCUITest"
    
    # Common iOS capabilities - override in individual tests as needed
    options.device_name = os.getenv("IOS_DEVICE_NAME", "iPhone Simulator")
    options.platform_version = os.getenv("IOS_PLATFORM_VERSION", "15.0")
    
    # Get cloud URL and add /wd/hub if not present
    cloud_url = os.getenv("CLOUD_URL", "http://localhost:4723")
    if not cloud_url.endswith("/wd/hub"):
        cloud_url += "/wd/hub"
    
    # Add cloud-specific capabilities if provided
    access_key = os.getenv("ACCESS_KEY")
    if access_key:
        options.set_capability("digitalai:accessKey", access_key)
        
    appium_version = os.getenv("APPIUM_VERSION")
    if appium_version:
        options.set_capability("digitalai:appiumVersion", appium_version)
    
    driver = webdriver.Remote(
        command_executor=cloud_url,
        options=options
    )
    
    yield driver
    
    driver.quit()


@pytest.fixture
def test_data_path():
    """Path to test resources."""
    return os.path.join(os.path.dirname(__file__), "tests", "resources")


def pytest_configure(config):
    """Configure pytest markers."""
    config.addinivalue_line("markers", "android: mark test as Android-specific")
    config.addinivalue_line("markers", "ios: mark test as iOS-specific")
    config.addinivalue_line("markers", "slow: mark test as slow running")


def pytest_collection_modifyitems(config, items):
    """Auto-mark tests based on filename patterns."""
    for item in items:
        # Auto-mark tests based on filename
        if "android" in item.nodeid.lower():
            item.add_marker(pytest.mark.android)
        elif "ios" in item.nodeid.lower():
            item.add_marker(pytest.mark.ios)