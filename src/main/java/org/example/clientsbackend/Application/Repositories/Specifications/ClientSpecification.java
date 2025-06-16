package org.example.clientsbackend.Application.Repositories.Specifications;

import org.example.clientsbackend.Application.Models.Client.ClientFilterModel;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientFilterCriteria;
import org.example.clientsbackend.Application.Models.Enums.FilterOperator;
import org.example.clientsbackend.Domain.Entities.Client;
import org.springframework.data.jpa.domain.Specification;

public class ClientSpecification {
    public static Specification<Client> addClientSpec(ClientFilterModel clientFilterModel) {
        Specification<Client> newClientSpecification;
        switch (clientFilterModel.getFilterOperator()){
            case equal:
                newClientSpecification = addEqualSpec(clientFilterModel.getValue(),clientFilterModel.getFilterCriteria());
                break;
            case notEqual:
                newClientSpecification = addNotEqualSpec(clientFilterModel.getValue(),clientFilterModel.getFilterCriteria());
                break;
            case greaterThan:
                newClientSpecification = addGreaterThanSpec(clientFilterModel.getValue(),clientFilterModel.getFilterCriteria());
                break;
            case lessThan:
                newClientSpecification = addLessThanSpec(clientFilterModel.getValue(),clientFilterModel.getFilterCriteria());
                break;
            default:
                newClientSpecification = addContainsSpec(clientFilterModel.getValue(),clientFilterModel.getFilterCriteria());
                break;
        }
        return newClientSpecification;
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
                criteriaBuilder.like(root.get(clientFilterCriteria.toString()), "%" + (String)value + "%");
    }
}
