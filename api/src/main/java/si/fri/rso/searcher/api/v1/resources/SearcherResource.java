package si.fri.rso.searcher.api.v1.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;
import com.kumuluz.ee.logs.cdi.Log;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import si.fri.rso.searcher.lib.Polnilnice;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Optional;


@Log
@ConfigBundle("external-api")
@ApplicationScoped
@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SearcherResource {

    private Optional<String> polnilnice_host;

    @Context
    protected UriInfo uriInfo;

    @ConfigValue(watch = true)
    private String distanceapi;

    public String getDistanceapi() { return this.distanceapi; }

    public void setDistanceapi(final String distanceapi) {
        this.distanceapi = distanceapi;
    }

    CloseableHttpClient httpClient = HttpClients.createDefault();
    ObjectMapper mapper = new ObjectMapper();

    @GET
    @Path("/curr_coord={curr_north},{curr_east}")
    public Response getPolnilnice(@PathParam("curr_north") Float curr_north, @PathParam("curr_east") Float curr_east) {
        List<Polnilnice> polnilniceList = null;

        polnilnice_host = Optional.of("http://localhost:8080");

        String polnilniceString = myHttpGet(polnilnice_host.get() + "/v1/polnilnice", null);

        System.out.println("Trying to get polnilnice...");
        System.out.println(polnilniceString);

        try {
            polnilniceList = mapper.readValue(polnilniceString, new TypeReference<List<Polnilnice>>() {});
            System.out.println("TU1");
        } catch (IOException e) {
            System.out.println("TU2");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

        // TODO: External api za racunanje poti

        return Response.status(Response.Status.OK).entity(polnilniceList).build();
    }



    private String myHttpPost(String url, String jsonbody) {
        HttpPost request = new HttpPost(url);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = null;
        try {
            request.setEntity(new StringEntity(jsonbody));
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
        try {
            response = httpClient.execute(request);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            return  e.getMessage();
        }
    }

    private String myHttpGet(String url, String body) {
        HttpGet request = new HttpGet(url);
        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(request);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            return  e.getMessage();
        }
    }

}
