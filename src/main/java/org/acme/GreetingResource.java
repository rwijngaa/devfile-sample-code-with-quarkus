package org.acme;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Clock;
import java.time.LocalTime;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Path("/api")
public class GreetingResource {
    @Inject
    Clock clock;

    public Map<String, LocalTime> getFlights() {
        return IntStream.range(1, 1439)
                .boxed()
                .collect(Collectors.toMap(i -> String.format("KL%d00%d00", i / 24, i % 60),
                        i -> LocalTime.of(i / 24, i % 60)));
    }

    enum COLOR {
        RED,
        YELLOW,
        GREEN
    }

    // vluchtnr + departure times
    Map<String, LocalTime> flights = getFlights();

    // /api/KL1111/210
    @GET
    @Path("{flightNr}/{travelTime}")
    @Produces(MediaType.TEXT_PLAIN)
    public COLOR hello(@PathParam("flightNr") String flightNr, @PathParam("travelTime") Integer travelTime) {
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
