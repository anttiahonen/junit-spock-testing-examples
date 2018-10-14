package fi.aalto.testingandqa.chucknorris;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Service
public class ChuckApiClient {

    private static final String RANDOM_CHUCK_JOKE_API = "https://api.chucknorris.io/jokes/random";

    private RestTemplate restTemplate;

    public ChuckApiClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        restTemplate = getSSLRestTemplate();
    }

    private RestTemplate getSSLRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);

        return new RestTemplate(requestFactory);
    }

    public String getRandomChuckNorrisJoke() throws ChuckException {
        ChuckJoke response;
        try {
            response = restTemplate.getForObject(RANDOM_CHUCK_JOKE_API, ChuckJoke.class);
        } catch (Exception e) {
            throw new ChuckException("Could not obtain a new chuck joke");
        }

        return response.getValue();
    }

}
