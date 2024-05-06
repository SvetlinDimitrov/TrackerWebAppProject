<template>
  <div class="flex flex-col justify-center items-center w-1/2 mx-auto">
    <router-link to="/settings/edit" class="w-full p-4 bg-gray-100 text-black rounded shadow-lg text-center cursor-pointer mb-4 transition duration-500 ease-in-out transform hover:-translate-y-1 hover:scale-110">
      Edit Details
    </router-link>
    <router-link :to="{ name: 'DeleteAccount' }" class="w-full p-4 bg-gray-100 text-black rounded shadow-lg text-center cursor-pointer mb-4 transition duration-500 ease-in-out transform hover:-translate-y-1 hover:scale-110">
      Delete Account
    </router-link>
    <router-link :to="{ name: 'Logout' }" class="w-full p-4 bg-gray-100 text-black rounded shadow-lg text-center cursor-pointer transition duration-500 ease-in-out transform hover:-translate-y-1 hover:scale-110">
      Logout
    </router-link>
  </div>
</template>