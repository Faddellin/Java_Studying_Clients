package org.example.clientsbackend.Application.Models.Common;

import lombok.*;

import java.util.Dictionary;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class ErrorLogModel {

    public ErrorLogModel(ResponseModel responseModel, String stackTrace) {
        this.statusCode = responseModel.getStatusCode();
        this.errors = responseModel.getErrors();
        this.stackTrace = stackTrace;
    }

    private Integer statusCode;
    private Dictionary<String, String> errors;
    private String stackTrace;

    @Override
    public String toString() {
        return String.format(
                "{statusCode=%s, errors=%s, stackTrace=%s}",
                statusCode, errors.toString(), stackTrace
        );
    }
}
