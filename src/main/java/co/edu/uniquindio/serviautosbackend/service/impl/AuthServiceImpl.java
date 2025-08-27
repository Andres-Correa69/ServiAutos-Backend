package co.edu.uniquindio.serviautosbackend.service.impl;

import co.edu.uniquindio.serviautosbackend.domain.models.User;
import co.edu.uniquindio.serviautosbackend.dto.UserCreationDTO;
import co.edu.uniquindio.serviautosbackend.repository.AuthRepository;
import co.edu.uniquindio.serviautosbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Override
    public User createUser(UserCreationDTO user) {
        User newUser = new User();
        newUser.setName(user.name());
        newUser.setLastName(user.lastName());
        newUser.setPhone(user.phone());
        newUser.setAddress(user.address());
        newUser.setEmail(user.email());
        newUser.setPassword(user.password());
        newUser.setRegisterDate(user.registerDate());

        authRepository.save(newUser);
        return newUser;
    }

}
