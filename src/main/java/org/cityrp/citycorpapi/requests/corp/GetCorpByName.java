package org.cityrp.citycorpapi.requests.corp;

import org.cityrp.citycorpapi.api.Corp;
import org.cityrp.citycorpapi.http.Headers;
import org.cityrp.citycorpapi.requests.Request;
import org.cityrp.citycorpapi.requests.RequestType;

public class GetCorpByName extends Request<Corp> {

    public GetCorpByName(String corpName) {
        super("corp?corp_name=%s", RequestType.GET, Corp.class);
        this.replacePathVariables(corpName);
        this.header(Headers.CONTENT_TYPE, "application/json");
    }
}
