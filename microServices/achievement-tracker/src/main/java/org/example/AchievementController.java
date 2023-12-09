package org.example;

import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.domain.dto.AchievementHolderCreateDto;
import org.example.domain.dto.AchievementTrackerEditDto;
import org.example.domain.dto.AchievementTrackerView;
import org.example.domain.dto.SingleErrorResponse;
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
        List<AchievementTrackerView> achievementTrackerViews = achievementServiceImp.getAllAchievementViewsWitherUserId(userToken);
        return new ResponseEntity<>(achievementTrackerViews, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<AchievementTrackerView> getAchievementHolderById(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @RequestParam(name = "id") Long id) {
        AchievementTrackerView foods = achievementServiceImp.getAchievementViewById(userToken, id);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createAchievementHolder(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @Valid @RequestBody AchievementHolderCreateDto dto,
            BindingResult result) {

        if (result.hasErrors()) {
            throw new AchievementException("Invalid data :" + result.getFieldErrors().get(0).getDefaultMessage());
        }

        achievementServiceImp.createAchievement(userToken, dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{achId}/addProgress")
    public ResponseEntity<HttpStatus> updateAchievement(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @PathVariable(name = "achId") Long achId,
            @RequestParam(name = "replaceDailyProgress" , defaultValue = "false") Boolean replaceDailyProgress ,
            @RequestBody Achievement achievement) {

        achievementServiceImp.updateAchievement(userToken, achievement , achId , replaceDailyProgress);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{achId}")
    public ResponseEntity<HttpStatus> changeAchievementTracker(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @PathVariable(name = "achId") Long achId,
            @RequestBody AchievementTrackerEditDto dto) {

        achievementServiceImp.editTracker(userToken, dto , achId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAchievement(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @RequestParam(name = "id") Long id) {

        achievementServiceImp.deleteAchievement(userToken, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @ExceptionHandler
    public ResponseEntity<SingleErrorResponse> handleException(AchievementException e) {
        return new ResponseEntity<>(new SingleErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
