<template>
  <div>
    <!-- 登录 -->
    <el-dialog title="用户登录" :visible.sync="loginDialogVisible" width="330px" :before-close="loginHandleClose"
      @closed='loginClosed'>
      <div>
        <el-form :model="loginFrom" :rules="loginRules" ref="loginFormRef" label-width="4px" class="demo-ruleForm">
          <el-form-item prop="mobile">
            <el-input v-model="loginFrom.mobile" placeholder="请输入登录手机号" prefix-icon="el-icon-mobile"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="loginFrom.password" placeholder="请输入登录密码" prefix-icon="el-icon-unlock" show-password>
            </el-input>
          </el-form-item>
        </el-form>
        <div style="text-align: right;color: #0099FF;cursor:pointer;margin-top: -10px;" @click='forgetPassword'> 忘记密码
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button type="warning" plain @click='login'>登录</el-button>
          <el-button type="info" plain @click='loginDialogVisible=false;registerDialogVisible=true'>注册</el-button>
        </span>
      </div>
    </el-dialog>

    <!-- 注册 -->
    <el-dialog title="注册" :visible.sync="registerDialogVisible" width="330px" :before-close="registerHandleClose"
      @closed='registerClosed'>
      <div>
        <el-form :model="registerFrom" :rules="registerRules" ref="registerFormRef" label-width="4px"
          class="demo-ruleForm">
          <el-form-item prop="userName">
            <el-input v-model="registerFrom.userName" placeholder="请输入用户名" prefix-icon="el-icon-user"></el-input>
          </el-form-item>
          <el-form-item prop="mobile">
            <el-input v-model="registerFrom.mobile" placeholder="请输入注册的手机号" prefix-icon="el-icon-mobile"></el-input>
          </el-form-item>
          <el-form-item prop="code">
            <el-input v-model="registerFrom.code" placeholder="输入手机验证码">
              <el-button slot="append" size="mini" style="padding: 0;" @click='getVerificationCode' :disabled='isabled'>
                {{btnText}}
              </el-button>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="registerFrom.password" placeholder="设置我的密码" prefix-icon="el-icon-unlock" show-password>
            </el-input>
          </el-form-item>
          <el-form-item>
            <center>
              <el-radio-group v-model="registerFrom.sex">
                <el-radio :label="1"><i class="el-icon-male"></i></el-radio>
                <el-radio :label="0"><i class="el-icon-female" :style="registerFrom.sex == 0 ? femaleColor:''"> </i>
                </el-radio>
              </el-radio-group>
            </center>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button type="warning" plain @click='register'>注册</el-button>
          <el-button type="info" plain @click='loginDialogVisible = true;registerDialogVisible=false'>登录</el-button>
        </span>
      </div>
    </el-dialog>



  </div>
