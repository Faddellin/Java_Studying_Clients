package org.example.clientsbackend.Application.Repositories;

import jakarta.persistence.EntityManager;
import org.example.clientsbackend.Application.Models.Client.ClientFilterModel;
import org.example.clientsbackend.Application.Models.Client.ClientFiltersModel;
import org.example.clientsbackend.Application.Repositories.Factories.SortFactory;
import org.example.clientsbackend.Application.Repositories.Interfaces.ClientRepository;
import org.example.clientsbackend.Application.Repositories.Specifications.ClientSpecification;
import org.example.clientsbackend.Domain.Entities.Client;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientRepositoryImpl extends
        BaseRepositoryImpl<Client, Long>
        implements ClientRepository
{
    private final EntityManager _entityManager;

    public ClientRepositoryImpl(EntityManager entityManager) {
        super(Client.class, entityManager);
        _entityManager = entityManager;
    }

    public List<Client> getClientsByFilters(ClientFiltersModel clientFiltersModel) {

        Specification<Client> spec = (root, query, criteriaBuilder) -> null;
        SortFactory sortFactory = new SortFactory();
        Sort sort = sortFactory.createSort(clientFiltersModel.getSortType().getSortOrder(),clientFiltersModel.getSortType().getSortCriteria().toString());

        for(ClientFilterModel cfm: clientFiltersModel.getFilterModels()){
            spec = ClientSpecification.addClientSpec(cfm);
        }

        List<Client> clients = this.findAll(spec,
                (clientFiltersModel.getPage() - 1) * clientFiltersModel.getSize(),
                clientFiltersModel.getSize(),
                sort);

        return clients;
    }

    public Integer getClientsCountByFilters(ClientFiltersModel clientFiltersModel){
        Specification<Client> spec = (root, query, criteriaBuilder) -> null;

        for(ClientFilterModel cfm: clientFiltersModel.getFilterModels()){
            spec = ClientSpecification.addClientSpec(cfm);
        }

        Integer clientsCount = Math.toIntExact(this.count(spec));

        return clientsCount;
    }
}
