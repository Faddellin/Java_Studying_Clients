package org.example.clientsbackend.Application.ServicesInterfaces;

import io.jsonwebtoken.Claims;
import org.example.clientsbackend.Domain.Entities.User;
import org.example.clientsbackend.Domain.Enums.UserRole;

public interface JwtService {

    String getUsernameFromToken(String token);

    UserRole getRoleFromToken(String token);

    String generateToken(User user);

    Claims getTokenClaims(String token);

    Boolean validateToken(String token);

}
