package org.example.clientsbackend.Application.Repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.clientsbackend.Application.Models.Client.ClientFilterModel;
import org.example.clientsbackend.Application.Models.Client.ClientFiltersModel;
import org.example.clientsbackend.Application.Models.Client.Enums.ClientFilterCriteria;
import org.example.clientsbackend.Application.Repositories.Factories.SortFactory;
import org.example.clientsbackend.Application.Repositories.Interfaces.ClientRepository;
import org.example.clientsbackend.Application.Repositories.Specifications.ClientSpecification;
import org.example.clientsbackend.Domain.Entities.Client;
import org.example.clientsbackend.Domain.Entities.Manager;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Primary
@Repository
public class ClientRepositoryImpl
        extends BaseRepositoryImpl<Client, Long>
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

    public Optional<Client> findByEmailOrUsername(String email, String username){
        CriteriaBuilder cb = _entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> query = cb.createQuery(Client.class);
        Root<Client> root = query.from(Client.class);

        query.where(cb.equal(root.get(ClientFilterCriteria.email.toString()), email));
        query.where(cb.equal(root.get(ClientFilterCriteria.username.toString()), username));

        try{
            Client client = _entityManager.createQuery(query).getSingleResult();
            return Optional.ofNullable(client);
        }catch (NoResultException  ex){
            return Optional.empty();
        }
    }

    public List<Client> findAllByEmail(String email) {

        CriteriaBuilder cb = _entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> query = cb.createQuery(Client.class);
        Root<Client> root = query.from(Client.class);

        query.where(cb.equal(root.get("email"), email));

        return _entityManager.createQuery(query).getResultList();
    }

    public List<Client> findAllByManager(Manager manager){
        CriteriaBuilder cb = _entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> query = cb.createQuery(Client.class);
        Root<Client> root = query.from(Client.class);

        query.where(cb.equal(root.get("manager"), manager));

        return _entityManager.createQuery(query).getResultList();
    }

    public List<Client> getClientsByFilters(ClientFiltersModel clientFiltersModel) {

        Specification<Client> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        SortFactory sortFactory = new SortFactory();
        boolean isSorting = clientFiltersModel.getSortType() != null;
        Sort sort = sortFactory.createSort(
                isSorting ?
                clientFiltersModel.getSortType().getSortOrder() : null,
                isSorting ?
                clientFiltersModel.getSortType().getSortCriteria().toString() : null
        );

        if(clientFiltersModel.getFilterModels() != null){
            for(ClientFilterModel cfm: clientFiltersModel.getFilterModels()){
                spec = ClientSpecification.addClientSpec(cfm);
            }
        }

        return this.findAll(spec,
                Math.max((clientFiltersModel.getPage() - 1) * clientFiltersModel.getSize(),0),
                clientFiltersModel.getSize(),
                sort);
    }

    public Integer getClientsCountByFilters(ClientFiltersModel clientFiltersModel){
        Specification<Client> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if(clientFiltersModel.getFilterModels() != null){
            for(ClientFilterModel cfm: clientFiltersModel.getFilterModels()){
                spec = ClientSpecification.addClientSpec(cfm);
            }
        }

        return Math.toIntExact(this.count(spec));
    }
}
