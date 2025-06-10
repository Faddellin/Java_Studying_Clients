package org.example.clientsbackend.Application.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.clientsbackend.Application.Models.Client.ClientFilterModel;
import org.example.clientsbackend.Application.Models.Client.ClientFiltersModel;
import org.example.clientsbackend.Application.Models.Enums.FilterOperator;
import org.example.clientsbackend.Application.Repositories.Interfaces.CustomClientRepository;
import org.example.clientsbackend.Domain.Entities.Client;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientRepositoryImpl implements CustomClientRepository {

    private final EntityManager _entityManager;

    public ClientRepositoryImpl(EntityManager entityManager) {
        _entityManager = entityManager;
    }

    public List<Client> getClientsByFilters(ClientFiltersModel clientFiltersModel) {
        CriteriaBuilder criteriaBuilder = _entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> criteriaQuery = criteriaBuilder.createQuery(Client.class);

        Root<Client> root = criteriaQuery.from(Client.class);
        List<Predicate> predicates = new ArrayList<>();

        //for (ClientFilterModel clientFilterModel: clientFiltersModel.getFilterModels()){
        //    switch (clientFilterModel.getFilterOperator()){
        //        case equal:
        //            predicates.add(criteriaBuilder.equal(root.get(clientFilterModel.getFilterCriteria(),)))
        //    }
        //}
        Predicate namePredicate = criteriaBuilder
                .like(root.get("name"), "%k%");

        Predicate andPredicate = criteriaBuilder.and(
                namePredicate
        );

        criteriaQuery.where(andPredicate);

        TypedQuery<Client> query = _entityManager.createQuery(criteriaQuery);
        List<Client> clients = query.getResultList();
        return clients;
    }

}
