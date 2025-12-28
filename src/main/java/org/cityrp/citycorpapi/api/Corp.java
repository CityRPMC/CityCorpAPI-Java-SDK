package org.cityrp.citycorpapi.api;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class Corp {

    private long id;
    private String name;
    private UUID owner;
    private CorpType corpType;
    private double balance;
    private Map<String, Double> valuationBreakdown;
    private double totalValuation;
    private String description;
    private String discordLink;
    private long hqPlotID;
    private String ticker;
    private String dividend;
    private long lastDividendPayout;
}
