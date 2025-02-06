package com.dammy.bookstoreapi.utils;


import com.dammy.bookstoreapi.model.PaymentMethod;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.core.JsonParseException;

import java.io.IOException;

public class PaymentMethodDeserializer extends JsonDeserializer<PaymentMethod> {

    @Override
    public PaymentMethod deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonParseException {
        String paymentMethodStr = jp.getText();
        if (paymentMethodStr == null || paymentMethodStr.isEmpty()) {
            throw new JsonParseException(jp, "Payment method cannot be empty");
        }
        return PaymentMethod.valueOf(paymentMethodStr);
    }
}