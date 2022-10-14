package com.ams.amsandroidlib.helpers.imagevalidation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadImageResp {
    private String message;
    private String task_id;
}