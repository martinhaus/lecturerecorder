<template>
  <div class="container">
    <div class="search-wrapper float-right row mb-2">
      <div class="col  my-auto">
        <p class="my-auto">Search recordings: </p>
      </div>
      <input class="form-control col" type="text" v-model="search" placeholder="Search title.."/>
          
    </div>
    <table class="table table-hover">
      <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col">Name</th>
          <th scope="col">Room</th>
          <th scope="col">Start</th>
          <th scope="col">End</th>
          <th scope="col">Duration</th>
        </tr>
      </thead>
      <tbody>
        <router-link  tag="tr" :to="{path: '/recording/' + recording.id}" class="clickable-row" v-for="recording in filteredList" :key="recording.id">
          <td>{{ recording.id }}</td>
          <td>{{ recording.title }}
            <div v-if="recording.active" class="progress">
              <div class="progress-bar progress-bar-striped progress-bar-animated bg-info" 
              role="progressbar" aria-valuenow="10" aria-valuemin="0" 
              aria-valuemax="100"
               :style="calculateProgress(recording.startTime, recording.endTime)"></div>
            </div>
            <font-awesome-icon v-if="recording.finished" icon="check-circle" />
          </td>
          <td>{{ recording.room.name }}</td>
          <td>{{ recording.startTime }}</td>
          <td>{{ recording.endTime }}</td>
          <td>{{ calculateDurationMinutes(recording.startTime, recording.endTime) }} minutes</td>
        </router-link>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios';
import moment from 'moment'

const API_URL = process.env.VUE_APP_ROOT_API

export default {
  name: 'RecordingsList',
  data() {
    return {
      recordings: [],
      search: ""
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
    },
    calculateDurationMinutes: function (start, end) {
      let duration =  moment(end).diff(moment(start));
      return moment.duration(duration).asMinutes();
    },
    calculateProgress: function (start, end) {
      let all = moment(end).diff(moment(start));
      let current = moment().diff(moment(start))
      return "width: " + current / all * 100 + "%;"
    }
  },
  computed: {
    filteredList() {
      return this.recordings.filter(recording => {
        return (recording.id.toString().toLowerCase().includes(this.search.toLowerCase()) || recording.title.toLowerCase().includes(this.search.toLowerCase()) || recording.startTime.toLowerCase().includes(this.search.toLowerCase()) || recording.endTime.toLowerCase().includes(this.search.toLowerCase()))
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
