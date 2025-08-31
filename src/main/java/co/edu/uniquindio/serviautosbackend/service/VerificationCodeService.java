package co.edu.uniquindio.serviautosbackend.service;

import co.edu.uniquindio.serviautosbackend.dto.UserCreationDTO;

public interface VerificationCodeService {
    String generateCode(String email, UserCreationDTO userDTO);
    String generatePasswordResetCode(String email);
    boolean validateCode(String email, String code);
    boolean validatePasswordResetCode(String email, String code);
    String getAdminEmail(); // Agregar este método
    UserCreationDTO getPendingUser(String email);
    void savePendingUser(String email, UserCreationDTO pendingUser);
    void removePendingUser(String email);

}
