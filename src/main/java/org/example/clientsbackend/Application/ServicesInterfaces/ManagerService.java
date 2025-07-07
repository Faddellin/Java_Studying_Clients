package org.example.clientsbackend.Application.ServicesInterfaces;

import org.example.clientsbackend.Application.Models.Manager.ManagerModel;
import org.example.clientsbackend.Domain.Entities.Manager;

import java.util.List;

public interface ManagerService {
    void saveManager(Manager manager);
    List<ManagerModel> getManagers();
}
