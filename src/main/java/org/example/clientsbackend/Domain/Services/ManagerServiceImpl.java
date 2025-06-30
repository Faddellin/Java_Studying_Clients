package org.example.clientsbackend.Domain.Services;

import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.Models.Manager.ManagerCreateModel;
import org.example.clientsbackend.Application.Models.Manager.ManagerModel;
import org.example.clientsbackend.Application.Repositories.Interfaces.ManagerRepository;
import org.example.clientsbackend.Application.ServicesInterfaces.ManagerService;
import org.example.clientsbackend.Domain.Mappers.ManagerMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository _managerRepository;

    public ManagerServiceImpl(ManagerRepository managerRepository) {
        _managerRepository = managerRepository;
    }

    public void addManager(ManagerCreateModel managerCreateModel){
        _managerRepository
                .save(ManagerMapper.INSTANCE.managerCreateModelToManager(managerCreateModel));
    }

    public List<ManagerModel> getManagers(){
        return _managerRepository.findAll().stream()
                .map(ManagerMapper.INSTANCE::managerToManagerModel)
                .toList();
    }
}
