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
    @Operation(summary = "Get all achievement holders by ID", responses = {
            @ApiResponse(responseCode = "200", description = "List of all achievement holders", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AchievementTrackerView.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json header", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class)))

    })
    public ResponseEntity<List<AchievementTrackerView>> getAllAchievementHoldersById(
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken)
            throws InvalidJsonTokenException;

    @GetMapping
    @Operation(summary = "Get achievement holder by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Achievement holder details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AchievementTrackerView.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json header", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class)))

    })
    public ResponseEntity<AchievementTrackerView> getAchievementHolderById(
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken,
            @Parameter(description = "Achievement holder ID.", example = "1") @RequestParam Long id)
            throws InvalidJsonTokenException;

    @PostMapping
    @Operation(summary = "Create achievement holder", responses = {
            @ApiResponse(responseCode = "201", description = "Achievement holder created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json header", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class)))

    }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = AchievementHolderCreateDto.class), examples = @ExampleObject(name = "achievementHolderCreateDto", value = "{\"name\":\"Achievement1\",\"description\":\"This is a sample achievement\",\"goal\":50,\"measurement\":\"kilograms\"}"))))
    public ResponseEntity<HttpStatus> createAchievementHolder(
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken,
            @Valid @RequestBody AchievementHolderCreateDto dto,
            BindingResult result) throws InvalidJsonTokenException;

    @PatchMapping("/{achId}/addProgress")
    @Operation(summary = "Update achievement", responses = {
            @ApiResponse(responseCode = "200", description = "Achievement updated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json header", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class)))

    }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = Achievement.class), examples = @ExampleObject(name = "achievement", value = "{\"date\":\"2022-12-31\",\"progress\":50}"))))
    public ResponseEntity<HttpStatus> updateAchievement(
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken,
            @Parameter(description = "Achievement ID.", example = "1") @PathVariable Long achId,
            @Parameter(description = "Replace daily progress.", example = "false") @RequestParam(defaultValue = "false") Boolean replaceDailyProgress,
            @RequestBody Achievement achievement) throws InvalidJsonTokenException;
    
    @PatchMapping("/{achId}")
    @Operation(summary = "Change achievement tracker", responses = {
            @ApiResponse(responseCode = "204", description = "Achievement tracker changed", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json header", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class)))

    }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = AchievementTrackerEditDto.class), examples = @ExampleObject(name = "achievementHolderCreateDto", value = "{\"name\":\"Achievement1\",\"description\":\"This is a sample achievement\",\"goal\":100,\"measurement\":\"kilograms\"}"))))
    public ResponseEntity<HttpStatus> changeAchievementTracker(
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken,
            @Parameter(description = "Achievement ID.", example = "1") @PathVariable Long achId,
            @RequestBody AchievementTrackerEditDto dto) throws InvalidJsonTokenException;
    
    @DeleteMapping
    @Operation(summary = "Delete achievement", responses = {
            @ApiResponse(responseCode = "204", description = "Achievement deleted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json header", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class)))

    })
    public ResponseEntity<Void> deleteAchievement(
            @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken,
            @Parameter(description = "Achievement ID.", example = "1") @RequestParam Long id)
            throws InvalidJsonTokenException ;
}
