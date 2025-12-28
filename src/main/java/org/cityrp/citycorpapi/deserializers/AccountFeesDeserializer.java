package org.cityrp.citycorpapi.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.cityrp.citycorpapi.api.account.AccountFeeType;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class AccountFeesDeserializer extends JsonDeserializer<Map<AccountFeeType, Double>> {

    @Override
    public Map<AccountFeeType, Double> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        if (node == null || node.isNull()) return null;

        Map<AccountFeeType, Double> fees = new EnumMap<>(AccountFeeType.class);
        if (node.isArray()) {
            for (JsonNode pair : node) {
                if (pair.size() >= 2) putFee(fees, pair.get(0).asText(), pair.get(1).asDouble(0.0));
            }
            return fees;
        }
        if (node.isObject()) node.fields().forEachRemaining(e -> putFee(fees, e.getKey(), e.getValue().asDouble(0.0)));
        return fees;
    }

    private void putFee(Map<AccountFeeType, Double> fees, String key, double value) {
        try {
            fees.put(AccountFeeType.valueOf(key), value);
        } catch (IllegalArgumentException ignored) {
            // unknown enum constant
        }
    }
}

