package org.cityrp.citycorpapi.requests.corp.get;

import org.cityrp.citycorpapi.api.Corp;
import org.cityrp.citycorpapi.http.Headers;
import org.cityrp.citycorpapi.requests.Request;
import org.cityrp.citycorpapi.requests.RequestType;

public class GetCorpByID extends Request<Corp> {

    public GetCorpByID(long id) {
        super("corp?corp_id=%s", RequestType.GET, Corp.class);
        this.replacePathVariables(id);
        this.header(Headers.CONTENT_TYPE, "application/json");
    }
}
