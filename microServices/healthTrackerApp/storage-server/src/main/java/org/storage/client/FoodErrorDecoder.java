package org.storage.client;

import java.io.IOException;

import org.storage.exception.FoodNotFoundException;
import org.storage.model.errorResponses.FoodErrorResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

public class FoodErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        if (response.status() == 400) {
            try {
                String responseBody = Util.toString(response.body().asReader());
                ObjectMapper objectMapper = new ObjectMapper();
                FoodErrorResponse foodErrorResponse = objectMapper.readValue(responseBody, FoodErrorResponse.class);

                throw new FoodNotFoundException(foodErrorResponse);
            } catch (IOException | FoodNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException();
    }
}
