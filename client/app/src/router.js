import Vue from 'vue'
import Router from 'vue-router'
import RecordingsList from '@/components/recording/RecordingsList'
import RecordingDetails from '@/components/recording/RecordingDetails'
import ScheduleRecording from '@/components/recording/ScheduleRecording'
import RoomList from '@/components/room/RoomList'
import AddRoom from '@/components/room/AddRoom'

Vue.use(Router)

export default new Router({
  mode: 'hash',
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
    },
    {
      path: '/rooms',
      component: RoomList
    },
    {
      path: '/rooms/add',
      component: AddRoom
    }
  ]
})