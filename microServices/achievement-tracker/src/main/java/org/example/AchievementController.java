package org.example;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.domain.dto.AchievementHolderCreateDto;
import org.example.domain.dto.AchievementTrackerEditDto;
import org.example.domain.dto.AchievementTrackerView;
import org.example.domain.dto.SingleErrorResponse;
import org.example.domain.entity.Achievement;
import org.example.exceptions.AchievementException;
import org.example.exceptions.InvalidJsonTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/achievement", headers = "X-ViewUser")
@Tag(name = "Achievement", description = "The Achievement API calculates and provides all the interval dates between the added dates. It provides the weeks, months, and years.")
@RequiredArgsConstructor
public class AchievementController {
    
    private final AchievementServiceImp achievementServiceImp;
    
    @GetMapping(path = "/all")
    @Operation(summary = "Get all achievement holders by ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "List of all achievement holders", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AchievementTrackerView.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json header", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class)))
            
        }
    )
    public ResponseEntity<List<AchievementTrackerView>> getAllAchievementHoldersById(
        @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken) throws InvalidJsonTokenException {
        List<AchievementTrackerView> achievementTrackerViews = achievementServiceImp.getAllAchievementViewsWitherUserId(userToken);
        return new ResponseEntity<>(achievementTrackerViews, HttpStatus.OK);
    }
    
    @GetMapping
    @Operation(summary = "Get achievement holder by ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Achievement holder details", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AchievementTrackerView.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json header", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class)))
            
        }
    )
    public ResponseEntity<AchievementTrackerView> getAchievementHolderById(
        @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken,
        @Parameter(description = "Achievement holder ID.", example = "1") @RequestParam(name = "id") Long id) throws InvalidJsonTokenException {
        AchievementTrackerView foods = achievementServiceImp.getAchievementViewById(userToken, id);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }
    
    @PostMapping
    @Operation(summary = "Create achievement holder",
        responses = {
            @ApiResponse(responseCode = "201", description = "Achievement holder created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json header", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class)))
            
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AchievementHolderCreateDto.class),
                examples = @ExampleObject(name = "achievementHolderCreateDto", value = "{\"name\":\"Achievement1\",\"description\":\"This is a sample achievement\",\"goal\":50,\"measurement\":\"kilograms\"}")
            )
        )
    )
    public ResponseEntity<HttpStatus> createAchievementHolder(
        @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken,
        @Valid @RequestBody AchievementHolderCreateDto dto,
        BindingResult result) throws InvalidJsonTokenException {
        
        if (result.hasErrors()) {
            throw new AchievementException("Invalid data :" + result.getFieldErrors().get(0).getDefaultMessage());
        }
        
        achievementServiceImp.createAchievement(userToken, dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    @PatchMapping("/{achId}/addProgress")
    @Operation(summary = "Update achievement",
        responses = {
            @ApiResponse(responseCode = "200", description = "Achievement updated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json header", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class)))
            
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Achievement.class),
                examples = @ExampleObject(name = "achievement", value = "{\"date\":\"2022-12-31\",\"progress\":50}")
            )
        )
    )
    public ResponseEntity<HttpStatus> updateAchievement(
        @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken,
        @Parameter(description = "Achievement ID.", example = "1") @PathVariable(name = "achId") Long achId,
        @Parameter(description = "Replace daily progress.", example = "false") @RequestParam(name = "replaceDailyProgress", defaultValue = "false") Boolean replaceDailyProgress,
        @RequestBody Achievement achievement) throws InvalidJsonTokenException {
        
        achievementServiceImp.updateAchievement(userToken, achievement, achId, replaceDailyProgress);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    
    @PatchMapping("/{achId}")
    @Operation(summary = "Change achievement tracker",
        responses = {
            @ApiResponse(responseCode = "204", description = "Achievement tracker changed", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json header", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class)))
            
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AchievementTrackerEditDto.class),
                examples = @ExampleObject(name = "achievementHolderCreateDto", value = "{\"name\":\"Achievement1\",\"description\":\"This is a sample achievement\",\"goal\":100,\"measurement\":\"kilograms\"}")
            )
        )
    )
    public ResponseEntity<HttpStatus> changeAchievementTracker(
        @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken,
        @Parameter(description = "Achievement ID.", example = "1") @PathVariable(name = "achId") Long achId,
        @RequestBody AchievementTrackerEditDto dto) throws InvalidJsonTokenException {
        
        achievementServiceImp.editTracker(userToken, dto, achId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @DeleteMapping
    @Operation(summary = "Delete achievement",
        responses = {
            @ApiResponse(responseCode = "204", description = "Achievement deleted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid json header", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SingleErrorResponse.class)))
            
        }
    )
    public ResponseEntity<Void> deleteAchievement(
        @Parameter(description = "User token.", example = "{\"id\":1,\"username\":\"user123\",\"email\":\"user123@example.com\",\"kilograms\":70,\"height\":170,\"age\":30,\"workoutState\":\"LIGHTLY_ACTIVE\",\"gender\":\"MALE\"}") @RequestHeader(name = "X-ViewUser") String userToken,
        @Parameter(description = "Achievement ID.", example = "1") @RequestParam(name = "id") Long id) throws InvalidJsonTokenException {
        
        achievementServiceImp.deleteAchievement(userToken, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @ExceptionHandler(AchievementException.class)
    public ResponseEntity<SingleErrorResponse> handleException(AchievementException e) {
        return new ResponseEntity<>(new SingleErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(InvalidJsonTokenException.class)
    public ResponseEntity<SingleErrorResponse> handleException(InvalidJsonTokenException e) {
        return new ResponseEntity<>(new SingleErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
