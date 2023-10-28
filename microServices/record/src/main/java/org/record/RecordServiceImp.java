package org.record;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.record.client.NutritionIntakeClient;
import org.record.client.NutritionIntakeCreateDto;
import org.record.client.NutritionIntakeView;
import org.record.exeptions.RecordCreationException;
import org.record.exeptions.RecordNotFoundException;
import org.record.model.dtos.RecordCreateDto;
import org.record.model.dtos.RecordView;
import org.record.model.dtos.UserView;
import org.record.model.entity.Record;
import org.record.model.enums.Gender;
import org.record.model.enums.WorkoutState;
import org.record.utils.RecordValidator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordServiceImp {


    private final RecordRepository recordRepository;
    private final NutritionIntakeClient nutritionIntakeClient;
    private final Gson gson;

    public List<RecordView> getAllViewsByUserId(String userToken) {
        return recordRepository.findAll()
                .stream()
                .filter(record -> record.getUserId().equals(getUserId(userToken).getId()))
                .map(record -> {
                    List<NutritionIntakeView> allNutritionIntakesWithRecordId = nutritionIntakeClient.getAllNutritionIntakesWithRecordId(record.getId());
                    return new RecordView(record.getId(),
                            allNutritionIntakesWithRecordId,
                            record.getDailyCalories(),
                            record.getUserId());
                })
                .collect(Collectors.toList());
    }

    public RecordView getViewByRecordId(Long day) throws RecordNotFoundException {

        return recordRepository.findById(day)
                .map(record -> {
                    List<NutritionIntakeView> allNutritionIntakesWithRecordId = nutritionIntakeClient.getAllNutritionIntakesWithRecordId(record.getId());
                    return new RecordView(record.getId(),
                            allNutritionIntakesWithRecordId,
                            record.getDailyCalories(),
                            record.getUserId());
                })
                .orElseThrow(() -> new RecordNotFoundException(day.toString()));
    }

    public void addNewRecordByUserId(String userToken) throws RecordCreationException {
        UserView user = getUserId(userToken);

        RecordCreateDto recordCreateDto = RecordCreateDto
                .builder()
                .age(user.getAge())
                .height(new BigDecimal(user.getHeight()))
                .kilograms(new BigDecimal(user.getKilograms()))
                .gender(Gender.valueOf(user.getGender()))
                .workoutState(WorkoutState.valueOf(user.getWorkoutState()))
                .build();

        RecordValidator.validateRecord(recordCreateDto);

        Record record = new Record();

        BigDecimal BMR = getBmr(recordCreateDto);
        BigDecimal caloriesPerDay = getCaloriesPerDay(recordCreateDto, BMR);

        record.setDailyCalories(caloriesPerDay);
        record.setUserId(user.getId());

        recordRepository.saveAndFlush(record);

        NutritionIntakeCreateDto intakeCreateDto = new NutritionIntakeCreateDto(
                record.getId(),
                Gender.valueOf(user.getGender()),
                record.getDailyCalories(),
                WorkoutState.valueOf(user.getWorkoutState()));

//        rabbitTemplate.convertAndSend(
//                "record-exchange",
//                "creation-key",
//                intakeCreateDto);
    }

    public void deleteById(Long recordId, String userToken) throws RecordNotFoundException {
        Record record = recordRepository.findByIdAndUserId(recordId, getUserId(userToken).getId())
                .orElseThrow(() -> new RecordNotFoundException(recordId.toString()));

//        rabbitTemplate.convertAndSend("record-exchange" , "deletion-key" , recordId);

        recordRepository.delete(record);
    }


    private static BigDecimal getCaloriesPerDay(RecordCreateDto recordCreateDto, BigDecimal BMR) {
        return switch (recordCreateDto.workoutState) {
            case SEDENTARY -> BMR.multiply(new BigDecimal("1.2"));
            case LIGHTLY_ACTIVE -> BMR.multiply(new BigDecimal("1.375"));
            case MODERATELY_ACTIVE -> BMR.multiply(new BigDecimal("1.55"));
            case VERY_ACTIVE -> BMR.multiply(new BigDecimal("1.725"));
            case SUPER_ACTIVE -> BMR.multiply(new BigDecimal("1.9"));
        };
    }

    private static BigDecimal getBmr(RecordCreateDto recordCreateDto) {
        BigDecimal BMR;

        if (recordCreateDto.gender.equals(Gender.MALE)) {
            BMR = new BigDecimal("88.362")
                    .add(new BigDecimal("13.397").multiply(recordCreateDto.getKilograms()))
                    .add(new BigDecimal("4.799").multiply(recordCreateDto.getHeight()))
                    .subtract(new BigDecimal("5.677").add(new BigDecimal(recordCreateDto.getAge())));
        } else {
            BMR = new BigDecimal("447.593 ")
                    .add(new BigDecimal("9.247").multiply(recordCreateDto.getKilograms()))
                    .add(new BigDecimal("3.098").multiply(recordCreateDto.getHeight()))
                    .subtract(new BigDecimal("4.330").add(new BigDecimal(recordCreateDto.getAge())));
        }
        return BMR;
    }

    private UserView getUserId(String userToken) {
        return gson.fromJson(userToken, UserView.class);
    }
}
