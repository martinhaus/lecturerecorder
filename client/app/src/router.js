import Vue from 'vue'
import Router from 'vue-router'
import RecordingsList from '@/components/RecordingsList'
import RecordingDetails from '@/components/RecordingDetails'
import ScheduleRecording from '@/components/ScheduleRecording'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      component: RecordingsList
    },
    {
      path: '/recording/:id',
      component: RecordingDetails
    },
    {
      path: '/schedule',
      component: ScheduleRecording
    }
  ]
})