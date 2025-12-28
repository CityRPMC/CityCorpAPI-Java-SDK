package org.cityrp.citycorpapi.requests.corp.get.account;

import org.cityrp.citycorpapi.api.Corp;
import org.cityrp.citycorpapi.api.account.CorpAccount;
import org.cityrp.citycorpapi.http.Headers;
import org.cityrp.citycorpapi.requests.Request;
import org.cityrp.citycorpapi.requests.RequestType;

public class GetAccountByName extends Request<CorpAccount> {

    public GetAccountByName(Corp corp, String accountName) {
        super("corp/accounts?corp_id=%s&account_name=%s", RequestType.GET, CorpAccount.class);
        this.replacePathVariables(corp.getID(), accountName);
        this.header(Headers.CONTENT_TYPE, "application/json");
    }
}
