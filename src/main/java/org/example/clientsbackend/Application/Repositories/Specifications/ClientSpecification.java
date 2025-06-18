package org.example.clientsbackend.Application.Repositories.Specifications;

import org.example.clientsbackend.Application.Models.Client.ClientFilterModel;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientFilterCriteria;
import org.example.clientsbackend.Domain.Entities.Client;
import org.springframework.data.jpa.domain.Specification;

public class ClientSpecification {
    public static Specification<Client> addClientSpec(ClientFilterModel clientFilterModel) {
        return switch (clientFilterModel.getFilterOperator()) {
            case equal -> addEqualSpec(clientFilterModel.getValue(), clientFilterModel.getFilterCriteria());
            case notEqual -> addNotEqualSpec(clientFilterModel.getValue(), clientFilterModel.getFilterCriteria());
            case greaterThan -> addGreaterThanSpec(clientFilterModel.getValue(), clientFilterModel.getFilterCriteria());
            case lessThan -> addLessThanSpec(clientFilterModel.getValue(), clientFilterModel.getFilterCriteria());
            default -> addContainsSpec(clientFilterModel.getValue(), clientFilterModel.getFilterCriteria());
        };
    }
    public static Specification<Client> addEqualSpec(Object value, ClientFilterCriteria clientFilterCriteria){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(clientFilterCriteria.toString()), value);
    }
    public static Specification<Client> addNotEqualSpec(Object value, ClientFilterCriteria clientFilterCriteria){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get(clientFilterCriteria.toString()), value);
    }
    public static Specification<Client> addGreaterThanSpec(Object value, ClientFilterCriteria clientFilterCriteria){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get(clientFilterCriteria.toString()), (Integer)value);
    }
    public static Specification<Client> addLessThanSpec(Object value, ClientFilterCriteria clientFilterCriteria){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get(clientFilterCriteria.toString()), (Integer)value);
    }
    public static Specification<Client> addContainsSpec(Object value, ClientFilterCriteria clientFilterCriteria){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(clientFilterCriteria.toString()), "%" + value + "%");
    }
}
