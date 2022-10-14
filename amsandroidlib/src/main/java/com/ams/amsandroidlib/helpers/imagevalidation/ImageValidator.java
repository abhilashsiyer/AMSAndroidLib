package com.ams.amsandroidlib.helpers.imagevalidation;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

import lombok.SneakyThrows;
import okhttp3.Response;

public class ImageValidator {
    @SneakyThrows
    public void verifyPage(UiDevice mDevice, String tagName){
        File file = new File(String.valueOf(InstrumentationRegistry.getInstrumentation().
                getTargetContext().getFilesDir()), tagName + ".png");
        boolean isScreenshot = mDevice.takeScreenshot(file);
        OkHTTPHelper okHTTPHelper = new OkHTTPHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        if (isScreenshot) {
            Response response = okHTTPHelper.postFileWithTag(file, tagName);
            UploadImageResp imageValidationResp = objectMapper.readValue(response.body().string(), UploadImageResp.class);
            System.out.println("File path" + file.getAbsolutePath());
            System.out.println("Response code" + response.code());
            System.out.println("Response from post file" + imageValidationResp.getMessage());
            System.out.println("Task ID" + imageValidationResp.getTask_id());
            System.out.println("Failure in validation");
        } else {
            throw new AssertionError("Failure in taking screenshot");
        }
    }
}
