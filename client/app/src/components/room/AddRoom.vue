<template>
  <div>
    <div class="container">
     <b-alert variant="success"
             dismissible
             fade
             :show="showSuccessAlert"
             @dismissed="showSuccessAlert=false">
      Room entry was successfully added to database
      </b-alert>
      <b-alert variant="danger"
             dismissible
             fade
             :show="showErrorAlert"
             @dismissed="showErrorAlert=false">
      Failed to create new room entry
      </b-alert>
    </div>
    
    <h1>Add Room</h1>
    <h5>Fill the form below to create new room entry</h5>
    
    <div class="container">
        <form @submit.prevent="scheduleRecording">

        <!-- name of the room -->
        <div class="row justify-content-center">
          <div class="col-2">
            <label for="title">Name</label>
          </div>
          <div class="col-2">
            <input type="text" id="title" v-model="name" />
          </div>
        </div>

        <!-- ip address of camera -->
        <div class="row justify-content-center">
          <div class="col-2">
            <label for="ipAddress">Camera Ip Address</label>
          </div>
          <div class="col-2">
            <input id="ipAddress" type="text" v-model="ipAddress" />
          </div>
        </div>

        <!-- audio source name  -->
        <div class="row justify-content-center">
          <div class="col-2">
            <label for="audioSource">Audio Source</label>
          </div>
          <div class="col-2">
            <input id="audioSource" type="text" v-model="audioSource" />
          </div>
        </div>

        <div class="row justify-content-center">
          <div class="col-2">
            <label for="cameraScene">Camera Scene</label>
          </div>
          <div class="col-2">
            <input id="cameraScene" type="text" v-model="cameraScene" />
          </div>
        </div>

        <!-- available rooms for recordings -->
        <div class="row justify-content-center">
          <div class="col-2">
            <label for="room">Room</label>
          </div>
          <div class="col-2">
            <select v-model='selectedRoomOption' id="timetable">
              <option v-for="(roomOption, index) in roomOptions" :key="roomOption.id" :value="index">{{ roomOption.title }}</option>
            </select>
          </div>
        </div>
        
        <div class="row justify-content-center">
         <button class="btn btn-primary">Submit</button> 
        </div>

       </form>
    </div>
   

  </div>
</template>

<script>

import axios from 'axios';

const API_URL = process.env.VUE_APP_ROOT_API;

export default {    
  data () {
      return {
          name: '',
          ipAddress: '',
          audioSource: '',
          roomOptions: {},
          selectedRoomOption: 0,
          cameraScene: null,
          showSuccessAlert: false,
          showErrorAlert: false
      }
  },
  created: function () {
    this.getRoomOptions();
  },
  methods: {
      scheduleRecording() {
      axios.post(API_URL + 'rooms/add', {
          name: this.name,
          ipAddress: this.ipAddress,
          audioSource: this.audioSource,
          timetableId: this.roomOptions[this.selectedRoomOption].id,
          timetableRoomName: this.roomOptions[this.selectedRoomOption].title,
          cameraScene: this.cameraScene,
      }).then(() => {
          this.showSuccessAlert = true;
      }).catch(function () {
          this.showErrorAlert = true;
      })
      },
      getRoomOptions() {
        axios.get(API_URL + 'timetables/available')
        .then((response) => {
          this.roomOptions = response.data;
        })
      }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
