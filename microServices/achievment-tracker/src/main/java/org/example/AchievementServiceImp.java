package org.example;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.AchievementCreateDto;
import org.example.domain.dto.AchievementEditDto;
import org.example.domain.dto.AchievementView;
import org.example.domain.dto.User;
import org.example.domain.entity.Achievement;
import org.example.utils.AchievementUtils;
import org.example.utils.GsonWrapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementServiceImp {

    private final AchievementRepository achievementRepository;
    private final GsonWrapper gsonWrapper;

    public List<AchievementView> getAllAchievementViewsWitherUserId(String userToken) {
        Long userId = getUserId(userToken);
        List<Achievement> allAchievements = achievementRepository.findAllByUserId(userId);

        return allAchievements.stream().map(AchievementView::new).toList();
    }

    public AchievementView getAchievementViewById(String userToken, Long id) {
        return achievementRepository.findById(id).map(AchievementView::new).orElseThrow(() -> new AchievementException("Achievement not found"));
    }

    private Long getUserId(String userToken) {
        return gsonWrapper.fromJson(userToken, User.class).getId();
    }

    public AchievementView createAchievement(String userToken, AchievementCreateDto dto) {

        if (achievementRepository.findByName(dto.getName()).isPresent()) {
            throw new AchievementException("Achievement with name " + dto.getName() + " already exists");
        }

        Achievement entity = dto.toEntity();
        entity.setStartDate(LocalDate.now());
        entity.setUserId(getUserId(userToken));
        Achievement saved = achievementRepository.save(entity);
        return new AchievementView(saved);
    }

    public AchievementView updateAchievement(String userToken, AchievementEditDto dto, Long id) {
        Achievement achievement = achievementRepository
                .findByIdAndUserId(id, getUserId(userToken))
                .orElseThrow(() -> new AchievementException("Achievement not found"));

        AchievementUtils.updateAchievement(achievement, dto);

        Achievement saved = achievementRepository.save(achievement);
        return new AchievementView(saved);
    }

    public void deleteAchievement(String userToken, Long id) {
        Achievement achievement = achievementRepository
                .findByIdAndUserId(id, getUserId(userToken))
                .orElseThrow(() -> new AchievementException("Achievement not found"));

        achievementRepository.delete(achievement);
    }
}
