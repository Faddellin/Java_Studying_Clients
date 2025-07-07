package org.example.clientsbackend.Application.Repositories.Interfaces;

import org.example.clientsbackend.Domain.Entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends
        BaseRepository<User, Long>
{
    List<User> findByEmailOrUsername(String email, String username);
    Optional<User> findByUsername(String username);

}
