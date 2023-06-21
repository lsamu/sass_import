import Vue from 'vue'
import VueRouter, { RouteConfig } from 'vue-router'

Vue.use(VueRouter)

//获取原型对象上的push函数
const originalPush = VueRouter.prototype.push
//修改原型对象中的push方法
VueRouter.prototype.push = function push(location:any) {
  return originalPush.call(this, location).catch((err:any) => err)
}

const routes: Array<RouteConfig> = [
  {
    path: '/',
    name: 'home',
    component: () => import("../views/demo/index.vue")
  },
]

const router = new VueRouter({
  routes
});

export default router
