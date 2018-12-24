<template>
    <div class="container">
        <div class="row">
            <h1 class="col">{{ room.name }}</h1>
        </div>
        <div class="row justify-content-center">
            <div class="col-2 font-weight-bold">Camera Ip Address</div>
            <div class="col-2">{{ room.ipAddress }}</div>
        </div>
        <div class="row justify-content-center">
            <div class="col-2 font-weight-bold">Audio Source</div>
            <div class="col-2">{{ room.audioSource }}</div>
        </div>
        <div class="row btn-group">
            <b-btn class="btn-danger" v-b-modal.deleteModal>Delete</b-btn>
        </div>

        <div>
            <b-modal id="deleteModal" title="Delete room entry?">
                <p class="my-4">Are you sure you want to delete this room entry?</p>
                <p><b>It cannot be undone.</b></p>
                <div slot="modal-footer" class="w-100">
                    <b-btn variant="outline-primary" @click="hideModal">Cancel</b-btn>
                    <b-btn variant="danger" @click="deleteRoom">Delete</b-btn>
                </div>
            </b-modal>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

const API_URL = process.env.VUE_APP_ROOT_API


export default {
  name: 'RoomDetail',
  data() {
    return {
      id: this.$route.params.id,
      room: ""
    }
  },
  mounted: function () {
      this.getRoomDetails();
  },
  methods: {
      getRoomDetails: function () {
          axios.get(API_URL + 'room/' + this.id)
          .then((response) => {
              this.room = response.data;
          })
      },
      deleteRoom: function () {
          axios.get(API_URL + 'room/' + this.id + '/delete')
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
