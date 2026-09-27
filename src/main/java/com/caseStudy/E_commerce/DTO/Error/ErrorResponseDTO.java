package com.caseStudy.E_commerce.DTO.Error;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {

    private LocalDateTime timeStamp;
    private int HttpStatusCode;
    private String message;
    private String path;

}
