import Vue from 'vue'
import Router from 'vue-router'
import StudentDetail from '@/components/StudentDetail'
import StudentList from '@/components/StudentList'
import StudentAdd from '@/components/StudentAdd'
import PageNotFound from '@/components/PageNotFound'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'studentList',
      component: StudentList,
      meta: {
        keepalive: true
      }
    },
    {
      path: '/studentDetail',
      name: 'studentDetail',
      component: StudentDetail,
      meta: {
        keepalive: true
      }
    },
    {
      path: '/studentAdd',
      name: 'StudentAdd',
      component: StudentAdd,
      meta: {
        keepalive: true
      }
    },
    {
      path: '/404',
      name: 'PageNotFound',
      component: PageNotFound,
      meta: {
        keepalive: false
      }
    }
  ]
})
