package org.example.clientsbackend.unit.Constraints;

import org.example.clientsbackend.Application.Models.Client.ClientFilterModel;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientFilterCriteria;
import org.example.clientsbackend.Application.Models.Enums.FilterOperator;
import org.example.clientsbackend.Application.Validators.ClientFilter.ClientFilterConstraintValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClientFilterConstraintTests {

    private final ClientFilterConstraintValidator validator = new ClientFilterConstraintValidator();

    @Test
    void isValid_Positive_returnTrue() {
        ClientFilterModel clientFilterModel = new ClientFilterModel(
                ClientFilterCriteria.age,
                FilterOperator.greaterThan,
                25
        );
        assertTrue(validator.isValid(clientFilterModel, null));
    }

    @Test
    void isValid_NegativeWithIncompatibleCriteriaAndOperator_returnFalse() {
        ClientFilterModel clientFilterModel = new ClientFilterModel(
                ClientFilterCriteria.age,
                FilterOperator.contains,
                "check"
        );
        assertFalse(validator.isValid(clientFilterModel, null));
    }

    @Test
    void isValid_NegativeWithIncompatibleCriteriaAndValue_returnFalse() {
        ClientFilterModel clientFilterModel = new ClientFilterModel(
                ClientFilterCriteria.email,
                FilterOperator.contains,
                15
        );
        assertFalse(validator.isValid(clientFilterModel, null));
    }

    @Test
    void isValid_NegativeWithNullValue_returnFalse() {
        ClientFilterModel clientFilterModel = new ClientFilterModel(
                ClientFilterCriteria.age,
                FilterOperator.lessThan,
                null
        );
        assertThrows(NullPointerException.class, () -> validator.isValid(clientFilterModel, null));
    }
}
