package org.example.clientsbackend.Presentation.Controllers;

import jakarta.validation.Valid;
import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.Models.Manager.ManagerCreateModel;
import org.example.clientsbackend.Application.Models.Manager.ManagerModel;
import org.example.clientsbackend.Application.ServicesInterfaces.ManagerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ManagersController {

    private final ManagerService _managerService;

    public ManagersController(ManagerService managerService) {
        _managerService = managerService;
    }

    @PostMapping(path = "managers/add")
    public void AddManager(@Valid @RequestBody ManagerCreateModel managerCreateModel) throws ExceptionWrapper {
        _managerService.addManager(managerCreateModel);
    }

    @GetMapping(path = "managers")
    public List<ManagerModel> GetManagers() {
        return _managerService.getManagers();
    }
}
