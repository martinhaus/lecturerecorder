<template>
  <div class="container">
    <b-alert variant="success"
             dismissible
             fade
             :show="showSuccessAlert"
             @dismissed="showSuccessAlert=false">
      All available timetables were updated
      </b-alert>
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col">Name</th>
          <th scope="col">Camera IP Address</th>
          <th scope="col">Audio Source</th>
          <th scope="col">Timetable Room Name</th>
        </tr>
      </thead>
      <tbody>
        <router-link  tag="tr" :to="{path: '/room/' + room.id}" class="clickable-row" v-for="room in rooms" :key="room.id">
          <td>{{ room.id }}</td>
          <td>{{ room.name }}</td>
          <td>{{ room.ipAddress }}</td>
          <td>{{ room.audioSource }}</td>
          <td>{{ room.timetableRoomName }}</td>
        </router-link>
      </tbody>
    </table>
    <router-link class="btn btn-outline-primary my-2 my-sm-0" to="/rooms/add" tag="button">Add New Room</router-link>
    <button class="btn btn-outline-primary my-2 my-sm-0" @click="updateAllTimetables">Update all timetables</button>
  </div>
</template>

<script>
import axios from 'axios';

const API_URL = process.env.VUE_APP_ROOT_API

export default {
  name: 'RoomList',
  data() {
    return {
      rooms: [],
      showSuccessAlert: false,
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
    updateAllTimetables: function () {
      axios.get(API_URL + "timetables/update")
      .then(() => {
        this.showSuccessAlert = true;
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
