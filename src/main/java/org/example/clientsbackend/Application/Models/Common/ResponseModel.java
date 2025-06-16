package org.example.clientsbackend.Application.Models.Common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Dictionary;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class ResponseModel {
    private Integer statusCode;
    private Dictionary<String, String> errors;
}
