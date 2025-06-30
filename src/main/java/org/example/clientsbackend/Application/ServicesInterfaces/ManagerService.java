package org.example.clientsbackend.Application.ServicesInterfaces;

import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.Models.Manager.ManagerCreateModel;
import org.example.clientsbackend.Application.Models.Manager.ManagerModel;

import java.util.List;

public interface ManagerService {
    void addManager(ManagerCreateModel managerCreateModel) throws ExceptionWrapper;
    List<ManagerModel> getManagers();
}
