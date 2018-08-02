<template>
  <div>
    <div class="container">
     <b-alert variant="success"
             dismissible
             fade
             :show="showSuccessAlert"
             @dismissed="showSuccessAlert=false">
      Recording scheduled
      </b-alert>
      <b-alert variant="danger"
             dismissible
             fade
             :show="showErrorAlert"
             @dismissed="showErrorAlert=false">
      Failed to schedule
      </b-alert>
    </div>
    
    <h1>Schedule recording</h1>
    <h5>Fill the form below to schedule new recording</h5>
    
    <div class="container">
        <form @submit.prevent="scheduleRecording">

        <div class="row justify-content-center">
          <div class="col-2">
            <label for="title">Title</label>
          </div>
          <div class="col-2">
            <input type="text" id="title" v-model="title" />
          </div>
        </div>

        <!-- available rooms for recordings -->
        <div class="row justify-content-center">
          <div class="col-2">
            <label for="room">Room</label>
          </div>
          <div class="col-2">
            <select v-model='selectedRoom' id="room">
              <option v-for="(room, index) in rooms" :key="room.id" :value="index">{{ room.name }}</option>
            </select>
          </div>
        </div>
        
        <!-- start datetime of scheduled recording -->
        <div class="row justify-content-center">
          <div class="col-2">
            <label for="startDatetime">Scheduled start</label>
          </div>
          <div class="col-2">
            <datetime id="startDatetime" type="datetime" v-on:input="calculateDuration" value-zone="local" format="yyyy-MM-dd HH:mm" v-model="startDatetime"></datetime>
          </div>
        </div>

        <!-- end datetime of the recording -->
        <div class="row justify-content-center">
          <div class="col-2">
            <label for="endDatetime">Scheduled end</label>
          </div>
          <div class="col-2">
            <datetime id="endDatetime" type="datetime" v-on:input="calculateDuration" value-zone="local" format="yyyy-MM-dd HH:mm" v-model="endDatetime"></datetime>
          </div>
        </div>

        <!-- duration based on selected times -->
        <div class="row justify-content-center"> 
          <div class="col-2">
            <label for="duration">Duration</label>
          </div>
          <div class="col-2">
            <span id="duration">{{ duration }} minutes</span>
          </div>
        </div>

        <button class="btn btn-primary">Schedule</button>
       </form>
    </div>
   

  </div>
</template>

<script>

import { Datetime } from 'vue-datetime';
import 'vue-datetime/dist/vue-datetime.css';
import moment from 'moment';
import axios from 'axios';

const API_URL = process.env.VUE_APP_ROOT_API

export default {    
  data () {
    return {
      title: '',
       startDatetime: null,
       endDatetime: null,
       duration: 0,
       rooms: null,
       selectedRoom: 0,  
       showSuccessAlert: false,
       showErrorAlert: false
    }
  },
  components: {
    datetime: Datetime,
  },
  created: function () {
    this.getRooms();
  },
  methods: {
    scheduleRecording() {
      this.startDatetime = moment(this.startDatetime).format('YYYY-MM-DDTHH:mm:ss')
      this.endDatetime = moment(this.endDatetime).format('YYYY-MM-DDTHH:mm:ss')
      axios.post(API_URL + 'recording/schedule', {
        title: this.title,
        startTime: this.startDatetime,
        endTime: this.endDatetime,
        room: this.rooms[this.selectedRoom]
      }).then(() => {
        this.showSuccessAlert = true;
      }).catch(function () {
        this.showErrorAlert = true;
      })
    },
    calculateDuration: function() {
      if (this.endDatetime != '' && this.startDatetime != '') {
        this.duration = moment(this.endDatetime).diff(this.startDatetime);
        this.duration = moment.duration(this.duration).asMinutes();
      }      
    },
    getRooms: function () {
      axios.get(API_URL + 'rooms')
      .then((response) => {
        this.rooms = response.data;
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
