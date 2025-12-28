package org.cityrp.citycorpapi.api.account;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CorpAccount {

    private String name;
    private Map<AccountFeeType, Double> fees;
    private double balance;
}
