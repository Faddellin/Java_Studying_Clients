package org.example.clientsbackend.Application.ServicesInterfaces;

import org.example.clientsbackend.Application.Exceptions.ExceptionWrapper;
import org.example.clientsbackend.Application.Models.Auth.CredentialsModel;
import org.example.clientsbackend.Application.Models.Auth.TokenResponseModel;
import org.example.clientsbackend.Application.Models.User.UserCreateModel;


public interface AuthService {

    TokenResponseModel login(CredentialsModel credentialsModel) throws ExceptionWrapper;
    TokenResponseModel register(UserCreateModel userCreateModel) throws ExceptionWrapper;
    String getPasswordHash(String password);
    Boolean isPasswordCorrect(String password, String passwordHash);
}
