import {toRaw} from "vue";

export function filterNutrients(food) {
    food.nutrients = food.nutrients.filter(nutrient => Number(nutrient.amount) > 0);
    return food;
}

export function generateGeneralFood(food) {
    return {
        name: food.name,
        calories: {
            amount: food.calories?.amount || food.calorie?.amount || undefined,
            unit: food.calories?.unit || food.calorie?.unit || undefined
        },
        mainServing: {
            amount: food.mainServing?.amount || undefined,
            metric: food.mainServing?.metric || undefined,
            servingWeight: food.mainServing?.servingWeight || undefined
        },
        otherServing: toRaw(food.otherServings || food.otherServing) || [],
        nutrients: toRaw(food.nutritionList || food.nutrients) || [],
        foodDetails: {
            picture: food.foodDetails?.picture || food.additionalInfo?.picture || undefined,
            info: food.foodDetails?.info || food.additionalInfo?.info || undefined,
            largeInfo: food.foodDetails?.largeInfo || food.additionalInfo?.largeInfo || undefined,
        },
    }
}

export function createNewNutrientArray(food) {
    let newNutrients = [];

    for (let nutrient of food.nutrients) {
        let newNutrient = {
            "name": nutrient.name,
            "unit": nutrient.unit,
            "amount": nutrient.amount
        };

        newNutrients.push(newNutrient);
    }
    return newNutrients;
}