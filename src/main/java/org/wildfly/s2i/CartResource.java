package org.wildfly.s2i;

import java.util.Enumeration;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class CartResource {

    @POST
    @Path("{item}/{quantity}")
    public Response addToCart(@Context HttpServletRequest request, @PathParam("item") String item, @PathParam("quantity") int quantity) {
        HttpSession session = request.getSession();
        session.setAttribute(item, quantity);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("cart")
    public JsonObject getCart(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder cartData = Json.createArrayBuilder();

        objectBuilder.add("host", getHostName());
        objectBuilder.add("sessionId", session.getId());

        Enumeration<String> items = session.getAttributeNames();
        while (items.hasMoreElements()) {
            String item = items.nextElement();
            cartData.add(Json.createObjectBuilder()
                    .add("item", item)
                    .add("quantity", (Integer) session.getAttribute(item))
            );
        }
        objectBuilder.add("cart", cartData);

        return objectBuilder.build();
    }

    private String getHostName() {
        final String hostname = System.getenv("HOSTNAME");
        return hostname == null ? "localhost" : hostname;
    }
}
