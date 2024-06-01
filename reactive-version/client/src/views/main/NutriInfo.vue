<template>
  <div v-if="nutritionInfo">
    <h1 class="text-center mx-auto py-4 px-4 text-2xl font-bold">{{ name }}</h1>
    <div class="w-1/2 mx-auto">
      <Accordion :activeIndex="index">
        <AccordionTab header="Description">
          <p class="m-0">{{ nutritionInfo.description }}</p>
        </AccordionTab>
        <AccordionTab header="Functions">
          <ul class="list-disc pl-5 overflow-auto max-h-80">
            <li v-for="(value, key) in nutritionInfo.functions" :key="key"
                class="mb-2">
              <strong>{{ key }}:</strong> {{ value }}
            </li>
          </ul>
        </AccordionTab>
        <AccordionTab header="Sources">
          <ul class="list-disc pl-5 overflow-auto max-h-80">
            <li v-for="(value, key) in nutritionInfo.sources" :key="key"
                class="mb-2">
              <strong>{{ key }}:</strong> {{ value }}
            </li>
          </ul>
        </AccordionTab>
        <AccordionTab header="Over consuming">
          <p class="m-0">{{ nutritionInfo.overdose }}</p>
        </AccordionTab>
        <AccordionTab header="Deficiency">
          <p class="m-0">{{ nutritionInfo.deficiency }}</p>
        </AccordionTab>
        <AccordionTab header="Daily Intake">
          <p class="m-0">{{ nutritionInfo.dailyIntake }}</p>
        </AccordionTab>
        <AccordionTab header="Resources">
          <ul class="list-disc pl-5">
            <li v-for="(link, index) in nutritionInfo.res" :key="index">
              <a :href="link" target="_blank" rel="noopener noreferrer"
                 class="text-blue-500 hover:text-blue-800 transition-colors duration-200">{{ link }}</a>
            </li>
          </ul>
        </AccordionTab>
      </Accordion>
    </div>
  </div>
</template>

<script setup>
import {useRoute} from 'vue-router';
import {ref, watchEffect} from "vue";
import {nutritionUnits} from "../../utils/nutrition/nutritionInfo.js";

const route = useRoute();
const name = ref(route.params.name);
const nutritionInfo = ref(null);
const index = route.query.activeIndex ? Number(route.query.activeIndex) : 0;

watchEffect(() => {
  name.value = route.params.name;
  nutritionInfo.value = nutritionUnits[name.value];
});

</script>
