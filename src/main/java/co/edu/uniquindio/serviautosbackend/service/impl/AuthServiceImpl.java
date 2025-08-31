package co.edu.uniquindio.serviautosbackend.service.impl;

import co.edu.uniquindio.serviautosbackend.domain.models.User;
import co.edu.uniquindio.serviautosbackend.dto.UserCreationDTO;
import co.edu.uniquindio.serviautosbackend.repository.AuthRepository;
import co.edu.uniquindio.serviautosbackend.repository.UserRepository;
import co.edu.uniquindio.serviautosbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserCreationDTO user) {
        User newUser = new User();
        newUser.setName(user.name());
        newUser.setLastName(user.lastName());
        newUser.setPhone(user.phone());
        newUser.setAddress(user.address());
        newUser.setEmail(user.email());
        newUser.setPassword(passwordEncoder.encode(user.password()));
        newUser.setRegisterDate(user.registerDate());

        authRepository.save(newUser);
        return newUser;
    }

    @Override
    public User validateLogin(String email, String password) throws Exception {
        Optional<User> user = authRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new Exception("Usuario no encontrado");
        }
        // ✅ Comparar usando BCrypt
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new Exception("Contraseña incorrecta");
        }
        return user.get();
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
