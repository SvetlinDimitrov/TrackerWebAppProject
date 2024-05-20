<template>
  <div class="flex flex-col justify-center items-center w-full p-1 border-2">
    <div class="flex justify-between items-center w-full gap-5">
      <InputSwitch v-model="show"/>
      <h2>
        <slot></slot>
      </h2>
      <div class="flex gap-2">
        <Tag severity="info"
             value="Info"
             rounded @click.stop="$emit('handle-click-info')"
             class="cursor-pointer"></Tag>
        <Tag v-if="showDanger"
             severity="danger"
             value="Danger"
             rounded @click.stop="$emit('handle-click-danger')"
             class="cursor-pointer"></Tag>
      </div>
    </div>
    <CustomProgressBar :progress="Number(progress)"/>
    <div v-if="show">
      <span class="mr-2">consumed: {{ consumed }} / total: {{ total }} {{ unit }}</span>
      <Tag severity="warning"
           value="Edit"
           rounded @click.stop="$emit('handle-edit-click')"
           class="cursor-pointer"></Tag>
    </div>
    <ul v-if="show && foodList.length !== 0" class="p-1"> Consumed foods:
      <li v-for="food in foodList">{{ food.foodName }}: {{ calculateAveragePercentage(food.nutrientAmount, total) }}%
        ({{ food.nutrientAmount }})
      </li>
    </ul>
  </div>
</template>

<script setup>
import {computed, ref} from 'vue';
import CustomProgressBar from "./CustomProgressBar.vue";
import {calculateAveragePercentage} from "../../utils/performance.js";

const props = defineProps({
  consumed: Number,
  total: Number,
  unit: String,
  foodList: Array,
  showDanger: Boolean,
  hideTag: Boolean
})

defineEmits(['handle-click-info', 'handle-click-danger' ,'handle-edit-click']);

const show = ref(false);
const progress = computed(() => calculateAveragePercentage(props.consumed, props.total) ? calculateAveragePercentage(props.consumed, props.total) : 0);

</script>
