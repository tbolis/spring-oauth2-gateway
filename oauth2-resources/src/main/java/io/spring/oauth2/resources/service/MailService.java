package io.spring.oauth2.resources.service;

import io.spring.oauth2.domain.user.User;
import io.spring.oauth2.resources.config.AppProperties;
import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Service for sending e-mails.
 * <p/>
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger LOG = getLogger(MailService.class);

    @Inject
    private AppProperties appProperties;

    @Inject
    private JavaMailSender javaMailSender;

    @Inject
    private MessageSource messageSource;

//    @Inject
//    private SpringTemplateEngine templateEngine;

    /**
     * System default email address that sends the e-mails.
     */
    private String from;

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        LOG.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(appProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            LOG.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            LOG.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

    @Async
    public void sendActivationEmail(User user, String baseUrl) {
//        TODO
//        LOG.debug("Sending activation e-mail to '{}'", user.getEmail());
//        Locale locale = Locale.forLanguageTag(user.getLangKey());
//        Context context = new Context(locale);
//        context.setVariable("user", user);
//        context.setVariable("baseUrl", baseUrl);
//        String content = templateEngine.process("activationEmail", context);
//        String subject = messageSource.getMessage("email.activation.title", null, locale);
//        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendPasswordResetMail(User user, String baseUrl) {
//        TODO
//        LOG.debug("Sending password reset e-mail to '{}'", user.getEmail());
//        Locale locale = Locale.forLanguageTag(user.getLangKey());
//        Context context = new Context(locale);
//        context.setVariable("user", user);
//        context.setVariable("baseUrl", baseUrl);
//        String content = templateEngine.process("passwordResetEmail", context);
//        String subject = messageSource.getMessage("email.reset.title", null, locale);
//        sendEmail(user.getEmail(), subject, content, false, true);
    }

}
