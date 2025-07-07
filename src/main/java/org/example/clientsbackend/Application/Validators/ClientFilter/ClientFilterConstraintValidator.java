package org.example.clientsbackend.Application.Validators.ClientFilter;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.clientsbackend.Application.Models.Client.ClientFilterModel;
import org.example.clientsbackend.Domain.Entities.Client;

import java.lang.reflect.Field;

public class ClientFilterConstraintValidator implements ConstraintValidator<ClientFilterConstraint, ClientFilterModel> {
    @Override
    public boolean isValid(ClientFilterModel clientFilterModel, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Field field = getFieldIncludingSuperclasses(Client.class ,clientFilterModel.getFilterCriteria().toString());

            return clientFilterModel.getFilterOperator().isApplicableTo(field.getType()) &&
                    field.getType().isAssignableFrom(clientFilterModel.getValue().getClass());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static Field getFieldIncludingSuperclasses(Class<?> clazz, String fieldName)
            throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                return getFieldIncludingSuperclasses(superclass, fieldName);
            }
            throw e;
        }
    }
}
