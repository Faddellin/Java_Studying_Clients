package org.example.clientsbackend.Application.Validators.ClientFilter;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.clientsbackend.Application.Models.Client.ClientFilterModel;
import org.example.clientsbackend.Domain.Entities.Client;

import java.lang.reflect.Field;

public class ClientFilterConstratintValidator implements ConstraintValidator<ClientFilterConstraint, ClientFilterModel> {
    @Override
    public boolean isValid(ClientFilterModel clientFilterModel, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Field field = Client.class.getDeclaredField(clientFilterModel.getFilterCriteria().toString());
            return clientFilterModel.getFilterOperator().isApplicableTo(field.getType()) &&
                    field.getType().isAssignableFrom(clientFilterModel.getValue().getClass());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
