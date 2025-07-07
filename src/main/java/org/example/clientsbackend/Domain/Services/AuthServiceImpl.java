package org.example.clientsbackend.Domain.Services;

import org.apache.coyote.BadRequestException;
import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.Models.Auth.CredentialsModel;
import org.example.clientsbackend.Application.Models.Auth.TokenResponseModel;
import org.example.clientsbackend.Application.Models.Client.ClientCreateModel;
import org.example.clientsbackend.Application.Models.Manager.ManagerCreateModel;
import org.example.clientsbackend.Application.Models.User.AdminCreateModel;
import org.example.clientsbackend.Application.Models.User.UserCreateModel;
import org.example.clientsbackend.Application.Repositories.Interfaces.UserRepository;
import org.example.clientsbackend.Application.ServicesInterfaces.*;
import org.example.clientsbackend.Domain.Entities.Client;
import org.example.clientsbackend.Domain.Entities.Manager;
import org.example.clientsbackend.Domain.Entities.User;
import org.example.clientsbackend.Domain.Enums.UserRole;
import org.example.clientsbackend.Domain.Mappers.ClientMapper;
import org.example.clientsbackend.Domain.Mappers.ManagerMapper;
import org.example.clientsbackend.Domain.Mappers.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder _passwordEncoder;
    private final ClientService _clientService;
    private final ManagerService _managerService;
    private final UserRepository _userRepository;
    private final JwtService _jwtService;
    private final Logger logger = LoggerFactory.getLogger("JSON_BUSINESS_LOGIC_LOGGER");

    public AuthServiceImpl(
            PasswordEncoder passwordEncoder,
            ClientService clientService,
            ManagerService managerService,
            UserRepository userRepository,
            JwtService jwtService
    ) {
        _passwordEncoder = passwordEncoder;
        _clientService = clientService;
        _managerService = managerService;
        _userRepository = userRepository;
        _jwtService = jwtService;
    }

    public TokenResponseModel register(UserCreateModel userCreateModel) throws ExceptionWrapper {
        List<User> usersWithSuchEmailOrUsername = _userRepository.findByEmailOrUsername(
                userCreateModel.getEmail(),
                userCreateModel.getUsername()
        );

        if(!usersWithSuchEmailOrUsername.isEmpty()){
            ExceptionWrapper badRequestExceptionWrapper = new ExceptionWrapper(new BadRequestException());
            badRequestExceptionWrapper.addError("userCreateModel", "User with such email or username already exists");
            logger.error("""
                    Cannot create user because user with such email or username already exists.\
                    
                    NewUser: {}
                    ExistingUsers: {}""", userCreateModel, usersWithSuchEmailOrUsername);
            throw badRequestExceptionWrapper;
        }
        String hashedPassword = _passwordEncoder.encode(userCreateModel.getPassword());
        User user;

        if(userCreateModel instanceof ManagerCreateModel managerCreateModel){
            managerCreateModel.setUserRole(UserRole.MANAGER);
            Manager manager = ManagerMapper.INSTANCE
                    .managerCreateModelToManager(
                            managerCreateModel, hashedPassword
                    );
            _managerService.saveManager(manager);
            user = manager;
        }
        else if(userCreateModel instanceof AdminCreateModel adminCreateModel){
            adminCreateModel.setUserRole(UserRole.ADMIN);
            user = UserMapper.INSTANCE.userCreateModelToUserModel(userCreateModel, hashedPassword);
            _userRepository.save(user);
        }
        else{
            ClientCreateModel clientCreateModel = (ClientCreateModel)userCreateModel;
            clientCreateModel.setUserRole(UserRole.CLIENT);
            Client client = ClientMapper.INSTANCE
                    .clientCreateModelToClient(
                            clientCreateModel, hashedPassword
                    );
            _clientService.saveClient(client);
            user = client;
        }
        logger.info("User registered: {}", user);
        String token = _jwtService.generateToken(user);
        return TokenResponseModel.builder().accessToken(token).build();
    }

    public TokenResponseModel login(CredentialsModel credentialsModel) throws ExceptionWrapper {
        Optional<User> userO = _userRepository.findByUsername(credentialsModel.getUsername());

        ExceptionWrapper badRequestExceptionWrapper = new ExceptionWrapper(new BadRequestException());
        badRequestExceptionWrapper.addError("Credentials", "Incorrect username or password");

        if(userO.isEmpty()){
            throw badRequestExceptionWrapper;
        }

        User user = userO.get();

        if(!isPasswordCorrect(credentialsModel.getPassword(), user.getPasswordHash())){
            throw badRequestExceptionWrapper;
        }

        String token = _jwtService.generateToken(user);
        return TokenResponseModel.builder().accessToken(token).build();

    }

    public String getPasswordHash(String password){
        return _passwordEncoder.encode(password);
    }

    public Boolean isPasswordCorrect(String password, String passwordHash){
        return _passwordEncoder.matches(password, passwordHash);
    }
}
