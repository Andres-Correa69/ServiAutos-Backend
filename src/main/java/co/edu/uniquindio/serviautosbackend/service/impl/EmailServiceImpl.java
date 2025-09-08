package co.edu.uniquindio.serviautosbackend.service.impl;

import co.edu.uniquindio.serviautosbackend.dto.EmailDTO;
import co.edu.uniquindio.serviautosbackend.service.EmailService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final SendGrid sendGrid;
    
    @Value("${sendgrid.from.email}")
    private String fromEmail;
    
    @Value("${sendgrid.from.name}")
    private String fromName;

    @Override
    public void sendEmail(EmailDTO emailDTO) {
        try {
            Email from = new Email(fromEmail, fromName);
            Email to = new Email(emailDTO.getTo());
            String subject = emailDTO.getSubject();
            Content content = new Content("text/plain", emailDTO.getBody());
            
            Mail mail = new Mail(from, subject, to, content);
            
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            
            Response response = sendGrid.api(request);
            
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                log.info("Email enviado exitosamente a: {}", emailDTO.getTo());
            } else {
                log.error("Error al enviar email. Status: {}, Body: {}", 
                    response.getStatusCode(), response.getBody());
                throw new RuntimeException("Error al enviar email: " + response.getBody());
            }
            
        } catch (IOException e) {
            log.error("Error al enviar email a {}: {}", emailDTO.getTo(), e.getMessage());
            throw new RuntimeException("Error al enviar email: " + e.getMessage());
        }
    }

    @Override
    public void sendVerificationCode(String to, String code, String subject) {
        EmailDTO emailDTO = EmailDTO.builder()
                .to(to)
                .subject(subject)
                .body("Tu código de verificación es: " + code)
                .build();
        sendEmail(emailDTO);
    }
}
