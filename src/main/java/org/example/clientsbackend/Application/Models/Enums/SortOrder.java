package org.example.clientsbackend.Application.Models.Enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Sorting order", example = "Ascending")
public enum SortOrder {
    Ascending, Descending
}
