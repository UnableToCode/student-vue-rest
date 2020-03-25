<template>
  <div id="app">
    <el-container v-if="$route.meta.keepalive">
      <el-header class="header">
        <keep-alive>
          <v_Header></v_Header>
        </keep-alive>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
    <router-view v-if="!$route.meta.keepalive"></router-view>
  </div>
</template>

<script>
import Header from './components/Header'

export default {
  name: 'App',
  components: {
    'v_Header': Header
  }
}
</script>

<style>
body {
  background: #dfdfdf;
}

#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
}
</style>
