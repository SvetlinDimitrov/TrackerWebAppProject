<template>
  <div class="flex flex-col gap-2" style="min-height: 16rem; max-width: 20rem">
    <div class="text-center mt-3 mb-3 text-xl font-semibold">More Details</div>

    <!-- Kilograms field -->
    <div class="mb-4">
      <InputGroup>
        <InputGroupAddon>
          kg
        </InputGroupAddon>
        <InputNumber placeholder="kilograms" v-model="kilograms" :min="20" :max="650"/>
      </InputGroup>
    </div>

    <!-- Height field -->
    <div class="mb-4">
      <InputGroup>
        <InputGroupAddon>
          cm
        </InputGroupAddon>
        <InputNumber placeholder="height" v-model="height" :min="20" :max="251"/>
      </InputGroup>
    </div>

    <!-- Age field -->
    <div class="mb-4">
      <InputGroup>
        <InputGroupAddon>
          <i class="pi pi-calendar"/>
        </InputGroupAddon>
        <InputNumber placeholder="age" v-model="age" :min="3" :max="122"/>
      </InputGroup>
    </div>

    <!-- WorkoutState field -->
    <div class="mb-4">
      <InputGroup>
        <InputGroupAddon>
          <i class="pi pi-crown"/>
        </InputGroupAddon>
        <Dropdown id="workoutState" v-model="workoutState" :options="workoutStateOptions" placeholder="workout status"
                  class="w-full"/>
      </InputGroup>
    </div>

    <!-- Gender field -->
    <div class="mb-4">
      <InputGroup>
        <InputGroupAddon>
          <i class="pi pi-user"/>
        </InputGroupAddon>
        <Dropdown id="gender" v-model="gender" :options="genderOptions" placeholder="gender" class="w-full"/>
      </InputGroup>
    </div>

    <div class="flex pt-2 justify-center w-full gap-10">
      <Button label="Submit" severity="success" @click="editHandler"/>
      <Button v-if="showSecondButton" label="Skip" severity="secondary" icon="pi pi-fast-forward" iconPos="right"
              @click="skipHandler"/>
    </div>
  </div>
</template>

<script setup>
import {ref} from "vue";

const props = defineProps({
  kilograms: Number,
  age: Number,
  height: Number,
  gender: String,
  workoutState: String,
  showSecondButton: Boolean
})

const emit = defineEmits(['submit' , 'skip'])
const gender = ref(props.gender);
const kilograms = ref(props.kilograms);
const workoutState = ref(props.workoutState);
const age = ref(props.age);
const height = ref(props.height);
const showSecondButton = ref(props.showSecondButton ? props.showSecondButton : false);
const genderOptions = ['MALE', 'FEMALE'];
const workoutStateOptions = ['SEDENTARY', 'LIGHTLY_ACTIVE', 'MODERATELY_ACTIVE', 'VERY_ACTIVE', 'SUPER_ACTIVE'];

const editHandler = async () => {
  const data = {
    gender: gender.value,
    kilograms: kilograms.value,
    workoutState: workoutState.value,
    age: age.value,
    height: height.value
  };
  emit('submit' , data);
}

const skipHandler = () => {
  emit('skip');
}
</script>

<style scoped>

</style>