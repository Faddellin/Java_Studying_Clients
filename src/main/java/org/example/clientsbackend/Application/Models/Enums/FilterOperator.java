package org.example.clientsbackend.Application.Models.Enums;

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
