<template>
  <div class="flex flex-col items-start justify-start w-full pt-10 mb-64">
    <div class="bg-orange-500 text-white text-center py-2 px-4 mb-4 rounded font-bold self-center">
      Early Access Version
    </div>
    <h1 class="text-2xl font-bold mb-4 self-center">Welcome to your NutriGuideBuddy app!</h1>
    <p class="mb-4 self-center" v-if="!user">
      To get the most out of our app, please
      <router-link :to="{name: 'SignUpOrLogin'}" class="text-blue-500 hover:underline">register or login</router-link>
      .
      This will give you access to personalized nutrition tracking and insights.
    </p>
    <p class="mb-4 self-center" v-else-if="!userDetailsComplete">
      Please
      <router-link :to="{name: 'Edit'}" class="text-blue-500 hover:underline">fill your details</router-link>
      to get the most out of our app.
    </p>
    <p class="mb-4 self-center">
      Even without authentication, you can still learn about nutrition.
      Check out our Nutrition Info page for more information.
    </p>
  </div>
</template>

<script setup>
defineProps(['user', 'userDetailsComplete'])
</script>