<template>
  <div style="text-align: right; width: 100%;">
    <div v-if='$store.state.myInfo.userName' style="display: flex;align-items: center;flex-direction: row-reverse;">
      <div class="show-name" style="margin: 10px; color: rgb(112, 111, 111);font-weight: bold;font-size: 25px;">
        {{$store.state.myInfo.userName}}</div>
      <img class="head-portrait" :src="$store.state.myInfo.url" alt="" @click='openDrawer'>
    </div>
    <span v-else>
      <i style="color: #ccc; margin-right: 10px;font-size: 20px;" class="el-icon-s-custom"></i>
      <span style="color: rgb(44, 160, 44);cursor:pointer;" @click='loginClick'>登录</span> / <span
        style="cursor:pointer;color: rgb(255, 72, 0);" @click='registerClick'>注册</span>
    </span>
  </div>
</template>
<script>
  export default {
    name: 'LoginModule',
    data() {
      return {
        myInfo: this.$store.state.myInfo,
        counter: this.$store.state.counter
      }
    },
    created() {
      const ucenbterInfo = JSON.parse(window.sessionStorage.getItem('myInfo'))
      if (ucenbterInfo) {
        this.$store.commit('setMyInfo', ucenbterInfo)
      }
    },
    methods: {
      openDrawer() {
        this.drawer = true
        this.$emit('openDrawerBox')
      },
      loginClick() {
        this.$emit("displayLogin")
      },
      registerClick() {
        this.$emit("displayRegister")
      },
    }
  }
</script>
<style scoped>
  /* PC端 */
  @media only screen and (min-width: 1029px) {

    .head-portrait {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      box-shadow: 0 0 10px 5px #aaa;
    }
  }

  /* 移动端 */
  @media only screen and (min-width: 320px) and (max-width: 1024px) {

    .head-portrait {
      width: 8vw;
      height: 8vw;
      border-radius: 50%;
      box-shadow: 0 0 5px 1px #aaa;
      margin: 0 2vw;
    }

    .show-name {
      display: none;
    }
  }
</style>