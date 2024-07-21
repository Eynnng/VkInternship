import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetUserGroupsTest {

    private static final String BASE_URL = "https://apiok.ru/dev/methods/rest/group/group.getUserGroups";

    @Test
    public void testValidRequest() throws Exception {
        String userId = "valid_user_id";
        String accessToken = "valid_access_token";
        String url = BASE_URL + "?user_id=" + userId + "&access_token=" + accessToken;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                assertEquals(200, response.getStatusLine().getStatusCode());
                String responseBody = EntityUtils.toString(response.getEntity());
                assertTrue(responseBody.contains("groups"));
            }
        }
    }

    @Test
    public void testInvalidUserId() throws Exception {
        String userId = "invalid_user_id";
        String accessToken = "valid_access_token";
        String url = BASE_URL + "?user_id=" + userId + "&access_token=" + accessToken;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                assertTrue(response.getStatusLine().getStatusCode() != 200);
            }
        }
    }

    @Test
    public void testInvalidAccessToken() throws Exception {
        String userId = "valid_user_id";
        String accessToken = "invalid_access_token";
        String url = BASE_URL + "?user_id=" + userId + "&access_token=" + accessToken;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                assertTrue(response.getStatusLine().getStatusCode() != 200);
            }
        }
    }

    @Test
    public void testMissingUserId() throws Exception {
        String accessToken = "valid_access_token";
        String url = BASE_URL + "?access_token=" + accessToken;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                assertTrue(response.getStatusLine().getStatusCode() != 200);
            }
        }
    }

    @Test
    public void testMissingAccessToken() throws Exception {
        String userId = "valid_user_id";
        String url = BASE_URL + "?user_id=" + userId;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                assertTrue(response.getStatusLine().getStatusCode() != 200);
            }
        }
    }

    @Test
    public void testBoundaryUserId() throws Exception {
        String userId = "1"; // минимально возможный user_id
        String accessToken = "valid_access_token";
        String url = BASE_URL + "?user_id=" + userId + "&access_token=" + accessToken;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                assertEquals(200, response.getStatusLine().getStatusCode());
                String responseBody = EntityUtils.toString(response.getEntity());
                assertTrue(responseBody.contains("groups"));
            }
        }
    }

    @Test
    public void testPerformance() throws Exception {
        String userId = "valid_user_id_with_many_groups";
        String accessToken = "valid_access_token";
        String url = BASE_URL + "?user_id=" + userId + "&access_token=" + accessToken;

        long startTime = System.currentTimeMillis();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                assertEquals(200, response.getStatusLine().getStatusCode());
                String responseBody = EntityUtils.toString(response.getEntity());
                assertTrue(responseBody.contains("groups"));
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("Performance test duration: " + duration + " ms");
    }
}