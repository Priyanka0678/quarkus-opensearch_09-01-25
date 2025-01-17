
package com.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.delete.DeleteRequest;
import org.opensearch.action.get.GetRequest;
import org.opensearch.action.update.UpdateRequest;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class AccommodationService {

    @Inject
    RestHighLevelClient client;

    private static final String INDEX = "accommodations";

    // Ensure the index exists
    public void ensureIndexExists() throws IOException {
        if (!client.indices().exists(new org.opensearch.client.indices.GetIndexRequest(INDEX), RequestOptions.DEFAULT)) {
            createIndex();
        }
    }

    // Create index if it doesn't exist
    private void createIndex() throws IOException {
        org.opensearch.client.indices.CreateIndexRequest createIndexRequest = new org.opensearch.client.indices.CreateIndexRequest(INDEX);
        client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    }

    // Create document
    public void createDocument(String id, Map<String, Object> document) throws IOException {
        ensureIndexExists();
        IndexRequest indexRequest = new IndexRequest(INDEX).id(id).source(document);
        client.index(indexRequest, RequestOptions.DEFAULT);
    }

    // Update document (PUT functionality)
    public void updateDocument(String id, Map<String, Object> updatedFields) throws IOException {
        ensureIndexExists();

        // Prepare the update request
        UpdateRequest updateRequest = new UpdateRequest(INDEX, id).doc(updatedFields);
        client.update(updateRequest, RequestOptions.DEFAULT);
    }

    // Read document
    public Map<String, Object> getDocument(String id) throws IOException {
        GetRequest getRequest = new GetRequest(INDEX, id);
        return client.get(getRequest, RequestOptions.DEFAULT).getSource();
    }

    // Delete document
    public void deleteDocument(String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX, id);
        client.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    // Search documents
    public List<Map<String, Object>> searchDocuments(String field, String value) throws IOException {
        SearchRequest searchRequest = new SearchRequest(INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(field, value));
        searchRequest.source(searchSourceBuilder);
    
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        List<Map<String, Object>> results = new ArrayList<>();
        searchResponse.getHits().forEach(hit -> results.add(hit.getSourceAsMap()));
        return results;
    }
    
}

