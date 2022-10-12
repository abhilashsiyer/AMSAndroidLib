package com.ams.amsandroidlib;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObject2Condition;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import com.ams.amsandroidlib.api.AMS;
import com.ams.amsandroidlib.helper.OkHTTPHelper;
import com.ams.amsandroidlib.helper.Reporter;
import com.ams.amsandroidlib.helper.Suite;
import com.ams.amsandroidlib.helper.UploadImageResp;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Response;

public class AMSAndroid implements AMS {
    private String packageName;

    private static final int LAUNCH_TIMEOUT = 5000;

    private static final int DEFAULT_TIMEOUT = 30000;

    private String apiKey;

    private static final String STRING_TO_BE_TYPED = "UiAutomator";

    private UiDevice mDevice;

    private Reporter reporter = new Reporter();
    private ArrayList<Suite> suites = new ArrayList<>();
    private Suite suite = new Suite();
    private ArrayList <String> testName = new ArrayList<>();


    public AMSAndroid(String apiKey) {
        this.apiKey = apiKey;
        // Verify apiKey
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        // Start from the home screen
        mDevice.pressHome();
        // Wait for device launcher
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);
        reporter = new Reporter();
        suites = new ArrayList<>();
        suite = new Suite();
        testName = new ArrayList<>();
    }

    public void launchApp(String packageName){
        this.packageName = packageName ;
        // Launch the blueprint app
        Context context = getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(this.packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(this.packageName).depth(0)), LAUNCH_TIMEOUT);
    }

    public void clickByResourceID(String identifier){
        //new UiSelector().resourceId(this.packageName +":id/"+identifier)
        UiObject2 objectByResourceId = mDevice.wait(Until
                .findObject(By.res(this.packageName,identifier)), DEFAULT_TIMEOUT);

        if (objectByResourceId.isClickable()){
            objectByResourceId.click();
        }
        else {
            System.out.println("Element not clickable");
        }
    }

    public void clickByResourceID(String packageName, String identifier){
        UiObject2 objectByResourceId = mDevice.wait(Until
                .findObject(By.res(packageName,identifier)), DEFAULT_TIMEOUT);

        if (objectByResourceId.isClickable()){
            objectByResourceId.click();
        }
        else {
            System.out.println("Element not clickable");
        }
    }

    public void clickByText(String textIdentifier){
        UiObject2 objectByResourceId = mDevice.wait(Until
                .findObject(By.textStartsWith(textIdentifier)), DEFAULT_TIMEOUT);

        if (objectByResourceId.isClickable()){
            objectByResourceId.click();
        }
        else {
            System.out.println("Element not clickable");
        }

    }

    public void verifyTextIsDisplayed(String textIdentifier){
        UiObject2 objectByResourceId = mDevice.wait(Until
                .findObject(By.textStartsWith(textIdentifier)), DEFAULT_TIMEOUT);

        if (objectByResourceId.isClickable()){
            objectByResourceId.click();
        }
        else {
            System.out.println("Element not clickable");
        }

    }

    @Override
    public void verifyScreen(String tagName) throws IOException {
        File file = new File(String.valueOf(InstrumentationRegistry.getInstrumentation().
                getTargetContext().getFilesDir()), tagName+".png");
        boolean isScreenshot = mDevice.takeScreenshot(file);
        OkHTTPHelper okHTTPHelper = new OkHTTPHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        if (isScreenshot){
                Response response = okHTTPHelper.postFileWithTag(file,tagName);
                UploadImageResp imageValidationResp = objectMapper.readValue(response.body().string(), UploadImageResp.class);
                System.out.println("File path"+file.getAbsolutePath());
                System.out.println("Response code"+response.code());
                System.out.println("Response from post file"+imageValidationResp.getMessage());
                System.out.println("Task ID"+imageValidationResp.getTask_id());

                System.out.println("Failure in validation");
//                e.printStackTrace()
        }
        else {
            throw new AssertionError("Failure in taking screenshot");
//            System.out.println("Failure in taking screenshot");
        }
    }

    @Override
    public void pageName(String text) {
        suite.setSuiteName(text);
    }

    @Override
    public void testName(String text) {
        testName.add(text);
    }

    @Override
    public void end() {
        suite.setTestName(testName);
        suites.add(suite);
        reporter.setSuite(suites);
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("Mapper begin");
        try {
            String jsonInString = mapper.writeValueAsString(reporter);
            System.out.println("JSON str"+ jsonInString);
        } catch (IOException e) {
            System.out.println("Mapper failed");
            e.printStackTrace();
        }


    }

    @Override
    public void verifyValueAtID(String identifier, String value) {
        UiObject toggleButton1 = mDevice.findObject(new UiSelector().resourceId(
                this.packageName +":id/"+identifier));
        String valueOriginal = "";
        try {
            valueOriginal = toggleButton1.getText();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Assert.assertEquals(value,valueOriginal);
            suite.setTestResult("Pass");
        } catch (AssertionError e) {
            suite.setTestResult("Fail");
            throw new AssertionError(e.getMessage());
//            e.printStackTrace();
        }

    }


    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */

    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = getApplicationContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }

    private void setupReporter(){

    }


}
