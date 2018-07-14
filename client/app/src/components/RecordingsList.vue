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
        <tr class="clickable-row" v-for="recording in recordings" :key="recording.id">
          <td>{{ recording.id }}</td>
          <td>{{ recording.title }}</td>
          <td>{{ recording.room.name }}</td>
          <td>{{ recording.startTime }}</td>
          <td>{{ recording.endTime }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'RecordingsList',
  data() {
    return {
      recordings: []
    }
  },
  created: function () {
    this.getRecordings();
  },
  methods: {
    getRecordings: function () {
      axios.get("http://localhost:8080/recordings")
      .then((response) => {
        this.recordings = response.data;
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
