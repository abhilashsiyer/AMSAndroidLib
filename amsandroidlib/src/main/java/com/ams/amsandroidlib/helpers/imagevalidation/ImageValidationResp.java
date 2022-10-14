package com.ams.amsandroidlib.helpers.imagevalidation;

import com.example.toasterexample.helpers.reporter.TaskResult;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageValidationResp {
    private String task_id;
    private TaskResult taskResult;
    private String task_status;
}