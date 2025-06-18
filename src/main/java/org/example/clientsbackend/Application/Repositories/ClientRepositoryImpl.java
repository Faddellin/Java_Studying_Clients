package org.example.clientsbackend.Application.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.clientsbackend.Application.Models.Client.ClientFilterModel;
import org.example.clientsbackend.Application.Models.Client.ClientFiltersModel;
import org.example.clientsbackend.Application.Repositories.Factories.SortFactory;
import org.example.clientsbackend.Application.Repositories.Interfaces.ClientRepository;
import org.example.clientsbackend.Application.Repositories.Specifications.ClientSpecification;
import org.example.clientsbackend.Domain.Entities.Client;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    public Optional<Client> findByEmail(String email) {

        CriteriaBuilder cb = _entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> query = cb.createQuery(Client.class);
        Root<Client> root = query.from(Client.class);

        query.where(cb.equal(root.get("email"), email));

        try{
            Client client = _entityManager.createQuery(query).getSingleResult();
            return Optional.ofNullable(client);
        }catch (NoResultException  ex){
            return Optional.empty();
        }

    }

    public List<Client> getClientsByFilters(ClientFiltersModel clientFiltersModel) {

        Specification<Client> spec = (root, query, criteriaBuilder) -> null;
        SortFactory sortFactory = new SortFactory();
        Sort sort = sortFactory.createSort(clientFiltersModel.getSortType().getSortOrder(),clientFiltersModel.getSortType().getSortCriteria().toString());

        for(ClientFilterModel cfm: clientFiltersModel.getFilterModels()){
            spec = ClientSpecification.addClientSpec(cfm);
        }

        return this.findAll(spec,
                (clientFiltersModel.getPage() - 1) * clientFiltersModel.getSize(),
                clientFiltersModel.getSize(),
                sort);
    }

    public Integer getClientsCountByFilters(ClientFiltersModel clientFiltersModel){
        Specification<Client> spec = (root, query, criteriaBuilder) -> null;

        for(ClientFilterModel cfm: clientFiltersModel.getFilterModels()){
            spec = ClientSpecification.addClientSpec(cfm);
        }

        return Math.toIntExact(this.count(spec));
    }
}
