package org.example.clientsbackend.Application.Models.Client.Enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "One of the user attributes that can be used for sorting", example = "username")
public enum ClientSortCriteria {
    username, age, email
}
