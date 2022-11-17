package org.acme;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalTime;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

@Path("/api")
public class GreetingResource {
    private static Map<String, LocalTime> getFlights() {
        return IntStream.range(0, 1440)
                .boxed()
                .collect(toMap(i -> String.format("KL%02d%02d", i / 60, i % 60), i -> LocalTime.of(i / 60, i % 60)));
    }

    enum COLOR {
        RED, YELLOW, GREEN
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
        if (LocalTime.now().plusMinutes(travelTime).isBefore(departureTime.minusHours(2))) {
            // Huidige tijd + reistijd < inchecktijd
            return COLOR.RED;
        } else if (!LocalTime.now().isBefore(departureTime.minusHours(2))) {
            // Huidige tijd >= inchecktijd
            return COLOR.GREEN;
        }
        return COLOR.YELLOW;
    }
}
