<template>
    <div class="container">
        
        <h3 class="font-italic" v-if="!recording.finished">Preview will be available after recording is finished</h3>

        
        <video v-if="recording.finished" controls=""  width="640" height="360">
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
                <button v-if="recording.finished" type="submit" class="btn btn-outline-primary m-1">Download</button>
                <button v-else disabled type="submit" class="btn btn-outline-primary m-1">Download</button>
            </form>
            <button v-if="recording.finished" @click="generateDownloadLink" type="submit" class="btn btn-outline-primary m-1">Generate download link</button>
            <button v-else disabled type="submit" class="btn btn-outline-primary m-1">Generate download link</button>
            <b-btn class="btn-danger m-1" v-b-modal.deleteModal>Delete</b-btn>
        </div>
        <transition name="fade">
            <div v-if="showDownloadUrl" class="row justify-content-center">
                <div class="col-8 text-center">
                    <input class="w-100 text-center" readonly type="text" v-model="downloadUrl" />
                </div>
            </div>
        </transition>
        

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
      recording: {
          room: {
              name: ""
          }
      },
      recordingDownloadUrl: null,
      downloadUrl: "",
      showDownloadUrl: false,
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
    },
    generateDownloadLink() {
        axios.post(API_URL + 'recording/' + this.id + '/create_download_link')
        .then((response) => {
            this.downloadUrl = API_URL + '/download/' + response.data 
            this.showDownloadUrl = true;
        })
    }
  }
}
</script>

<style scoped>


</style>
