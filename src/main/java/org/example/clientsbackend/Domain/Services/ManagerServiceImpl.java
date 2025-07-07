package org.example.clientsbackend.Domain.Services;

import org.example.clientsbackend.Application.Models.Manager.ManagerModel;
import org.example.clientsbackend.Application.Repositories.Interfaces.ManagerRepository;
import org.example.clientsbackend.Application.ServicesInterfaces.ManagerService;
import org.example.clientsbackend.Domain.Entities.Manager;
import org.example.clientsbackend.Domain.Mappers.ManagerMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository _managerRepository;

    public ManagerServiceImpl(ManagerRepository managerRepository) {
        _managerRepository = managerRepository;
    }

    public void saveManager(Manager manager){
        _managerRepository
                .save(manager);
    }

    public List<ManagerModel> getManagers(){
        return _managerRepository.findAll().stream()
                .map(ManagerMapper.INSTANCE::managerToManagerModel)
                .toList();
    }
}
