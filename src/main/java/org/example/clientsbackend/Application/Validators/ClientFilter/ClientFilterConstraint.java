package org.example.clientsbackend.Application.Validators.ClientFilter;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ClientFilterConstratintValidator.class)
public @interface ClientFilterConstraint {
    String message() default "ClientFilter not correct";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
