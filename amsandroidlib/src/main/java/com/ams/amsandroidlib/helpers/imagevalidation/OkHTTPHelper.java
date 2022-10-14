package com.ams.amsandroidlib.helpers.imagevalidation;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHTTPHelper {
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
//    private String HOST_NAME = "http://10.0.0.48:5004";
    private String HOST_NAME = "https://visual-validation-manager.herokuapp.com";

    public Response postFileWithTag(File file, String tagName) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
//        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("tag", tagName)
                .addFormDataPart("file", tagName+".png",
                        RequestBody.create(MEDIA_TYPE_PNG, file))
                .build();

        Request request = new Request.Builder()
                .url(HOST_NAME+"/upload-file-for-validation")
                .method("POST", requestBody)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public Response getTaskStatus(File fileToPost, String taskID) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(HOST_NAME+"/tasks/"+taskID)
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
}
