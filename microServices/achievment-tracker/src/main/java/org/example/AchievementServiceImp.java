package org.example;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.AchievementHolderCreateDto;
import org.example.domain.dto.AchievementTrackerEditDto;
import org.example.domain.dto.AchievementTrackerView;
import org.example.domain.dto.User;
import org.example.domain.entity.Achievement;
import org.example.domain.entity.AchievementProgress;
import org.example.domain.entity.AchievementTracker;
import org.example.utils.AchievementUtils;
import org.example.utils.GsonWrapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementServiceImp {

    private final AchievementRepository achievementRepository;
    private final GsonWrapper gsonWrapper;

    public List<AchievementTrackerView> getAllAchievementViewsWitherUserId(String userToken) {
        Long userId = getUserId(userToken);
        List<AchievementTracker> allAchievementTrackers = achievementRepository.findAllByUserId(userId);

        return allAchievementTrackers.stream().map(AchievementTrackerView::new).toList();
    }

    public AchievementTrackerView getAchievementViewById(String userToken, Long id) {
        return achievementRepository.findById(id).map(AchievementTrackerView::new)
                .orElseThrow(() -> new AchievementException("Achievement not found"));
    }

    private Long getUserId(String userToken) {
        return gsonWrapper.fromJson(userToken, User.class).getId();
    }

    public AchievementTrackerView createAchievement(String userToken, AchievementHolderCreateDto dto) {

        if (achievementRepository.findByName(dto.getName()).isPresent()) {
            throw new AchievementException("Achievement with name " + dto.getName() + " already exists");
        }

        AchievementTracker entity = dto.toEntity();
        entity.setStartDate(LocalDate.now());
        entity.setUserId(getUserId(userToken));
        AchievementTracker saved = achievementRepository.save(entity);
        return new AchievementTrackerView(saved);
    }

    public AchievementTrackerView updateAchievement(String userToken, Achievement progress, String achievementName,
            Boolean replaceDailyProgress) {

        AchievementTracker achievementTracker = achievementRepository
                .findByNameAndUserId(achievementName, getUserId(userToken))
                .orElseThrow(() -> new AchievementException("Achievement not found"));

        AchievementUtils.addProgress(achievementTracker, progress, replaceDailyProgress);

        AchievementTracker saved = achievementRepository.save(achievementTracker);
        return new AchievementTrackerView(saved);
    }

    public void deleteAchievement(String userToken, Long id) {
        AchievementTracker achievementTracker = achievementRepository
                .findByIdAndUserId(id, getUserId(userToken))
                .orElseThrow(() -> new AchievementException("Achievement not found"));

        achievementRepository.delete(achievementTracker);
    }

    public AchievementTrackerView editTracker(String userToken, AchievementTrackerEditDto dto, String achievementName) {
        AchievementTracker achievementTracker = achievementRepository
                .findByNameAndUserId(achievementName, getUserId(userToken))
                .orElseThrow(() -> new AchievementException("Achievement not found"));

        if (dto.getName() != null) {
            achievementRepository
                    .findByNameAndUserId(dto.getName(), getUserId(userToken))
                    .ifPresent(achievementTracker1 -> {
                        throw new AchievementException("Achievement with name " + dto.getName() + " already exists");
                    });
        }
        AchievementUtils.changeAchievementTracker(dto, achievementTracker);
        return new AchievementTrackerView(achievementRepository.save(achievementTracker));
    }
}
