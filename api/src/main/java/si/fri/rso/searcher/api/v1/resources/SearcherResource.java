package si.fri.rso.searcher.api.v1.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;
import com.kumuluz.ee.logs.cdi.Log;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.kumuluz.ee.cors.annotations.CrossOrigin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import si.fri.rso.searcher.lib.Polnilnice;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
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
@CrossOrigin(allowOrigin = "*")
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

        polnilnice_host = Optional.of("http://20.82.85.106/polnilnica/v1/polnilnice");

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

        // MapQuest API
        JSONObject jsonBody = new JSONObject();
        JSONArray coordArray = new JSONArray();

        JSONObject currCoord = new JSONObject()
                .put("latLng", new JSONObject()
                        .put("lat", curr_north)
                        .put("lng", curr_east));
        coordArray.put(currCoord);

        JSONObject options = new JSONObject().put("manyToOne", true);
        jsonBody.put("options", options);

        for (int i=0; i < polnilniceList.size(); i++) {
            JSONObject coords = new JSONObject()
                    .put("latLng", new JSONObject()
                            .put("lat", polnilniceList.get(i).getCoord_north())
                            .put("lng", polnilniceList.get(i).getCoord_east()));
            coordArray.put(coords);
        }
        jsonBody.put("locations", coordArray);

        String mapQuestApiResponse = myHttpPost(distanceapi, jsonBody.toString());
        JSONObject mapQuestJson;
        JSONArray distances;
        JSONArray times;
        JSONArray locations;

        try {
            mapQuestJson = new JSONObject(mapQuestApiResponse);
            distances = (JSONArray) mapQuestJson.get("distance");
            times = (JSONArray) mapQuestJson.get("time");
            locations = (JSONArray) mapQuestJson.get("locations");
        } catch (JSONException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("External API JSON response parsing error").build();
        }

        for (int i=0; i < polnilniceList.size(); i++) {
            Polnilnice element = polnilniceList.get(i);
            element.setDistance((Double) distances.get(i+1));
            element.setTime((Integer) times.get(i+1));
            JSONObject location = (JSONObject) locations.get(i+1);
            element.setCity((String) location.get("adminArea5"));
            element.setStreet((String) location.get("street"));
        }

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
