package org.example;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.domain.dto.AchievementCreateDto;
import org.example.domain.dto.AchievementEditDto;
import org.example.domain.dto.AchievementView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/achievement", headers = "X-ViewUser")
public class AchievementController {

    private final AchievementServiceImp achievementServiceImp;

    @GetMapping(path = "/all")
    public ResponseEntity<List<AchievementView>> getAllAchievementsById(
            @RequestHeader(name = "X-ViewUser") String userToken) {
        List<AchievementView> foods = achievementServiceImp.getAllAchievementViewsWitherUserId(userToken);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<AchievementView> getAchievementById(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @RequestParam(name = "id") Long id) {
        AchievementView foods = achievementServiceImp.getAchievementViewById(userToken, id);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AchievementView> createAchievement(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @Valid @RequestBody AchievementCreateDto dto,
            BindingResult result) {

        if (result.hasErrors()) {
            throw new AchievementException("Invalid data :" + result.getFieldErrors().toString());
        }

        AchievementView achievement = achievementServiceImp.createAchievement(userToken, dto);
        return new ResponseEntity<>(achievement, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<AchievementView> updateAchievement(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @RequestParam(name = "id") Long id,
            @RequestBody AchievementEditDto dto) {

        AchievementView achievement = achievementServiceImp.updateAchievement(userToken, dto , id);
        return new ResponseEntity<>(achievement, HttpStatus.OK);
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
