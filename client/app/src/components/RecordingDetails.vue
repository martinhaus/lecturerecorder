<template>
    <div class="container">
        <video controls=""  width="640" height="360">
            <source src="http://clips.vorwaerts-gmbh.de/VfE_html5.mp4" type="video/mp4"><!-- Safari / iOS, IE9 -->
        </video>

        <div class="row">
            <h1 class="col">{{ recording.title }} | {{ recording.room.name }}</h1>
        </div>
        <div class="row">
            <h2 class="col">{{ recording.startTime }} - {{ recording.endTime }}</h2>
        </div>
        <div class="row">
            <h4 class="col-6 text-right">Scheduled at: </h4>
            <h4 class="col-6 text-left">{{ recording.datetimeCreated }}</h4>
        </div>
        <div class="row btn-group">
            <form method="get" :action="recordingDownloadUrl">
                <button type="submit" class="btn btn-primary" >Download</button>
            </form>
            <button class="btn btn-danger">Delete</button>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

const API_URL = process.env.VUE_APP_ROOT_API


export default {
  name: 'RecordingDetails',
  data() {
    return {
      id: this.$route.params.id,
      recording: null,
      recordingDownloadUrl: null
    }
  },
  mounted: function () {
      this.getRecordingDetails();
  },
  methods: {
      getRecordingDetails: function () {
          axios.get(API_URL + 'recording/' + this.id)
          .then((response) => {
              this.recording = response.data;
              this.recordingDownloadUrl = API_URL + 'recording/' + this.id + '/download'
          })
      }
  }
}
</script>

<style scoped>


</style>
