package sendgrid;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.smallrye.common.annotation.Blocking;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import static java.lang.System.getenv;

@Path("/mail")
public class MailResource {

    @Inject
    Mailer mailer;

    @GET
    @Blocking
    public void sendEmail(@QueryParam("address") String address) {
        mailer.send(
            Mail.withText(address, "Ahoy from Quarkus", "A simple email sent from a Quarkus application.")
        );
    }

}