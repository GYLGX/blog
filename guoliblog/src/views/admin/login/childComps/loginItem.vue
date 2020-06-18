<template>
  <div class='login-item'>
    <div class="login-shine"></div>
    <div class="login-icon">
      <img src="https://guoli368.oss-cn-shenzhen.aliyuncs.com/blog/imgs/log.gif" alt="">
    </div>
    <div class="login-from">
      <!-- from表单 -->
      <el-form :model="ruleForm" :rules='loginrules' ref='loginFromRef'>
        <!-- 用户框 -->
        <el-form-item prop="username">
          <el-input prefix-icon="el-icon-user" v-model="ruleForm.username" placeholder='请输入用户名称'
            @keyup.enter.native='Enter'></el-input>
        </el-form-item>
        <!-- 密码框 -->
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-goods" v-model="ruleForm.password" placeholder='请输入密码' type='password'
            @keyup.enter.native='Enter'>
          </el-input>
        </el-form-item>
        <!-- 按钮 -->
        <el-form-item class="btns">
          <el-button type="primary" @click='login'>登录</el-button>
          <el-button type="info" @click='resetLoginFrom'>重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import {
    adminlogin
  } from 'network/admin'
  export default {
    name: 'loginItem',
    components: {},
    data() {
      return {
        //登录表单的数据
        ruleForm: {
          username: '',
          password: ''
        },
        //表单的验证规则
        loginrules: {
          username: [{
              required: true,
              message: '请输入用户名称',
              trigger: 'blur'
            },
            {
              min: 2,
              max: 10,
              message: '长度在 2 到 10 个字符',
              trigger: 'blur'
            }
          ],
          password: [{
              required: true,
              message: '请输入密码',
              trigger: 'blur'
            },
            {
              min: 5,
              max: 20,
              message: '长度在 5 到 20 个字符',
              trigger: 'blur'
            }
          ]
        }
      }
    },
    created() {

    },
    methods: {
      Enter() {
        this.login()
      },
      login() {
        this.$refs.loginFromRef.validate(valid => {
          if (!valid) {
            return
          } else {
            this.postKeyValueRequest(this.ruleForm)
          }
        })
      },
      //发送用户信息验证
      postKeyValueRequest(userInfo) {
        adminlogin(userInfo).then(res => {
          if (res.status !== 200) return this.$message.error('！ 对不起 , 你登录失败 , 用户名或密码错误');
          this.$message({
            message: '密码正确，登录成功',
            type: 'success'
          });
          window.sessionStorage.setItem('admintoken', res.data.token)
          delete res.data.token
          window.sessionStorage.setItem('loginer', JSON.stringify(res.data))
          this.$router.push('/home')
        })
      },
      //重置输入框
      resetLoginFrom() {
        this.$refs.loginFromRef.resetFields()
      }
    },
  }
</script>
<style scoped>
  .login-item {
    background-color: rgba(255, 255, 255, 0.26);
    position: fixed;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    height: 300px;
    width: 450px;
    border-radius: 10px;
  }

  .login-shine {
    width: 160px;
    height: 80px;
    border-radius: 120px 120px 0 0;
    box-shadow: -2px -2px 20px rgba(0, 0, 0, 0.671);
    background-color: rgba(255, 255, 255, 0.26);
    position: fixed;
    left: 50%;
    transform: translate(-50%, -100%);
  }

  .login-icon {
    background-color: rgba(255, 255, 255, 0.26);
    width: 140px;
    height: 140px;
    border-radius: 50%;
    padding: 10px;
    position: fixed;
    left: 50%;
    transform: translate(-50%, -50%);
  }

  .login-icon img {
    width: 100%;
    height: 100%;
    background-color: #eee;
    border-radius: 50%;
  }

  .login-from {
    position: fixed;
    bottom: 0;
    width: 100%;
    padding: 0 10px;
    box-sizing: border-box;
  }

  .el-input__inner {
    background-color: #000;
  }

  .btns {
    display: flex;
    justify-content: flex-end;
  }
</style>