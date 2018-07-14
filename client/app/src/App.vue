<template>
  <div id="app">
    <RecordingsList />
    <ScheduleRecording />
  </div>
</template>

<script>
import RecordingsList from './components/RecordingsList.vue'
import ScheduleRecording from './components/ScheduleRecording.vue'

export default {
  name: 'app',
  components: {
    RecordingsList,
    ScheduleRecording
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
