package pl.sda.refactoring.customers;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import pl.sda.refactoring.customers.exception.MailSendFailureException;

final class DefaultMailSender implements MailSender {

    @Override
    public void sendEmail(String address, String subject, String content) {
        try {
            final var message = prepareMimeMessage(address, subject);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(prepareMimeBody(content));
            message.setContent(multipart);
            Transport.send(message);
        } catch (Exception ex) {
            throw new MailSendFailureException(ex.getMessage(), ex);
        }
    }

    private MimeBodyPart prepareMimeBody(String content) throws MessagingException {
        final var mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(content, "text/html");
        return mimeBodyPart;
    }

    private MimeMessage prepareMimeMessage(String address, String subj) throws MessagingException {
        final var message = new MimeMessage(prepareMailSession());
        message.setFrom(new InternetAddress("no-reply@company.com"));
        message.setRecipients(
            Message.RecipientType.TO, InternetAddress.parse(address));
        message.setSubject(subj);
        return message;
    }

    private Session prepareMailSession() {
        final var session = Session.getInstance(prepareMailPropeties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("admin", "admin");
            }
        });
        return session;
    }

    private Properties prepareMailPropeties() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", System.getenv().get("MAIL_SMTP_HOST"));
        prop.put("mail.smtp.port", System.getenv().get("MAIL_SMTP_PORT"));
        prop.put("mail.smtp.ssl.trust", System.getenv().get("MAIL_SMTP_SSL_TRUST"));
        return prop;
    }
}