</template>
<script>
  import {
    login,
    register,
    getcode
  } from 'network/user'
  export default {
    name: 'DialogBox',
    data() {
      //自定义手机验证规则
      var mobileValidate = (rule, value, callback) => {
        var myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
        if (value === null) {
          callback(new Error('请输入登录的手机号'));
        } else if (!myreg.test(value)) {
          callback(new Error('请输入有效的手机号'));
        } else {
          callback();
        }
      };
      return {
        loginDialogVisible: false,
        registerDialogVisible: false,
        loginFrom: {
          mobile: null,
          password: ''
        },
        //验证规则
        loginRules: {
          mobile: [{
            validator: mobileValidate,
            trigger: 'blur'
          }],
          password: [{
              required: true,
              message: '请输入密码',
              trigger: 'blur'
            },
            {
              min: 6,
              max: 12,
              message: '长度在 6 到 12 个字符',
              trigger: 'blur'
            }
          ]
        },
        registerFrom: {
          userName: '',
          mobile: null,
          password: '',
          sex: 1,
          code: ''
        },
        //验证规则
        registerRules: {
          userName: [{
              required: true,
              message: '请输入用户名',
              trigger: 'blur'
            },
            {
              min: 2,
              max: 15,
              message: '长度在 2 到 15 个字符',
              trigger: 'blur'
            }
          ],
          mobile: [{
            validator: mobileValidate,
            trigger: 'blur'
          }],
          code: [{
              required: true,
              message: '请输入验证码',
              trigger: 'blur'
            },
            {
              min: 4,
              max: 4,
              message: '长度在 4 个字符',
              trigger: 'blur'
            }
          ],
          password: [{
              required: true,
              message: '请输入密码',
              trigger: 'blur'
            },
            {
              min: 6,
              max: 12,
              message: '长度在 6 到 12 个字符',
              trigger: 'blur'
            }
          ]
        },
        btnText: '获取验证码',
        isabled: false,
        femaleColor: 'color:red',
      }
    },
    props: {
      displayLoginBox: {
        type: Boolean,
        default: false
      },
      displayRegisterBox: {
        type: Boolean,
        default: false
      }
    },
    watch: {
      displayLoginBox() {
        this.loginDialogVisible = this.displayLoginBox
      },
      displayRegisterBox() {
        this.registerDialogVisible = this.displayRegisterBox
      }
    },
    methods: {
      //忘记密码
      forgetPassword() {
        this.$message.warning('功能暂未开发,如有需要找回密码请添加博主微信，谢谢支持。');
      },
      //登录
      login() {
        this.$refs.loginFormRef.validate((valid) => {
          if (valid) {
            login(this.loginFrom).then(res => {
              if (res.status !== 200) return this.$message.error(res.message);
              if (res.data.ucenbterInfo.warningContent) {
                this.$message.error('获取一条警告信息：点击头像查看。');
              } else {
                this.$message({
                  message: res.message,
                  type: 'success'
                });
              }
              this.loginDialogVisible = false
              window.sessionStorage.setItem('token', res.data.token)
              window.sessionStorage.setItem('myInfo', JSON.stringify(res.data.ucenbterInfo))
              this.$store.commit('setMyInfo', res.data.ucenbterInfo)
            })
          } else {
            return false;
          }
        });
      },
      //注册
      register() {
        this.$refs.registerFormRef.validate((valid) => {
          if (valid) {
            register(this.registerFrom).then(res => {
              if (res.status !== 200) return this.$message.error(res.message);
              this.$message({
                message: res.message,
                type: 'success'
              });
              this.registerDialogVisible = false
              this.loginDialogVisible = true
            })
          } else {
            return false;
          }
        });
      },
      getVerificationCode() {
        if (this.registerFrom.mobile === null) return this.$message.error('请输入手机号');
        let mobile = this.registerFrom.mobile
        let myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
        if (!myreg.test(mobile)) return this.$message.error('请输入有效的手机号');
        getcode(mobile).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            message: res.message,
            type: 'success'
          });
          this.isabled = true
          let i = 60
          this.btnText = `60秒后重新获取`
          const countDown = setInterval(() => {
            i -= 1
            this.btnText = `${i}秒后重新获取`
            if (i == 0) {
              this.btnText = `获取验证码`
              this.isabled = false
              clearInterval(countDown)
            }
          }, 1000)
        })
      },
      loginClosed() {
        this.$emit('closedlogin')
        this.$refs.loginFormRef.resetFields()
        this.registerFrom.mobile = null
        this.registerFrom.password = ''
      },
      registerClosed() {
        this.$emit('closedRegister')
        this.$refs.registerFormRef.resetFields()
        this.registerFrom.userName = ''
        this.loginFrom.mobile = null
        this.loginFrom.password = ''
        this.loginFrom.sex = 1
        this.loginFrom.code = ''

      },
      loginHandleClose(done) {
        done();
      },
      registerHandleClose(done) {
        done();
      }
    }
  }
</script>
<style scoped>
  .el-button {
    width: 100%;
    margin: 5px;
  }

  /* .el-icon-male {
    color: rgb(64, 158, 255);
  }

  .el-icon-female {
    color: red;
  } */
</style>