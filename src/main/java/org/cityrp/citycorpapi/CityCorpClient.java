package org.cityrp.citycorpapi;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.cityrp.citycorpapi.http.Headers;
import org.cityrp.citycorpapi.http.Response;
import org.cityrp.citycorpapi.http.TLSSocketFactory;
import org.cityrp.citycorpapi.requests.Request;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class CityCorpClient implements Authorization {

    private static final String BASE_URL = "https://api.cityrp.org/citycorp/";

    private final UUID uuid;
    private final String token;

    private final SSLSocketFactory sslSocketFactory;

    private int connectTimeout;
    private int readTimeout;

    public CityCorpClient(UUID uuid, String token) {
        this.uuid = uuid;
        this.token = token;
        try {
            this.sslSocketFactory = new TLSSocketFactory();
        } catch (SSLException e) {
            throw new RuntimeException(e);
        }

        this.readTimeout = (int) TimeUnit.SECONDS.toMillis(30L);
        this.connectTimeout = this.readTimeout;
    }

    @Override
    public String authorizationString() {
        return String.format("Basic %s", new String(Base64.getEncoder().encode((this.uuid + ":" + this.token).getBytes())));
    }

    @Override
    public int hashCode() {
        return (this.uuid.toString() + this.token).hashCode();
    }

    public <E, T extends Request<E>> Response<E> execute(T request) throws IOException {
        HttpURLConnection connection = this.getConnection(request);
        InputStream inputStream = connection.getInputStream();
        Headers responseHeaders = this.parseResponseHeaders(connection);
        Class<?> clazz = request.getResponseClass();
        if (clazz != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION);
            E result = objectMapper.readValue(inputStream, (Class<E>) clazz);
            return new Response<>(responseHeaders, connection.getResponseCode(), result);
        }
        return new Response<>(responseHeaders, connection.getResponseCode(), null);
    }

    HttpURLConnection getConnection(Request<?> request) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) URL.of(URI.create(BASE_URL + request.getPath()), null).openConnection();
        if (connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection)connection).setSSLSocketFactory(this.sslSocketFactory);
        }
        connection.setReadTimeout(this.getReadTimeout());
        connection.setConnectTimeout(this.getConnectTimeout());
        connection.setRequestMethod(request.getRequestType().name());
        this.applyHeadersFromRequest(connection, request);
        return connection;
    }

    private void applyHeadersFromRequest(HttpURLConnection connection, Request<?> request) {
        for (String key : request.getHeaders()) {
            connection.setRequestProperty(key, request.getHeaders().header(key));
        }
        connection.setRequestProperty("Authorization", this.authorizationString());
    }

    Headers parseResponseHeaders(URLConnection connection) {
        Headers headers = new Headers();
        for(String key : connection.getHeaderFields().keySet()) {
            headers.header(key, connection.getHeaderField(key));
        }
        return headers;
    }
}
