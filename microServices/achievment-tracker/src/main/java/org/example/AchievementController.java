package org.example;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.domain.dto.AchievementHolderCreateDto;
import org.example.domain.dto.AchievementTrackerEditDto;
import org.example.domain.dto.AchievementTrackerView;
import org.example.domain.entity.Achievement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/achievement", headers = "X-ViewUser")
public class AchievementController {

    private final AchievementServiceImp achievementServiceImp;

    @GetMapping(path = "/all")
    public ResponseEntity<List<AchievementTrackerView>> getAllAchievementHoldersById(
            @RequestHeader(name = "X-ViewUser") String userToken) {
        List<AchievementTrackerView> foods = achievementServiceImp.getAllAchievementViewsWitherUserId(userToken);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<AchievementTrackerView> getAchievementHolderById(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @RequestParam(name = "id") Long id) {
        AchievementTrackerView foods = achievementServiceImp.getAchievementViewById(userToken, id);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AchievementTrackerView> createAchievementHolder(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @Valid @RequestBody AchievementHolderCreateDto dto,
            BindingResult result) {

        if (result.hasErrors()) {
            throw new AchievementException("Invalid data :" + result.getFieldErrors().get(0).getDefaultMessage());
        }

        AchievementTrackerView achievement = achievementServiceImp.createAchievement(userToken, dto);
        return new ResponseEntity<>(achievement, HttpStatus.CREATED);
    }

    @PatchMapping("/{achievementName}/addProgress")
    public ResponseEntity<AchievementTrackerView> updateAchievement(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @PathVariable(name = "achievementName") String achievementName,
            @RequestParam(name = "replaceDailyProgress" , defaultValue = "false") Boolean replaceDailyProgress ,
            @RequestBody Achievement achievement) {

        AchievementTrackerView achievementHolder = achievementServiceImp.updateAchievement(userToken, achievement , achievementName , replaceDailyProgress);
        return new ResponseEntity<>(achievementHolder, HttpStatus.OK);
    }

    @PatchMapping("/{achievementName}")
    public ResponseEntity<AchievementTrackerView> changeAchievementTracker(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @PathVariable(name = "achievementName") String achievementName,
            @RequestBody AchievementTrackerEditDto dto) {

        AchievementTrackerView achievementHolder = achievementServiceImp.editTracker(userToken, dto , achievementName);
        return new ResponseEntity<>(achievementHolder, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAchievement(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @RequestParam(name = "id") Long id) {

        achievementServiceImp.deleteAchievement(userToken, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @ExceptionHandler
    public ResponseEntity<String> handleException(AchievementException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
