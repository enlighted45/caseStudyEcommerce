package com.caseStudy.E_commerce.DTO.Error;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class ValidationErrorResponseDTO {

    private LocalDateTime timeStamp;
    private int HttpStatusCode;
    private String message;
    private String path;
    private HashMap<String,String> fieldError;


}
