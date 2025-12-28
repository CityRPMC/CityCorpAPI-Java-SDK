package org.cityrp.citycorpapi.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CorpType {

    private String ID;
    private String name;
    private List<CorpMeta> corpMeta;
}
