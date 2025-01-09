package com.example.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.builder.SearchSourceBuilder;

import java.io.IOException;

@Path("/api/opensearch")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OpenSearchResource {

    @Inject
    RestHighLevelClient client;

    @GET
    @Path("/search")
    public Response search(@QueryParam("index") String index, @QueryParam("query") String query) {
        try {
            SearchRequest searchRequest = new SearchRequest(index);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.queryStringQuery(query));
            searchRequest.source(sourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, org.opensearch.client.RequestOptions.DEFAULT);

            return Response.ok(searchResponse.toString()).build();
        } catch (IOException e) {
            return Response.serverError().entity("Error: " + e.getMessage()).build();
        }
    }
}
