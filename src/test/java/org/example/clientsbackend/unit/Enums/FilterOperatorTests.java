package org.example.clientsbackend.unit.Enums;

import org.example.clientsbackend.Application.Models.Enums.FilterOperator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilterOperatorTests {

    @Test
    void isApplicableTo_Positive_returnTrue(){
        assertEquals(FilterOperator.equal.isApplicableTo(Object.class), true);

        assertEquals(FilterOperator.notEqual.isApplicableTo(Object.class), true);

        assertEquals(FilterOperator.greaterThan.isApplicableTo(Number.class), true);
        assertEquals(FilterOperator.greaterThan.isApplicableTo(Integer.class), true);

        assertEquals(FilterOperator.lessThan.isApplicableTo(Integer.class), true);
        assertEquals(FilterOperator.lessThan.isApplicableTo(Number.class), true);

        assertEquals(FilterOperator.contains.isApplicableTo(String.class), true);
    }

    @Test
    void isApplicableTo_Negative_returnFalse(){
        assertEquals(FilterOperator.greaterThan.isApplicableTo(String.class), false);
        assertEquals(FilterOperator.greaterThan.isApplicableTo(Object.class), false);

        assertEquals(FilterOperator.lessThan.isApplicableTo(String.class), false);
        assertEquals(FilterOperator.lessThan.isApplicableTo(Object.class), false);

        assertEquals(FilterOperator.contains.isApplicableTo(Integer.class), false);
        assertEquals(FilterOperator.contains.isApplicableTo(Number.class), false);
    }
}
