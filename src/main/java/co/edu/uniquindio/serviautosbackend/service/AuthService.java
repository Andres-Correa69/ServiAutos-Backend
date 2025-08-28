package co.edu.uniquindio.serviautosbackend.service;

import co.edu.uniquindio.serviautosbackend.domain.models.User;
import co.edu.uniquindio.serviautosbackend.dto.UserCreationDTO;

public interface AuthService {
    User createUser(UserCreationDTO user);
    User validateLogin(String email, String password) throws Exception;
}
