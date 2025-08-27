package co.edu.uniquindio.serviautosbackend.service;

import co.edu.uniquindio.serviautosbackend.dto.EmailDTO;

public interface EmailService {
    void sendEmail(EmailDTO emailDTO);
}
