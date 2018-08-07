<template>
    <div class="container">
        <video controls=""  width="640" height="360">
             <source :src="recordingDownloadUrl" type="video/mp4">
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
            <b-btn class="btn-danger" v-b-modal.deleteModal>Delete</b-btn>
        </div>

        <div>
            <!-- Modal Component -->
            <b-modal id="deleteModal" title="Delete recording?">
                <p class="my-4">Are you sure you want to delete this recording?</p>
                <p><b>It cannot be undone.</b></p>
                <div slot="modal-footer" class="w-100">
                    <b-btn variant="outline-primary" @click="hideModal">Cancel</b-btn>
                    <b-btn variant="danger" @click="deleteRecording">Delete</b-btn>
                </div>
            </b-modal>
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
      recording: {},
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
      },
      deleteRecording: function () {
          axios.get(API_URL + 'recording/' + this.id + '/delete')
          .then(() => {
            this.$router.push('/')
          })
      },
      hideModal () {
        this.$root.$emit('bv::hide::modal','deleteModal')
    }
  }
}
</script>

<style scoped>


</style>
