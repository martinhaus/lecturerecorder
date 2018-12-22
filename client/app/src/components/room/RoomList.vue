<template>
  <div class="container">
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col">Name</th>
          <th scope="col">Camera IP Address</th>
          <th scope="col">Audio Source</th>
        </tr>
      </thead>
      <tbody>
        <router-link  tag="tr" :to="{path: '/room/' + room.id}" class="clickable-row" v-for="room in rooms" :key="room.id">
          <td>{{ room.id }}</td>
          <td>{{ room.name }}</td>
          <td>{{ room.ipAddress }}</td>
          <td>{{ room.audioSource }}</td>
        </router-link>
      </tbody>
    </table>
    <router-link class="btn btn-outline-primary my-2 my-sm-0" to="/rooms/add" tag="button">Add New Room</router-link>
  </div>
</template>

<script>
import axios from 'axios';

const API_URL = process.env.VUE_APP_ROOT_API

export default {
  name: 'RoomList',
  data() {
    return {
      rooms: []
    }
  },
  created: function () {
    this.getRooms();
  },
  methods: {
    getRooms: function () {
      axios.get(API_URL + "rooms")
      .then((response) => {
        this.rooms = response.data;
      })
    },
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
