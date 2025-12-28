package org.cityrp.citycorpapi.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Headers implements Iterable<String> {
    public static final String CONTENT_TYPE = "content-type";
    public static final String AUTHORIZATION = "authorization";
    public static final String USER_AGENT = "user-agent";
    public static final String ACCEPT_ENCODING = "accept-encoding";

    private final Map<String, String> mHeaders = new HashMap<>();
    private final Map<String, String> keyMapping = new HashMap<>();

    public Iterator<String> iterator() {
        return this.mHeaders.keySet().iterator();
    }

    public Headers header(String header, String value) {
        this.mHeaders.put(header, value);
        this.keyMapping.put(header != null ? header.toLowerCase() : null, header);
        return this;
    }

    public Headers headerIfNotPresent(String key, String value) {
        return this.header(key) == null ? this.header(key, value) : this;
    }

    public Headers remove(String key) {
        this.mHeaders.remove(key);
        this.keyMapping.remove(key.toLowerCase());
        return this;
    }

    public String header(String key) {
        String caseKey = (String)this.keyMapping.get(key.toLowerCase());
        return caseKey != null ? (String)this.mHeaders.get(caseKey) : null;
    }
}
