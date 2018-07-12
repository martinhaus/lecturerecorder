<template>
  <div>
    <h1>Schedule recording</h1>
    <h5>Fill the form below to schedule new recording</h5>

    
    <div class="container">
      <form @submit.prevent="scheduleRecording">

        <!-- available rooms for recordings -->
        <div class="row justify-content-center">
          <div class="col-2">
            <label for="room">Room</label>
          </div>
          <div class="col-2">
            <select id="room">
              <option>aaa</option>
            </select>
          </div>
        </div>
        
        <!-- start datetime of scheduled recording -->
        <div class="row justify-content-center">
          <div class="col-2">
            <label for="startDatetime">Scheduled start</label>
          </div>
          <div class="col-2">
            <datetime id="startDatetime" type="datetime" v-on:input="calculateDuration" format="yyyy-MM-dd HH:mm" v-model="startDatetime"></datetime>
          </div>
        </div>

        <!-- end datetime of the recording -->
        <div class="row justify-content-center">
          <div class="col-2">
            <label for="endDatetime">Scheduled end</label>
          </div>
          <div class="col-2">
            <datetime id="endDatetime" type="datetime" v-on:input="calculateDuration" format="yyyy-MM-dd HH:mm" v-model="endDatetime"></datetime>
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
       </form>
    </div>
   

  </div>
</template>

<script>

import { Datetime } from 'vue-datetime';
import 'vue-datetime/dist/vue-datetime.css';
import moment from 'moment';

export default {    
  data () {
    return {
       startDatetime: null,
       endDatetime: null,
       duration: 0,
    }
  },
  components: {
    datetime: Datetime
  },
  methods: {
    scheduleRecording() {
      alert('Scheduled')
    },
    calculateDuration: function() {
      if (this.endDatetime != '' && this.startDatetime != '') {
        this.duration = moment(this.endDatetime).diff(this.startDatetime);
        this.duration = moment.duration(this.duration).asMinutes();
      }      
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
