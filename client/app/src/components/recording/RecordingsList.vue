<template>
  <div class="container">
    <div class="row mb-2">
      <b-btn class="btn-danger m-1" v-b-modal.deleteModal>Delete all selected</b-btn>
      <div class="col-5"></div>
      <div class="col-2  my-auto">
        <p class="my-auto">Filter recordings: </p>
      </div>
      <div class="col-3 align-items-center">
        <div class="row h-100">
          <input class="form-control col-6 " type="text" v-model="search" placeholder="name, id, date..."/> 
          <select v-model="filter" class="form-control col-5 ml-2 h-100">
            <option value="all" >all</option>
            <option value="past">past</option>
            <option value="future">future</option>
            <option value="current">current</option>
          </select>
        </div>
      </div>
      
    </div>
    <table class="table table-hover">
      <thead>
        <tr>
          <!-- <th style="padding: 0rem" scope="col"><p style="margin-bottom: 0rem;">Select all</p><input type="checkbox" ></th> -->
          <th style="padding: 0rem" scope="col"><p style="margin-bottom: 0rem;">Select all</p><input type="checkbox" v-model="selectAll" ></th>
          <th scope="col"><a @click="sortById"># <font-awesome-icon icon="arrows-alt-v" /></a></th>
          <!-- <a href="#" @click="sortById">tes</a> -->
          <th scope="col"  ><a @click="sortByName">Name <font-awesome-icon icon="arrows-alt-v" /></a> </th>
          <th scope="col" class="px-0"><a @click="sortByFinished">Status <font-awesome-icon icon="arrows-alt-v" /></a></th>
          <th scope="col" ><a @click="sortByRoom">Room <font-awesome-icon icon="arrows-alt-v" /></a></th>
          <th scope="col"><a @click="sortByStart">Start <font-awesome-icon icon="arrows-alt-v" /></a></th>
          <th scope="col">End</th>
          <th scope="col">Duration</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="recording in filteredList" :key="recording.id">
          <td><input type="checkbox"   :value="recording.id" v-model="checkedRecordings"></td>
          <td>{{ recording.id }}</td>
          <router-link tag="td" :to="{path: '/recording/' + recording.id}">{{ recording.title }}
            <div v-if="recording.active" class="progress">
              <div class="progress-bar progress-bar-striped progress-bar-animated bg-info" 
              role="progressbar" aria-valuenow="10" aria-valuemin="0" 
              aria-valuemax="100"
               :style="calculateProgress(recording.startTime, recording.endTime)"></div>
            </div>
          </router-link>
          <td>
            <!-- {{recording.finished}} -->
            <div><font-awesome-icon v-if="recording.finished" icon="check-circle" /></div>
            <p v-if="!recording.finished">-</p>
          </td>
          <td>{{ recording.room.name }}</td>
          <td>{{ recording.startTime }}</td>
          <td>{{ recording.endTime }}</td>
          <td>{{ calculateDurationMinutes(recording.startTime, recording.endTime) }} minutes</td>
        </tr>
      </tbody>
    </table>
    <div>
        <!-- Modal Component -->
        <b-modal id="deleteModal" title="Delete recording?">
            <p class="my-4">Are you sure you want to delete all selected recordings?</p>
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
import moment from 'moment'

const API_URL = process.env.VUE_APP_ROOT_API

export default {
  name: 'RecordingsList',
  data() {
    return {
      recordings: [],
      search: "",
      filter: "all",
      checkedRecordings: [],
      selectAll: false,
      sortBy: "id",
      operator: -1
    }
  },
  created: function () {
    this.getRecordings();
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
    },
    deleteRecording: function () {
      this.checkedRecordings.forEach(id => {
        axios.get(API_URL + 'recording/' + id + '/delete');
      });
      location.reload();
    },
    sortByName: function() {
      this.operator *= -1; 
      this.sortBy = "title";
    },
    sortById: function() {
      this.operator *= -1; 
      this.sortBy = "id";
    },
    sortByStart: function() {
      this.operator *= -1; 
      this.sortBy = "startTime";
    },
    sortByRoom: function() {
      this.operator *= -1; 
      this.sortBy = "room";
    },
    sortByFinished: function() {
      this.operator *= -1; 
      this.sortBy = "finished";
    }
  },
  watch: {
    selectAll: function() {
      if(this.selectAll){
        for (let rec in this.filteredList){
          this.checkedRecordings.push(this.filteredList[rec].id)
        }
      } else {
        this.checkedRecordings = [];
      }
    },
    search: function() {
      this.checkedRecordings = [];
    }
  },
  computed: {
    filteredList() {
      let date = new Date();
      return this.recordings.filter(recording => {
        let recordingDate = new Date(recording.startTime);
        if(this.filter == "all") return true;
        if(this.filter == "past" && recordingDate < date) return true;
        if(this.filter == "future" && recordingDate > date) return true;
        if(this.filter == "current" && recordingDate > date.setDate(date.getDate() - 7) && recordingDate < date.setDate(date.getDate() + 7)) return true;
        return false;
      }).filter(recording => {
        return (recording.id.toString().toLowerCase().includes(this.search.toLowerCase()) || recording.finished.toString().toLowerCase().includes(this.search.toLowerCase()) || recording.title.toLowerCase().includes(this.search.toLowerCase()) || recording.startTime.toLowerCase().includes(this.search.toLowerCase()) || recording.room.name.toLowerCase().includes(this.search.toLowerCase()) || recording.endTime.toLowerCase().includes(this.search.toLowerCase()))
      }).sort((a,b) => {
          if(this.sortBy == "room")
          {
            if (a[this.sortBy]["name"] < b[this.sortBy]["name"])
              return -1 * this.operator;
            if (a[this.sortBy]["name"] > b[this.sortBy]["name"])
              return 1 * this.operator;
            return 0;
          }
          if (a[this.sortBy] < b[this.sortBy])
            return -1 * this.operator;
          if (a[this.sortBy] > b[this.sortBy])
            return 1 * this.operator;
          return 0;
      });

    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
