package co.edu.uniquindio.serviautosbackend.service.impl;

import co.edu.uniquindio.serviautosbackend.dto.EmailDTO;
import co.edu.uniquindio.serviautosbackend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(EmailDTO emailDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDTO.getTo());
        message.setSubject(emailDTO.getSubject());
        message.setText(emailDTO.getBody());
        mailSender.send(message);
    }
}