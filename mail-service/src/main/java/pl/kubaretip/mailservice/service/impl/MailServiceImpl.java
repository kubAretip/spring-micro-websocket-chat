package pl.kubaretip.mailservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.kubaretip.dtomodels.UserDTO;
import pl.kubaretip.mailservice.service.SendMailService;

import javax.mail.MessagingException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class MailServiceImpl implements SendMailService {

    private final JavaMailSender sender;
    private final TemplateEngine templateEngine;

    @Value("${mail.links.baseUrl:none}")
    private String baseMailProcessingUrl;

    public MailServiceImpl(JavaMailSender sender,
                           TemplateEngine templateEngine) {
        this.sender = sender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        var mimeMessage = this.sender.createMimeMessage();

        try {
            var mimeHelper = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            mimeHelper.setTo(to);
            mimeHelper.setSubject(subject);
            mimeHelper.setText(content, isHtml);

            sender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            log.warn("Email wasn't send to user {}", to, e);
        }
    }


    @Async
    @Override
    public void sendActivationEmail(UserDTO user) {

        var userEmail = user.getEmail();
        if (userEmail != null) {
            log.debug("Sending email template to {}", userEmail);

            var activationLink = baseMailProcessingUrl + "?data=" + user.getActivationKey();
            var context = new Context();
            context.setVariable("user", user);
            context.setVariable("activationUrl", activationLink);

            var subject = "Activate your account";
            var content = templateEngine.process("mail/activationMail", context);
            sendEmail(userEmail, subject, content, false, true);
        }

    }


}
