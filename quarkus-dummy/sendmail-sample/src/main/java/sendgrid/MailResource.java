package sendgrid;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

@Path("/mail")
public class MailResource {

    @Inject
    Mailer mailer;

    @Inject
    ReactiveMailer reactiveMailer;

    @GET
    @Blocking
    @Path("single")
    public void sendEmail(@QueryParam("address") String address) {
        mailer.send(Mail.withText(address, "Ahoy from Quarkus", "A simple email sent from a Quarkus application."));
    }

    @GET
    @Path("/async")
    public Uni<Void> asyncSendEmail(@QueryParam("address") String address) {
        return reactiveMailer.send(Mail.withText(address, "A reactive email from quarkus",
                "A simple email sent from a Quarkus application."));
    }

}
