package co.edu.uniquindio.serviautosbackend.service.impl;

import co.edu.uniquindio.serviautosbackend.dto.VerificationCodeDataDTO;
import co.edu.uniquindio.serviautosbackend.dto.UserCreationDTO;
import co.edu.uniquindio.serviautosbackend.service.VerificationCodeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Getter
public class VerificationCodeServiceImpl implements VerificationCodeService {
    private final Map<String, VerificationCodeDataDTO> registrationCodes = new ConcurrentHashMap<>();
    private final Map<String, VerificationCodeDataDTO> passwordResetCodes = new ConcurrentHashMap<>();
    private static final long EXPIRATION_TIME = 300000; // 5 minutos

    @Value("${admin.email}")
    private String adminEmail;

    @Override
    public String generateCode(String email, UserCreationDTO userDTO) {
        String code = String.format("%06d", new Random().nextInt(999999));
        registrationCodes.put(email, new VerificationCodeDataDTO(code, System.currentTimeMillis(), userDTO));
        return code;
    }

    @Override
    public String generatePasswordResetCode(String email) {
        String code = String.format("%06d", new Random().nextInt(999999));
        passwordResetCodes.put(email, new VerificationCodeDataDTO(code, System.currentTimeMillis(), null));
        return code;
    }

    @Override
    public boolean validateCode(String email, String code) {
        VerificationCodeDataDTO data = registrationCodes.get(email);
        return validateAndRemove(registrationCodes, email, code, data);
    }

    @Override
    public boolean validatePasswordResetCode(String email, String code) {
        VerificationCodeDataDTO data = passwordResetCodes.get(email);
        return validateAndRemove(passwordResetCodes, email, code, data);
    }

    private boolean validateAndRemove(Map<String, VerificationCodeDataDTO> codes, String email,
                                      String code, VerificationCodeDataDTO data) {
        if (data == null) return false;

        boolean isValid = data.code().equals(code) &&
                (System.currentTimeMillis() - data.timestamp()) < EXPIRATION_TIME;

        // ❌ No remover aquí, deja que lo remueva verifySignup después de usar el pendingUser
        return isValid;
    }

    @Override
    public void removePendingUser(String email) {
        registrationCodes.remove(email);
    }


    public UserCreationDTO getPendingUser(String email) {
        VerificationCodeDataDTO data = registrationCodes.get(email);
        return data != null ? data.pendingUser() : null;
    }

    @Override
    public void savePendingUser(String email, UserCreationDTO pendingUser) {
        VerificationCodeDataDTO data = registrationCodes.get(email);
        if (data != null) {
            registrationCodes.put(email,
                    new VerificationCodeDataDTO(data.code(), data.timestamp(), pendingUser));
        }
    }

    public String getAdminEmail() {
        return adminEmail;
    }
}

