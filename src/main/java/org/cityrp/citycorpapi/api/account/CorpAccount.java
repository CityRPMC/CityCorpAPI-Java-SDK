package org.cityrp.citycorpapi.api.account;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.cityrp.citycorpapi.deserializers.AccountFeesDeserializer;

@Getter
@Setter
public class CorpAccount {

    private String name;
    @JsonDeserialize(using = AccountFeesDeserializer.class)
    private Map<AccountFeeType, Double> fees;
    private double balance;
}
