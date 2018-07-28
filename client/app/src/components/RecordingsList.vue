<template>
  <div class="container">
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col">Name</th>
          <th scope="col">Room</th>
          <th scope="col">Start</th>
          <th scope="col">End</th>
        </tr>
      </thead>
      <tbody>
        <router-link tag="tr" :to="{path: '/recording/' + recording.id}" class="clickable-row" v-for="recording in recordings" :key="recording.id">
          <td>{{ recording.id }}</td>
          <td>{{ recording.title }}</td>
          <td>{{ recording.room.name }}</td>
          <td>{{ recording.startTime }}</td>
          <td>{{ recording.endTime }}</td>
        </router-link>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios';


const API_URL = process.env.VUE_APP_ROOT_API

export default {
  name: 'RecordingsList',
  data() {
    return {
      recordings: []
    }
  },
  created: function () {
    this.getRecordings();
    
    // setInterval(function () {
    //   this.getRecordings();
    // }.bind(this), 5000); 
  },
  methods: {
    getRecordings: function () {
      axios.get(API_URL + "recordings")
      .then((response) => {
        this.recordings = response.data;
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
