package org.example.utils;

import org.example.AchievementException;
import org.example.domain.dto.AchievementTrackerEditDto;
import org.example.domain.entity.Achievement;
import org.example.domain.entity.AchievementProgress;
import org.example.domain.entity.AchievementTracker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.SortedMap;

public class AchievementUtils {
    public static void addProgress(AchievementTracker achievementTracker,
                                   Achievement achievementToAdd,
                                   Boolean replaceDailyProgress) {

        if (achievementToAdd.getDate() == null) {
            throw new AchievementException("Date must be set");
        }

        if (achievementToAdd.getProgress() == null || achievementToAdd.getProgress().compareTo(BigDecimal.ZERO) < 0) {
            throw new AchievementException("Progress must be positive");
        }

        addDaily(achievementToAdd, achievementTracker, replaceDailyProgress);
        addMonthlyProgress(achievementToAdd, achievementTracker);
        addYearlyProgress(achievementToAdd, achievementTracker);


    }

    public static void changeAchievementTracker(AchievementTrackerEditDto dto, AchievementTracker achievementTracker) {
        if (dto.getName() != null && !dto.getName().isEmpty() && !dto.getName().isBlank()) {
            achievementTracker.setName(dto.getName());
        }
        if (dto.getMeasurement() != null && !dto.getMeasurement().isEmpty() && !dto.getMeasurement().isBlank()) {
            achievementTracker.setMeasurement(dto.getMeasurement());
        }
        if (dto.getDescription() != null && !dto.getDescription().isEmpty() && !dto.getDescription().isBlank()) {
            achievementTracker.setDescription(dto.getDescription());
        }
        if (dto.getGoal() != null && dto.getGoal().compareTo(BigDecimal.ZERO) > 0) {
            achievementTracker.setGoal(dto.getGoal());
        }
    }

    private static void addDaily(Achievement achievementToAdd, AchievementTracker entity, Boolean replaceDailyProgress) {

        SortedMap<LocalDate, Achievement> dailyProgress = entity.getDailyProgress();

        if (dailyProgress.isEmpty()) {
            dailyProgress.put(achievementToAdd.getDate(), achievementToAdd);
        } else {
            if (dailyProgress.containsKey(achievementToAdd.getDate())) {
                if (replaceDailyProgress) {
                    dailyProgress.put(achievementToAdd.getDate(), achievementToAdd);
                } else {
                    dailyProgress.get(achievementToAdd.getDate()).setProgress(
                            dailyProgress.get(achievementToAdd.getDate()).getProgress().add(achievementToAdd.getProgress())
                    );
                }
            } else {
                dailyProgress.put(achievementToAdd.getDate(), achievementToAdd);
            }
        }
    }

