package com.ams.amsandroidlib;

import java.io.IOException;

public interface AMS {
    void initialise();
    void waitForApp();
    void clickByResourceID();
    void clickText(String text);
    void verifyScreen(String tagName) throws IOException;
    void pageName(String text);
    void testName(String text);
    void end();
    void verifyValueAtID(String identifier, String value);
}
