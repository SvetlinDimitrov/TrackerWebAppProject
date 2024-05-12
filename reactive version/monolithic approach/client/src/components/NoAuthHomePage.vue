<template>
  <div class="flex flex-col items-center justify-center p-8">
    <h1 class="text-2xl font-bold mb-4">Welcome to our Nutrition Tracker App!</h1>
    <p class="mb-4" v-if="!user">
      To get the most out of our app, please
      <router-link :to="{name: 'SignUpOrLogin'}" class="text-blue-500 hover:underline">register or login</router-link>.
      This will give you access to personalized nutrition tracking and insights.
    </p>
    <p class="mb-4" v-else-if="!userDetailsComplete">
      It seems like you haven't completed your user details yet. Please
      <router-link :to="{name: 'Edit'}" class="text-blue-500 hover:underline">complete your details</router-link>
      to get the most out of our app.
    </p>
    <p class="mb-4">
      Even without authentication, you can still learn about nutrition.
      Check out our Nutrition Info page for more information.
    </p>
  </div>
</template>

<script setup>

defineProps(['user', 'userDetailsComplete'])

</script>
