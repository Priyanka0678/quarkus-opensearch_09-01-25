package com.example.service;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.Map;

@Path("/accommodations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccommodationResource {

    @Inject
    AccommodationService service;

    @POST
    @Path("/{id}")
    public Response create(@PathParam("id") String id, Map<String, Object> document) {
        try {
            if (document == null || document.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Document payload is missing or empty").build();
            }
            service.createDocument(id, document);
            return Response.status(Response.Status.CREATED).entity("Document created successfully").build();
        } catch (IOException e) {
            return Response.serverError().entity("Error creating document: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") String id) {
        try {
            Map<String, Object> document = service.getDocument(id);
            if (document == null || document.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Document not found").build();
            }
            return Response.ok(document).build();
        } catch (IOException e) {
            return Response.serverError().entity("Error fetching document: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") String id, Map<String, Object> updatedFields) {
        try {
            if (updatedFields == null || updatedFields.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Update payload is missing or empty").build();
            }
            service.updateDocument(id, updatedFields);
            return Response.ok("Document updated successfully").build();
        } catch (IOException e) {
            return Response.serverError().entity("Error updating document: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") String id) {
        try {
            service.deleteDocument(id);
            return Response.ok("Document deleted successfully").build();
        } catch (IOException e) {
            return Response.serverError().entity("Error deleting document: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/search")
    public Response search(@QueryParam("field") String field, @QueryParam("value") String value) {
        try {
            // Validate input parameters
            if (field == null || field.isEmpty() || value == null || value.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Search parameters 'field' and 'value' are required").build();
            }

            // Call the service method with field and value
            var results = service.searchDocuments(field, value);
            return Response.ok(results).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (IOException e) {
            return Response.serverError().entity("Error searching documents: " + e.getMessage()).build();
        }
    }
}

