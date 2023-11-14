package org.nutrition.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nutrition.NutritionIntakeRepository;
import org.nutrition.exceptions.RecordNotFoundException;
import org.nutrition.model.dtos.NutritionIntakeView;
import org.nutrition.model.entity.NutritionIntake;

@ExtendWith(MockitoExtension.class)
public class NutrientIntakeServiceTest {
    @Mock
    private NutritionIntakeRepository repository;

    @InjectMocks
    private NutrientIntakeService service;

    private final List<NutritionIntake> nutritionIntakes = new ArrayList<>();
    private final List<NutritionIntakeView> nutritionIntakesViews = new ArrayList<>();

    @BeforeEach
    public void setUp() {

        NutritionIntake nutritionIntake = new NutritionIntake();
        nutritionIntake.setId(1L);
        nutritionIntake.setNutrientName("A");
        nutritionIntake.setNutrientType("Vitamin");
        nutritionIntake.setDailyConsumed(BigDecimal.valueOf(100));
        nutritionIntake.setLowerBoundIntake(BigDecimal.valueOf(50));
        nutritionIntake.setUpperBoundIntake(BigDecimal.valueOf(100));
        nutritionIntake.setMeasurement("mg");
        nutritionIntake.setRecordId(1L);

        nutritionIntakes.add(nutritionIntake);

        NutritionIntake nutritionIntake2 = new NutritionIntake();
        nutritionIntake2.setId(2L);
        nutritionIntake2.setNutrientName("B");
        nutritionIntake2.setNutrientType("Vitamin");
        nutritionIntake2.setDailyConsumed(BigDecimal.valueOf(100));
        nutritionIntake2.setLowerBoundIntake(BigDecimal.valueOf(50));
        nutritionIntake2.setUpperBoundIntake(BigDecimal.valueOf(100));
        nutritionIntake2.setMeasurement("mg");
        nutritionIntake2.setRecordId(2L);

        nutritionIntakes.add(nutritionIntake2);

        NutritionIntakeView nutritionIntakeView = new NutritionIntakeView();
        nutritionIntakeView.setId(1L);
        nutritionIntakeView.setNutrientName("A");
        nutritionIntakeView.setNutrientType("Vitamin");
        nutritionIntakeView.setDailyConsumed(BigDecimal.valueOf(100));
        nutritionIntakeView.setLowerBoundIntake(BigDecimal.valueOf(50));
        nutritionIntakeView.setUpperBoundIntake(BigDecimal.valueOf(100));
        nutritionIntakeView.setMeasurement("mg");
        nutritionIntakeView.setRecordId(1L);

        nutritionIntakesViews.add(nutritionIntakeView);

        NutritionIntakeView nutritionIntakeView2 = new NutritionIntakeView();
        nutritionIntakeView2.setId(2L);
        nutritionIntakeView2.setNutrientName("B");
        nutritionIntakeView2.setNutrientType("Vitamin");
        nutritionIntakeView2.setDailyConsumed(BigDecimal.valueOf(100));
        nutritionIntakeView2.setLowerBoundIntake(BigDecimal.valueOf(50));
        nutritionIntakeView2.setUpperBoundIntake(BigDecimal.valueOf(100));
        nutritionIntakeView2.setMeasurement("mg");
        nutritionIntakeView2.setRecordId(2L);

        nutritionIntakesViews.add(nutritionIntakeView2);
    }

    @Test
    public void testGetAllNutritionIntakeByRecordId_ValidRecordId_TransformCorrectlyIntoViewModel()
            throws RecordNotFoundException {
        Long validRecordId = 1L;
        when(repository.findAllByRecordId(validRecordId)).thenReturn(Optional.of(nutritionIntakes));

        List<NutritionIntakeView> result = service.getAllNutritionIntakeByRecordId(validRecordId);

        assertEquals(nutritionIntakesViews, result);
    }

    @Test
    public void testGetAllNutritionIntakeByRecordId_InvalidRecordId_ThrowsException() {
        Long invalidRecordId = 99L;
        when(repository.findAllByRecordId(invalidRecordId)).thenReturn(Optional.of(List.of()));

        assertThrows(RecordNotFoundException.class,
                () -> service.getAllNutritionIntakeByRecordId(invalidRecordId));
    }
}
