package co.edu.uniquindio.serviautosbackend.service;

import co.edu.uniquindio.serviautosbackend.dto.EmailDTO;

public interface EmailService {
    void sendEmail(EmailDTO emailDTO);
    void sendVerificationCode(String to, String code, String subject);
}
