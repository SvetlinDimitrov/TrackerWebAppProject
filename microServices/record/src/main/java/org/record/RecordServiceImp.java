package org.record;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.record.exeptions.RecordNotFoundException;
import org.record.model.dtos.RecordCreateDto;
import org.record.model.dtos.RecordView;
import org.record.model.entity.Record;
import org.record.model.enums.Gender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordServiceImp {


    private final RecordRepository recordRepository;

    public List<RecordView> getAllViewsByUserId(Long userId) {
        return recordRepository.findAll()
                .stream()
                .filter(record -> record.getUserId().equals(userId))
                .map(this::toRecordView)
                .collect(Collectors.toList());
    }

    public RecordView getViewByRecordId(Long day) throws RecordNotFoundException {

        return recordRepository.findById(day)
                .map(this::toRecordView)
                .orElseThrow(() -> new RecordNotFoundException(day.toString()));
    }


    public RecordView addNewRecordByUserId(Long userId, @Valid RecordCreateDto recordCreateDto) {

        Record record = createRecord(userId , recordCreateDto);
        recordRepository.save(record);

        return toRecordView(record);
    }

    public void deleteById(Long recordId, Long userId) throws RecordNotFoundException {
        Record record = recordRepository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new RecordNotFoundException(recordId.toString()));

        //TODO: DELETE ALL NUTRITION'S WITH THAT RECORD , HTTP REQUEST TO NUTRITION SERVER

        recordRepository.delete(record);
    }

    private Record createRecord(Long id, @Valid RecordCreateDto recordCreateDto) {

        Record record = new Record();
        BigDecimal BMR = getBmr(recordCreateDto);

        BigDecimal caloriesPerDay = switch (recordCreateDto.workoutState) {
            case SEDENTARY -> BMR.multiply(new BigDecimal("1.2"));
            case LIGHTLY_ACTIVE -> BMR.multiply(new BigDecimal("1.375"));
            case MODERATELY_ACTIVE -> BMR.multiply(new BigDecimal("1.55"));
            case VERY_ACTIVE -> BMR.multiply(new BigDecimal("1.725"));
            case SUPER_ACTIVE -> BMR.multiply(new BigDecimal("1.9"));
        };

        record.setDailyCalories(caloriesPerDay);
        record.setUserId(id);
        /*
        //TODO: SET NEW NUTRITION VALUES . MAKE HTTP REQUEST TO NUTRITION SERVICE
        record.setDailyIntakeViews(nutrientIntakeService
                .create(user.getGender(), caloriesPerDay, user.getWorkoutState(), record));


         */

        return record;
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

    public void deleteById(Long recordId) throws RecordNotFoundException {
        recordRepository.deleteById(recordId);
    }

    private RecordView toRecordView (Record record){
        return RecordView.builder()
                .id(record.getId())
                .userID(record.getUserId())
                .dailyCaloriesToConsume(record.getDailyCalories())
                .build();
    }


}
