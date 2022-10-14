package com.ams.amsandroidlib.api;

import org.junit.rules.TestName;

import java.io.IOException;

public interface AMS {
    void launchApp(String pkgName);
    void clickById(String identifier);
    void clickById(int resId);
    void clickById(String pkgName, String identifier);
    void clickByText(String textIdentifier);
    void clickByText(String text, int resId);
    void verifyPage(String tagName);
    void verifyTextIsVisible(String textIdentifier) ;
    void verifyTextIsVisibleAtId(String resourceId, String valueToMatch);
    void verifyTextIsVisibleAtId(String valueToMatch, int resId);
    void verifyTextIsVisibleAtId(String resourceId, String pkgName, String valueToMatch);
    void end(TestName testName);
}
