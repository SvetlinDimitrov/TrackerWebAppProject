<template>
  <div class="flex flex-col items-center justify-center">
    <h2><slot></slot></h2>
    <Knob v-model="value" valueTemplate="{value}%" readonly />
    <div class="flex items-center gap-2">
      <p v-if="!show">show</p>
      <p v-else>hide</p>
      <InputSwitch v-model="show" @change="$emit('toggle' , show)"/>
    </div>
  </div>
</template>

<script setup>
import {ref, watchEffect} from 'vue';

const props = defineProps({
  show: Boolean,
  knobValue: Number,
})

const value = ref(props.knobValue);
const show = ref(props.show);

watchEffect(() => {
  show.value = props.show;
});
</script>
