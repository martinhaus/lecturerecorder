<template>
  <div>
    <h1>Schedule recording</h1>
    <h5>Fill the form below to schedule new recording</h5>

    
    <div class="container">
      <form @submit.prevent="scheduleRecording">

        <!-- available rooms for recordings -->
        <div class="row">
          <label for="room">Room</label>
          <select id="room">
            <option>aaa</option>
          </select>
        </div>
        
        <!-- start datetime of scheduled recording -->
        <div class="row">
          <div class="col-md-12">
        <!-- <date-picker v-model="date" :config="options"></date-picker> -->
        <datetime v-model="date"></datetime>
        </div>

        <!-- end datetime of the recording -->
        <div class="row">
          <div class="col-md-12">
            <!-- <date-picker v-model="date" :config="options"></date-picker> -->
          </div>
        </div>

        <!-- duration based on selected times -->
        <div class="row"> 
        </div>
          
        </div>
       </form>
    </div>
   

  </div>
</template>

<script>


export default {    
  data () {
    return {

    }
  },
  components: {
    
  },
  methods: {
    scheduleRecording() {
      alert('Scheduled')
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
