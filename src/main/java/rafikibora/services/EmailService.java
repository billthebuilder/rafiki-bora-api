package rafikibora.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import rafikibora.exceptions.RafikiBoraException;

import java.io.IOException;

/**
 * This service allows you to send a unique token to a user email
 *
 */
@Service
@Slf4j
public class EmailService {

    /**
     * Sends Emails
     *
     * @param recipient Email address of recipient
     */
    @Async
    void sendEmail(
            String recipient,
            String token) throws IOException {
        Email from = new Email("no-reply@rafikibora.com"); // sender
        Email to = new Email(recipient); // use your own email address here

        String emailSubject = "RAFIKIBORA MICROFINANCE: MONEY WITHDRAWAL TOKEN";
        String content = "Hi, <br/> <p>Please use the token below to withdraw money" +
                " from a Rafikibora Microfinance " +
                "agent</p><br/> <div><strong> " + token +  "</strong></div>" +
                "<br/>  <em>The RafikiBora Team</em>";
        Content emailContent = new Content("text/html", content);

        Mail mail = new Mail(from, emailSubject, to, emailContent);
        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            System.out.println(response.getStatusCode());
            System.out.println(response.getHeaders());
            System.out.println(response.getBody());

            log.info("Email with withdrawal token sent!");

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            log.info("Exception occurred when sending email: " + ex.getMessage());
            throw new RafikiBoraException("Exception occurred when sending email to " +
                    recipient);
        }

    }

}