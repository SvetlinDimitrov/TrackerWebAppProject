<template>
  <div class="flex flex-col items-center justify-center w-full gap-2">
    <div class="bg-orange-500 text-white text-center py-2 px-4 mb-4 rounded font-bold self-center">
      Early Access Version
    </div>
    <div v-if="record" class="w-1/2">
      <h2 class="text-2xl font-bold mb-6 text-center">Your Calorie Consumption Overview.</h2>
      <p class="text-xl mb-4 leading-relaxed">
        The daily calorie consumption is set at
        {{ record.dailyCaloriesToConsume }}
        <img src="../assets/calorie.png" class="h-8 w-8 inline-block" alt="calorie_image">.
        Currently, you have consumed {{ record.dailyCaloriesConsumed }}
        <img src="../assets/calorie.png" class="h-8 w-8 inline-block" alt="calorie_image">.
        To meet your daily target, consider crafting a meal featuring.
        <Button label="Create Meal" class="w-1/9" @click="$emit('handleMealCreate')"></Button>
      </p>
    </div>
    <Accordion v-if="meals" class="w-1/2 overflow-auto h-80">
      <AccordionTab class="w-full" v-for="(meal, id) in meals" :key="id">
        <template #header>
          <span class="flex items-center gap-2 w-full justify-between">
            <span class="font-bold white-space-nowrap">{{ meal.name }}</span>
            <span class="flex items-center gap-2">
              <span class="text-sm">Calories: {{ meal.consumedCalories }}</span>
              <Avatar icon="pi pi-apple" style="background-color: #FFA500; color: white"
                      @click.stop="$emit('handleCustomFood' , id)"
                      v-tooltip.top="'Insert custom food'"></Avatar>
              <Avatar icon="pi pi-apple" style="background-color: #60C921FF; color: white"
                      @click.stop="$emit('handleMealInsertFood' , id)"
                      v-tooltip.top="'Insert food'"></Avatar>
              <Avatar icon="pi pi-trash" style="background-color: #ff4d4d; color: white"
                      @click.stop="$emit('handleMealDeletion' , id)"
                      v-tooltip.top="'Delete meal'"></Avatar>
              <Avatar icon="pi pi-pencil" style="background-color: #4d94ff; color: white"
                      @click.stop="$emit('handleMealEdit' , id)"
                      v-tooltip.top="'Modify meal'"></Avatar>
            </span>
          </span>
        </template>
        <div class="overflow-auto max-h-40">
          <div class="food-item flex justify-between items-center p-2 border-b border-gray-300"
               v-for="(food, index) in meal.foods" :key="index">
            <span class="food-name">{{ food.name }}</span>
            <div class="flex gap-2 items-center">
              <span class="calorie-amount italic">{{ food.calorie.amount }} {{ food.calorie.unit }}</span>
              <Avatar icon="pi pi-pencil"
                      style="background-color: #4d94ff; color: white"
                      class="cursor-pointer"
                      @click.stop="$emit('handleChangeFoodById', meal.id , food.id)"
                      v-tooltip.top="'Modify food'"></Avatar>
              <Avatar icon="pi pi-trash"
                      style="background-color: #ff4d4d; color: white"
                      class="cursor-pointer"
                      @click.stop="$emit('handleRemoveFoodById',meal.id , food.id)"
                      v-tooltip.top="'Delete food'"></Avatar>
            </div>
          </div>
        </div>
      </AccordionTab>
    </Accordion>
  </div>
</template>

<script setup>
defineProps(['record', 'meals'])
defineEmits(['handleMealCreate', 'handleCustomFood', 'handleMealDeletion', 'handleMealEdit', 'handleMealInsertFood', 'handleRemoveFoodById', 'handleChangeFoodById'])

</script>
