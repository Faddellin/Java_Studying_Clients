package org.example.clientsbackend.Application.Models.Enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A filter operator for understanding the type of sorting", example = "equal")
public enum FilterOperator {
    equal,
    notEqual,
    greaterThan,
    lessThan,
    contains;

    public Boolean isApplicableTo(Class<?> obj){
        if(this == FilterOperator.greaterThan || this == FilterOperator.lessThan){
            return Number.class.isAssignableFrom(obj);
        }else if (this == contains) {
            return String.class.isAssignableFrom(obj);
        }

        return true;
    }
}
