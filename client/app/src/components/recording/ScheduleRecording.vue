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
            <select v-model='selectedRoom' id="room" v-on:change="getTimetable">
              <option v-for="(room, index) in rooms" :key="room.id" :value="index">{{ room.name }}</option>
            </select>
          </div>
        </div>

        <!-- available timetables -->
        <div class="row justify-content-center">
          <div class="col-2">
            <label for="timetable">Available Lessons</label>
          </div>
          <div class="col-2">
            <select v-model='selectedLesson' id="timetable" v-on:change="calculateRecordingTimesFromLesson">
              <option v-for="(lesson, index) in timetable.lessonsList" :key="lesson.id" :value="index">{{ lesson.title }}</option>
            </select>
          </div>
        </div>
        
        <!-- start datetime of scheduled recording -->
        <div class="row justify-content-center">
          <div class="col-2">
            <label for="startDatetime">Scheduled start</label>
          </div>
          <div class="col-2">
            <datetime id="startDatetime" type="datetime" v-on:input="approximateEndTime" value-zone="local" format="yyyy-MM-dd HH:mm" v-model="startDatetime"></datetime>
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

const API_URL = process.env.VUE_APP_ROOT_API;
const DEFAULT_DURATION = process.env.VUE_APP_DEFAULT_RECORDING_DURATION_MINUTES;

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
       showErrorAlert: false,
       timetable: {
         lessonsList: null
       },
       selectedLesson: null,
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
    approximateEndTime() {
      // Add default length of the recording to approximate end time
      if (this.startDatetime != '') {
        this.endDatetime = moment(this.startDatetime).add(DEFAULT_DURATION, 'minutes').format('YYYY-MM-DDTHH:mm:ss');
      }

      // Also calculate duration
      this.calculateDuration();
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
        this.getTimetable();
      })
    },
    getTimetable() {
      axios.get(API_URL + 'room/' + this.rooms[this.selectedRoom].id + '/timetable')
      .then((response) => {
        this.timetable = response.data;
        this.timetable.lessonsList.unshift({id: null, title: "---"});
      })
    },
    calculateRecordingTimesFromLesson() {

        let lesson = this.timetable.lessonsList[this.selectedLesson]
        
        let startDatetime = moment().weekday(lesson.dayOfWeek + 1);
        let startTime = moment(lesson.startTime, 'HH:mm:ss');

        startDatetime.set({
            hour:   startTime.get('hour'),
            minute: startTime.get('minute'),
            second: startTime.get('second')
        });

        this.startDatetime = startDatetime.format('YYYY-MM-DDTHH:mm:ss');
        

        let endDatetime = moment().weekday(lesson.dayOfWeek + 1);
        let endTime = moment(lesson.endTime, 'HH:mm:ss')

        endDatetime.set({
            hour:   endTime.get('hour'),
            minute: endTime.get('minute'),
            second: endTime.get('second')
        });

        this.endDatetime = endDatetime.format('YYYY-MM-DDTHH:mm:ss');
        
        if (lesson.id == null) {
          this.approximateEndTime();
        }
        this.calculateDuration();
        
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
