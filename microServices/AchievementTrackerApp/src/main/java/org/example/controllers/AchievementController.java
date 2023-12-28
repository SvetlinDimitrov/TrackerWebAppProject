package org.example.controllers;

import java.util.List;

import org.example.domain.dto.AchievementHolderCreateDto;
import org.example.domain.dto.AchievementTrackerEditDto;
import org.example.domain.dto.AchievementTrackerView;
import org.example.domain.dto.SingleErrorResponse;
import org.example.domain.entity.Achievement;
import org.example.exceptions.InvalidJsonTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/achievement", headers = "X-ViewUser")
@Tag(name = "Achievement", description = "The Achievement API calculates and provides all the interval dates between the added dates. It provides the weeks, months, and years.")
public interface AchievementController {

    @GetMapping(path = "/all")
    ResponseEntity<List<AchievementTrackerView>> getAllAchievementHoldersById(
            @RequestHeader(name = "X-ViewUser") String userToken)
            throws InvalidJsonTokenException;

    @GetMapping
    public ResponseEntity<AchievementTrackerView> getAchievementHolderById(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @RequestParam String id)
            throws InvalidJsonTokenException;

    @PostMapping
    public ResponseEntity<HttpStatus> createAchievementHolder(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @Valid @RequestBody AchievementHolderCreateDto dto,
            BindingResult result) throws InvalidJsonTokenException;

    @PatchMapping("/{achId}/addProgress")
    public ResponseEntity<HttpStatus> updateAchievement(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @PathVariable String achId,
            @RequestParam(defaultValue = "false") Boolean replaceDailyProgress,
            @RequestBody Achievement achievement) throws InvalidJsonTokenException;
    
    @PatchMapping("/{achId}")
    public ResponseEntity<HttpStatus> changeAchievementTracker(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @PathVariable String achId,
            @RequestBody AchievementTrackerEditDto dto) throws InvalidJsonTokenException;
    
    @DeleteMapping
    public ResponseEntity<Void> deleteAchievement(
            @RequestHeader(name = "X-ViewUser") String userToken,
            @RequestParam String id)
            throws InvalidJsonTokenException ;
}