    private static void addMonthlyProgress(Achievement achievementToAdd, AchievementTracker entity) {

        SortedMap<YearMonth, AchievementProgress> monthlyProgress = entity.getMonthlyProgress();
        YearMonth keyToInsert = YearMonth.from(achievementToAdd.getDate());

        if (monthlyProgress.isEmpty()) {
            monthlyProgress.put(keyToInsert,
                    new AchievementProgress(
                            achievementToAdd.getDate(),
                            achievementToAdd.getProgress(),
                            achievementToAdd.getProgress(),
                            achievementToAdd.getProgress()
                    )
            );
        } else {

            AchievementProgress oldestProgress = getOldestProcess(monthlyProgress);
            AchievementProgress newestProgress = getNewestProcess(monthlyProgress);

            if (isBeforeOrEqualToTheNewestDate(newestProgress, achievementToAdd) && isAfterOrEqualToTheOldestDate(oldestProgress, achievementToAdd)) {

                AchievementProgress progress = monthlyProgress.get(keyToInsert);

                progress
                        .setBestProgress(progress.getBestProgress().max(achievementToAdd.getProgress()));
                progress
                        .setWorstProgress(progress.getWorstProgress().min(achievementToAdd.getProgress().compareTo(BigDecimal.ZERO) == 0 ? progress.getWorstProgress() : achievementToAdd.getProgress()));
                progress
                        .setTotalProgress(progress.getTotalProgress().add(achievementToAdd.getProgress()));

            } else if (achievementToAdd.getDate().isAfter(newestProgress.getDate())) {

                while (ChronoUnit.MONTHS.between(newestProgress.getDate(), achievementToAdd.getDate()) != 0) {

                    LocalDate newDate = newestProgress.getDate().plusMonths(1);

                    AchievementProgress newAchievement = new AchievementProgress(newDate,
                            BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

                    monthlyProgress.put(YearMonth.from(newAchievement.getDate()), newAchievement);

                    newestProgress = newAchievement;
                }

                newestProgress.setBestProgress(achievementToAdd.getProgress().max(newestProgress.getBestProgress()));
                newestProgress.setWorstProgress(achievementToAdd.getProgress()
                        .min(newestProgress.getWorstProgress().compareTo(BigDecimal.ZERO) == 0 ? achievementToAdd.getProgress() : newestProgress.getWorstProgress())
                );
                newestProgress.setTotalProgress(newestProgress.getTotalProgress().add(achievementToAdd.getProgress()));

            } else {

                while (ChronoUnit.MONTHS.between(achievementToAdd.getDate(), oldestProgress.getDate()) != 0) {

                    LocalDate newDate = oldestProgress.getDate().minusMonths(1);

                    AchievementProgress newAchievement = new AchievementProgress(newDate,
                            BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

                    monthlyProgress.put(YearMonth.from(newAchievement.getDate()), newAchievement);
                    oldestProgress = newAchievement;
                }

                oldestProgress.setBestProgress(achievementToAdd.getProgress().max(oldestProgress.getBestProgress()));
                oldestProgress.setWorstProgress(achievementToAdd.getProgress()
                        .min(oldestProgress.getWorstProgress().compareTo(BigDecimal.ZERO) == 0 ? achievementToAdd.getProgress() : oldestProgress.getWorstProgress())
                );
                oldestProgress.setTotalProgress(oldestProgress.getTotalProgress().add(achievementToAdd.getProgress()));
            }
        }
    }

    private static void addYearlyProgress(Achievement achievementToAdd, AchievementTracker entity) {

        SortedMap<Integer, AchievementProgress> yearlyProgresses = entity.getYearlyProgresses();

        if (yearlyProgresses.isEmpty()) {
            yearlyProgresses.put(achievementToAdd.getDate().getYear(),
                    new AchievementProgress(
                            achievementToAdd.getDate(),
                            achievementToAdd.getProgress(),
                            achievementToAdd.getProgress(),
                            achievementToAdd.getProgress()
                    )
            );
        } else {

            AchievementProgress oldestProgress = getOldestProcess(yearlyProgresses);
            AchievementProgress newestProgress = getNewestProcess(yearlyProgresses);

            if (isBeforeOrEqualToTheNewestDate(newestProgress, achievementToAdd) && isAfterOrEqualToTheOldestDate(oldestProgress, achievementToAdd)) {

                int yearToInsert = achievementToAdd.getDate().getYear();
                yearlyProgresses.get(yearToInsert)
                        .setBestProgress(yearlyProgresses.get(yearToInsert).getBestProgress().max(achievementToAdd.getProgress()));
                yearlyProgresses.get(yearToInsert)
                        .setWorstProgress(yearlyProgresses.get(yearToInsert).getWorstProgress().min(achievementToAdd.getProgress().compareTo(BigDecimal.ZERO) == 0 ? yearlyProgresses.get(yearToInsert).getWorstProgress() : achievementToAdd.getProgress()));
                yearlyProgresses.get(yearToInsert)
                        .setTotalProgress(yearlyProgresses.get(yearToInsert).getTotalProgress().add(achievementToAdd.getProgress()));
            } else if (achievementToAdd.getDate().isAfter(newestProgress.getDate())) {

                while (newestProgress.getDate().getYear() != achievementToAdd.getDate().getYear()) {

                    LocalDate newDate = newestProgress.getDate().plusYears(1);

                    AchievementProgress newAchievement = new AchievementProgress(newDate,
                            BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

                    yearlyProgresses.put(newAchievement.getDate().getYear(), newAchievement);

                    newestProgress = newAchievement;
                }

                newestProgress.setBestProgress(achievementToAdd.getProgress().max(newestProgress.getBestProgress()));
                newestProgress.setWorstProgress(achievementToAdd.getProgress()
                        .min(newestProgress.getWorstProgress().compareTo(BigDecimal.ZERO) == 0 ? achievementToAdd.getProgress() : newestProgress.getWorstProgress())
                );
                newestProgress.setTotalProgress(newestProgress.getTotalProgress().add(achievementToAdd.getProgress()));
            } else {

                while (achievementToAdd.getDate().getYear() != oldestProgress.getDate().getYear()) {

                    LocalDate newDate = oldestProgress.getDate().minusYears(1);

                    AchievementProgress newAchievement = new AchievementProgress(newDate,
                            BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

                    yearlyProgresses.put(newAchievement.getDate().getYear(), newAchievement);
                    oldestProgress = newAchievement;
                }

                oldestProgress.setBestProgress(achievementToAdd.getProgress().max(oldestProgress.getBestProgress()));
                oldestProgress.setWorstProgress(achievementToAdd.getProgress()
                        .min(oldestProgress.getWorstProgress().compareTo(BigDecimal.ZERO) == 0 ? achievementToAdd.getProgress() : oldestProgress.getWorstProgress())
                );
                oldestProgress.setTotalProgress(oldestProgress.getTotalProgress().add(achievementToAdd.getProgress()));
            }
        }
    }
    private static AchievementProgress getOldestProcess(SortedMap<?, AchievementProgress> dailyProgress) {
        return dailyProgress.get(dailyProgress.firstKey());
    }

    private static AchievementProgress getNewestProcess(SortedMap<?, AchievementProgress> dailyProgress) {
        return dailyProgress.get(dailyProgress.lastKey());
    }

    private static boolean isBeforeOrEqualToTheNewestDate (AchievementProgress newestProgress, Achievement achievementToAdd){
        return achievementToAdd.getDate().isBefore(newestProgress.getDate()) || achievementToAdd.getDate().isEqual(newestProgress.getDate());
    }

    private static boolean isAfterOrEqualToTheOldestDate (AchievementProgress oldestProgress, Achievement achievementToAdd){
        return achievementToAdd.getDate().isAfter(oldestProgress.getDate()) || achievementToAdd.getDate().isEqual(oldestProgress.getDate());
    }
}
