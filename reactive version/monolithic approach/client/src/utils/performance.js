export function calculateAveragePercentageForArrayNutrients(array) {
    let totalPercentage = 0;

    for (let i = 0; i < array.length; i++) {
        let dailyConsumed = array[i].dailyConsumed;
        let recommendedIntake = array[i].recommendedIntake;

        let percentage;
        if (dailyConsumed === 0 && recommendedIntake === 0) {
            percentage = 0;
        } else {
            percentage = (dailyConsumed / recommendedIntake) * 100;
        }

        totalPercentage += percentage;
    }

    let averagePercentage = totalPercentage / array.length;

    if (averagePercentage > 100) {
        averagePercentage = 100;
    }

    return averagePercentage.toFixed(0);
}

export function calculateAveragePercentage(num1, num2) {

    let percentage = (num1 / num2) * 100;
    if (percentage > 100) {
        percentage = 100;
    }
    return percentage.toFixed(0) ? percentage.toFixed(0) : 0;
}

export function getNutrientAmountFromFood(foods, nutrientName) {
    let result = [];
    for (let food of foods) {
        for (let nutrient of food.nutritionList) {
            if (nutrient.name === nutrientName) {
                result.push({foodName: food.name, nutrientAmount: nutrient.amount});
            }
        }
    }
    return mergeFoods(result);
}

export function getAllFoods(meals) {

    let allFoods = [];
    for (let mealId in meals) {
        let meal = meals[mealId];
        allFoods.push(...meal.foods);
    }
    return allFoods;
}

export function mergeFoods(foods) {
    let foodMap = new Map();

    for (let food of foods) {
        const newFood = {
            foodName: food.foodName,
            nutrientAmount: food.nutrientAmount
        };
        if (foodMap.has(newFood.foodName)) {
            foodMap.get(newFood.foodName).nutrientAmount += newFood.nutrientAmount;
        } else {
            foodMap.set(newFood.foodName, newFood);
        }
    }
    return Array.from(foodMap.values());
}
