package com.fpt.ecoversenotification.services.imp;

import com.fpt.ecoversenotification.dtos.ParentCredentialMail;
import com.fpt.ecoversenotification.services.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class MailServiceImp implements MailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${app.mail.from}")
    private String from;

    @Value("${app.mail.loginUrl}")
    private String loginUrl;

    public MailServiceImp(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    @Async("mailExecutor")
    public void sendParentWelcomeBatch(List<ParentCredentialMail> mails) {
        for (ParentCredentialMail mail : mails) {
            try {
                sendParentWelcome(mail);
            } catch (Exception ex) {
                
            }
        }
    }

    @Retryable(
            retryFor = {MailException.class, MessagingException.class, RuntimeException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    private void sendParentWelcome(ParentCredentialMail mail) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name()
        );
        helper.setFrom(from);
        helper.setTo(loginUrl);
        helper.setSubject("[EcoVerse] Tài khoản Parent của bạn đã được tạo");
        Context context = new Context();
        context.setVariable("fullName", mail.getFullName());
        context.setVariable("email", mail.getEmail());
        context.setVariable("rawPassword", mail.getRawPassword());
        context.setVariable("loginUrl", loginUrl);

        String html = templateEngine.process("mail/welcome-parent", context);
        helper.setText(html, true);
        mailSender.send(message);
    }

    @Recover
    public void recover(Exception ex, ParentCredentialMail mail) {

    }
}
