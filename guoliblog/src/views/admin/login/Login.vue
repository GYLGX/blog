<template>
  <div class='login'>
    <login-item></login-item>
  </div>
</template>
<script>
  import loginItem from './childComps/loginItem'
  export default {
    name: 'Login',
    components: {
      loginItem
    }
  }
</script>
<style scoped>
  .login {
    height: 100%;
    background: url('https://guoli368.oss-cn-shenzhen.aliyuncs.com/blog/imgs/back.jpg') no-repeat;
    background-size: 100% 100%;
  }
</style>