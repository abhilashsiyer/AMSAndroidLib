package com.ams.amsandroidlib.api;

import java.io.IOException;

public interface AMS {
    void launchApp(String pkgName);
    void clickByResourceID(String identifier);
    void clickByResourceID(String pkgName, String identifier);
    void clickByText(String textIdentifier);
    void verifyScreen(String tagName) throws IOException;
    void pageName(String text);
    void testName(String text);
    void end();
    void verifyValueAtID(String identifier, String value);
}
