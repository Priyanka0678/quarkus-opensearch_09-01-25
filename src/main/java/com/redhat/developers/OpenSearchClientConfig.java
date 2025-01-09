// 

package com.redhat.developers;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.net.URIBuilder;

import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.RestClientBuilder;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OpenSearchClientConfig {

    private static final String OPENSEARCH_URL = "https://localhost:9200";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "Priyanka@123";

    public RestHighLevelClient createClient() {
        // Create credentials provider with authentication details
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
            new AuthScope("localhost", 9200),
            new UsernamePasswordCredentials(USERNAME, PASSWORD.toCharArray())
        );

        // Create the RestHighLevelClient with the custom HTTP client
        RestClientBuilder builder = RestClient.builder(
                new HttpHost("localhost", 9200, "https"))
                .setHttpClientConfigCallback(httpClientBuilder -> 
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));

        return new RestHighLevelClient(builder);
    }
}
