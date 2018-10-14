package fi.aalto.testingandqa.chucknorris;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;

@RestController
@RequestMapping("/chucknorris")
public class ChuckNorrisController {

    private static final String RANDOM_CHUCK_JOKE_API = "https://api.chucknorris.io/jokes/random";

    private static final List<String> OTHER_STARS = Arrays.asList(
            "Sylvester Stallone",
            "Arnold Schwarzenegger",
            "Steven Seagal"
    );

    @RequestMapping("/randomjoke")
    public ResponseEntity<String> getRandomJokeWithChuckAndOtherStar() throws ChuckException, KeyStoreException,
                                                                NoSuchAlgorithmException, KeyManagementException {
        RestTemplate restTemplate = getSSLRestTemplate();
        ChuckJoke response;
        try {
            response = restTemplate.getForObject(RANDOM_CHUCK_JOKE_API, ChuckJoke.class);
        } catch (Exception e) {
             throw new ChuckException("Could not obtain a new chuck joke");
        }

        String chuckNorrisJoke = response.getValue();

        String[] wordsInJoke = chuckNorrisJoke.split(" ");
        List<String> newJokeWords = new ArrayList<>();

        boolean previousIndexContainsName = false;
        String previousIndexWord = null;

        for (int i = 0; i < wordsInJoke.length; i++) {
            String word = wordsInJoke[i];
            boolean wordFirstCharactersIsUpperCase = Character.isUpperCase(word.codePointAt(0));
            if (wordFirstCharactersIsUpperCase && previousIndexContainsName) {
                if (previousIndexWord.contains("Chuck") && word.contains("Norris")) {
                    newJokeWords.add(previousIndexWord);
                    newJokeWords.add(word);
                } else {
                    Random random = new Random();
                    int index = random.nextInt(OTHER_STARS.size());
                    String otherStar = OTHER_STARS.get(index);
                    newJokeWords.add(otherStar);
                }
                previousIndexWord = null;
                previousIndexContainsName = false;
            }
            else if (!wordFirstCharactersIsUpperCase && previousIndexContainsName) {
                newJokeWords.add(previousIndexWord);
                newJokeWords.add(word);
                previousIndexWord = null;
                previousIndexContainsName = false;
            }
            else if (wordFirstCharactersIsUpperCase) {
                previousIndexContainsName = true;
                previousIndexWord = word;
            }
            else
                newJokeWords.add(word);
        }

        String newJoke = String.join(" ", newJokeWords);

        newJoke += "<br> original joke:" + chuckNorrisJoke;

        return new ResponseEntity<>(newJoke, HttpStatus.OK);
    }

    private RestTemplate getSSLRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException  {
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

}
