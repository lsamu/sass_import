import Vue from 'vue';

import App from './App.vue';
import router from './router'
import store from './store'

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(ElementUI, { size: 'mini' });

import 'vue-awesome/icons'
import Icon from 'vue-awesome/components/Icon'
Vue.component('v-icon', Icon)

import pinia from "./store/pinia"

new Vue({
  router,
  store,
  pinia,
  render: (h) => h(App),
}).$mount('#app');