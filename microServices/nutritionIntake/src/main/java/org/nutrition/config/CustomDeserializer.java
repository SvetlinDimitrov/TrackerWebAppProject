package org.nutrition.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.nutrition.model.dtos.NutritionIntakeCreateDto;

public class CustomDeserializer implements Deserializer<NutritionIntakeCreateDto> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public NutritionIntakeCreateDto deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                System.out.println("Null received at deserializing");
                return null;
            }
            System.out.println("Deserializing...");
            return objectMapper.readValue(data, NutritionIntakeCreateDto.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to NutritionIntakeCreateDto");
        }
    }
}
