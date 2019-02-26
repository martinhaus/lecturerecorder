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
            <input class="w-100" type="text" id="title" v-model="title" v-on:keyup="repetitionPreview" />
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
            <select class="w-100" v-model='selectedLesson' id="timetable" v-on:change="calculateRecordingTimesFromLesson">
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
            <datetime class="w-100" id="startDatetime" type="datetime" v-on:input="approximateEndTime" value-zone="local" format="yyyy-MM-dd HH:mm" v-model="startDatetime"></datetime>
          </div>
        </div>

        <!-- end datetime of the recording -->
        <div class="row justify-content-center">
          <div class="col-2">
            <label for="endDatetime">Scheduled end</label>
          </div>
          <div class="col-2">
            <datetime class="w-100" id="endDatetime" type="datetime" v-on:input="calculateDuration" value-zone="local" format="yyyy-MM-dd HH:mm" v-model="endDatetime"></datetime>
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

        <!-- email for download link -->
        <div class="row justify-content-center"> 
          <div class="col-2">
            <label for="email">E-mail</label>
          </div>
          <div class="col-2">
            <input class="w-100" type="email" id="email" v-model="email" v-on:keyup="repetitionPreview" />
          </div>
        </div>

        <div class="row justify-content-center"> 
        <div class="col-6">
          <b-card no-body>
            <b-card-header header-tag="header" class="p-1" role="tab">
            <b-btn block v-b-toggle.repeatCollapse variant="btn">Repeat settings</b-btn>
            </b-card-header>
          </b-card>
            <b-collapse id="repeatCollapse">
              <b-card-body>
              <div class="row justify-content-center">
                <label for="repeat-every-week"><input id="repeat-every-week"  class="checkbox" type="checkbox" v-model="repeatEveryWeek" v-on:change="repetitionPreview"/> Repeat every week </label>
              </div>

              <div class="row justify-content-center">
                <label for="repeat-count">Repeat <input id="repeat-count"  type="number" v-model="repetitionCount" v-on:change="repetitionPreview"/> times </label>
              </div>
              <div class="row justify-content-center">
                <label for="suffix">Start number of title suffix <input id="suffix"  type="number" v-model="suffixStart" v-on:change="repetitionPreview"/></label>
              </div>
              
              <div class="card card-body" v-if="repeatEveryWeek">
                <h4>Preview</h4>
                <table class="table" >
                  <thead>
                    <th>Title</th>
                    <th>Start</th>
                    <th>End</th>
                  </thead>

                  <tbody>
                    <tr v-for="preview in repetitionPreviewList" :key="preview.id">
                      <td>{{ preview.title }}</td>
                      <td>{{ preview.startDatetime }}</td>
                      <td>{{ preview.endDatetime }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
              </b-card-body>
            </b-collapse>
          </div>
        </div>

        <button class="btn btn-primary m-2">Schedule</button>
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
       repetitionCount: 0,
       repeatEveryWeek: false,
       suffixStart: 1,
       repetitionPreviewList: [],
       email: ""
    }
  },
  components: {
    datetime: Datetime,
  },
  created: function () {
    this.getRooms();
  },
    watch: {
      startDatetime: function() {
        this.repetitionPreview();
      },
      endDatetime: function() {
        this.repetitionPreview();
      },
  },
  methods: {
    scheduleRecording() {
      this.startDatetime = moment(this.startDatetime).format('YYYY-MM-DDTHH:mm:ss')
      this.endDatetime = moment(this.endDatetime).format('YYYY-MM-DDTHH:mm:ss')

      let count = 1;
      if (this.repeatEveryWeek && this.repetitionCount != 0) {
        count = this.repetitionCount;
      }

      for (let i=0; i<count; i++) {
        // Calculate start and end time in each iteration
        let startDatetime = moment(this.startDatetime).add(7*i, 'days').format('YYYY-MM-DDTHH:mm:ss');
        let endDatetime = moment(this.endDatetime).add(7*i, 'days').format('YYYY-MM-DDTHH:mm:ss');
        let title = this.title;
        
        if (this.repeatEveryWeek && this.repetitionCount != 0) {
          title = title + '_' + (parseInt(this.suffixStart) + i);
        }
        
        this.postRecordingSchedule(title, startDatetime, endDatetime, this.rooms[this.selectedRoom], this.email)
      }  
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
        
    },
    postRecordingSchedule(title, startDatetime, endDatetime, room, email) {
      axios.post(API_URL + 'recording/schedule', {
        title: title,
        startTime: startDatetime,
        endTime: endDatetime,
        room: room,
        email: email
      }).then(() => {
        this.showSuccessAlert = true;
      }).catch(function () {
        this.showErrorAlert = true;
      })
    },
    repetitionPreview() {
      this.repetitionPreviewList = [];
      if (!this.repeatEveryWeek || this.repetitionCount == 0) {
        return;
      }
      this.startDatetime = moment(this.startDatetime).format('YYYY-MM-DDTHH:mm:ss');
      this.endDatetime = moment(this.endDatetime).format('YYYY-MM-DDTHH:mm:ss');

      let count = this.repetitionCount;

 

      for (let i=0; i<count; i++) {
        // Calculate start and end time in each iteration
        let startDatetime = moment(this.startDatetime).add(7*i, 'days').format('YYYY-MM-DDTHH:mm:ss');
        let endDatetime = moment(this.endDatetime).add(7*i, 'days').format('YYYY-MM-DDTHH:mm:ss');
        let title = this.title;
        title = title + '_' + (parseInt(this.suffixStart) + i);
      
        this.repetitionPreviewList.push({title: title, startDatetime: startDatetime, endDatetime: endDatetime})
      }  
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  #repeat-count {
    width: 50px;
  }
  #suffix {
    width: 50px;
  }
</style>
