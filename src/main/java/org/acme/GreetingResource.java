package org.acme;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Path("/api")
public class GreetingResource {
    @Inject
    private Clock clock;

    enum COLOR {
        RED,
        YELLOW,
        GREEN;
    }

    // vluchtnr + departure times
    Map<String, LocalTime> flights = Map.of(
            "KL1111", LocalTime.of(10, 00),
            "KL2222", LocalTime.of(18, 00)
    );

    // /api/KL1111/210
    @GET @Path("{flightNr}/{travelTime}")
    @Produces(MediaType.TEXT_PLAIN)
    public COLOR hello( @PathParam("flightNr") String flightNr,  @PathParam("travelTime") Integer travelTime) {
        System.out.println("Hello Schiphol: " + flightNr + ", travelTime=" + travelTime);
        final LocalTime departureTime = flights.get(flightNr);
        if (LocalTime.now(clock).plusMinutes(travelTime).isBefore(departureTime.minusHours(2))) {
            // Huidige tijd + reistijd < inchecktijd
            return COLOR.RED;
        } else if (!LocalTime.now(clock).isBefore(departureTime.minusHours(2))) {
            // Huidige tijd >= inchecktijd
            return COLOR.GREEN;
        }
        return COLOR.YELLOW;
    }
}
