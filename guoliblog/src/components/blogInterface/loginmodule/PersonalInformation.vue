<template>
  <span>
    <el-drawer :visible.sync="drawerBox" :direction="directionrtl" :with-header="false" @close='closeDrawer'
      @open='openDrawer' size='300px'>
      <div>
        <center>
          <el-image class="head-portrait" :src="myInfo.url" :preview-src-list="[myInfo.url]">
          </el-image>
          <div style="color: goldenrod;"><i class="el-icon-key"></i>: {{myInfo.id}}</div>
        </center>
        <div>
          <div style="margin: 10px;">
            <span>昵称: </span>
            <el-tag type="success" effect="plain">{{myInfo.userName}} </el-tag>
          </div>
          <div style="margin: 10px;">
            <span>已绑定手机: </span>
            <el-tag effect="plain">{{myInfo.mobile}} </el-tag>
          </div>
          <div style="margin: 10px;">
            <span>状态: </span>
            <el-tag v-if='myInfo.state' type="success">正常</el-tag>
            <el-tag v-else type="info">禁用</el-tag>
          </div>
          <div style="margin: 10px;display: flex; justify-content: space-between;align-items: center;">
            <span>
              <span>性别: </span>
              <i v-if='myInfo.sex === 1' class="el-icon-male" style="color: #0099FF;"> </i>
              <i v-else class="el-icon-female" style="color: #D52B4D;"></i>
            </span>
            <el-button type="info" plain @click='modifyMyInfoClick'>编辑个人信息</el-button>
          </div>
        </div>
        <el-alert v-if='myInfo.warningContent' title="警告信息！" type="error" :description="myInfo.warningContent"
          show-icon>
        </el-alert>
        <div style="text-align: center; position: fixed; bottom: 0;width: 300px;">
          <el-button style="margin: 10px;" type="info" plain @click='modifyPassword'>修改密码</el-button>
          <el-button style="margin: 10px;" type="info" plain @click='signOut'>退出该用户</el-button>
        </div>

      </div>
    </el-drawer>

    <!-- 修改个人信息 -->
    <el-dialog title="修改个人信息" :visible.sync="modifyDialogVisible" width="330px" :before-close="modifyHandleClose"
      @closed='modifyClosed'>
      <div class="flex-box">
        <img class="head-portrait" :src="modifyMyInfoForm.url" alt="">
        <el-upload class="upload-demo" action="http://localhost:8881/blogoss/fileoss/uploadavatar" :headers='tokens'
          :show-file-list='false' :on-change="handleChange">
          <el-button style="margin: 10px;" type="info" size="mini" plain>更换头像</el-button>
        </el-upload>
      </div>
      <el-form :model="modifyMyInfoForm" :rules="modifyMyInfoRules" ref="modifyMyInfoRef">
        <el-form-item prop="userName">
          <el-input prefix-icon="el-icon-user" v-model="modifyMyInfoForm.userName"></el-input>
        </el-form-item>
        <el-form-item>
          <center>
            <el-radio-group v-model="modifyMyInfoForm.sex">
              <el-radio :label="1"><i class="el-icon-male"></i></el-radio>
              <el-radio :label="0"><i class="el-icon-female" :style="modifyMyInfoForm.sex == 0 ? femaleColor:''"> </i>
              </el-radio>
            </el-radio-group>
          </center>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="modifyDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateMyInfo">确 定</el-button>
      </span>
    </el-dialog>







    <!-- 修改个人信息 -->
    <el-dialog title="修改密码" :visible.sync="modifyPasswordDialogVisible" width="330px" :before-close="modifyHandleClose"
      @closed='modifyPasswdClosed'>

      <el-form :model="modifyPasswordInfo" status-icon :rules="modifyPasswordRules" ref="modifyPasswordRef"
        label-width="100px" class="demo-ruleForm">
        <el-form-item label="密码" prop="oldpassword">
          <el-input type="password" v-model="modifyPasswordInfo.oldpassword" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newpassword">
          <el-input type="password" v-model="modifyPasswordInfo.newpassword" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass">
          <el-input type="password" v-model="modifyPasswordInfo.checkPass" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="modifyPasswordDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="modifyPasswordConfirm">确 定</el-button>
      </span>
    </el-dialog>


  </span>
</template>
<script>
  import {
    updateMyInfo,
    changePasswordMethod
  } from 'network/user'
  export default {
    name: 'PersonalInformation',
    data() {
      var judgepass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (value.length < 6 || value.length > 12) {
            callback(new Error('最少 6 位 最多 12位'));
          }
          callback();
        }
      };
      var confirmPassword = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.modifyPasswordInfo.newpassword) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        modifyPasswordDialogVisible: false,
        modifyPasswordInfo: {
          oldpassword: '',
          newpassword: '',
          checkPass: ''
        },
        modifyPasswordRules: {
          oldpassword: [{
            validator: judgepass,
            trigger: 'blur'
          }],
          newpassword: [{
            validator: judgepass,
            trigger: 'blur'
          }],
          checkPass: [{
            validator: confirmPassword,
            trigger: 'blur'
          }],
        },
        tokens: {
          token: ''
        },
        femaleColor: 'color:red',
        //抽屉
        directionrtl: 'rtl',
        drawerBox: false,
        myInfo: {},
        modifyDialogVisible: false,
        modifyMyInfoForm: {
          id: null,
          url: '',
          userName: '',
          sex: null,
        },
        modifyMyInfoRules: {
          userName: [{
              required: true,
              message: '请输入用户昵称',
              trigger: 'blur'
            },
            {
              min: 2,
              max: 15,
              message: '长度在 2 到 15 个字符',
              trigger: 'blur'
            }
          ]
        }
      }
    },
    props: {
      drawer: {
        type: Boolean,
        default: false
      }
    },
    watch: {
      drawer() {
        this.drawerBox = this.drawer
      }
    },
    methods: {
      //修改密码关闭
      modifyPasswdClosed() {
        this.$refs.modifyPasswordRef.resetFields()
      },
      //确认修改密码
      modifyPasswordConfirm() {
        this.$refs.modifyPasswordRef.validate((valid) => {
          if (valid) {
            changePasswordMethod(this.modifyPasswordInfo).then(res => {
              if (res.status !== 200) return this.$message.error(res.message);
              this.$message({
                message: res.message,
                type: 'success'
              });
              this.modifyPasswordDialogVisible = false
            })
          } else {
            return false;
          }
        });
      },
      //重置密码
      modifyPassword() {
        this.modifyPasswordDialogVisible = true
      },
      //退出登录
      signOut() {
        this.$confirm('此操作将退出用户登录 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$store.commit('signOutLogin')
          this.drawerBox = false
          this.$message.warning("已退出该用户")
          window.sessionStorage.clear()
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消操作',
          });
        });
      },
      handleChange(file, fileList) {
        if (file.response) {
          if (file.response.status !== 200) return this.$message.error(file.response.message);
          this.$message({
            message: file.response.message,
            type: 'success'
          });
          this.modifyMyInfoForm.url = file.response.data.url
          this.$store.commit('updateMyInfoUrl', file.response.data.url)
        }
      },
      //确认按钮
      updateMyInfo() {
        this.$refs.modifyMyInfoRef.validate((valid) => {
          if (valid) {
            this.updateMyInfo()
          } else {
            return false;
          }
        });
      },
      updateMyInfo() {
        updateMyInfo(this.modifyMyInfoForm).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            message: res.message,
            type: 'success'
          });
          this.$store.commit('updateMyInfo', this.modifyMyInfoForm)
          this.modifyDialogVisible = false
          this.myInfo = this.$store.state.myInfo
        })
      },
      modifyClosed() {
        this.$refs.modifyMyInfoRef.resetFields()
      },
      modifyMyInfoClick() {
        this.modifyMyInfoForm.id = this.myInfo.id
        this.modifyMyInfoForm.url = this.myInfo.url
        this.modifyMyInfoForm.userName = this.myInfo.userName
        this.modifyMyInfoForm.sex = this.myInfo.sex
        this.modifyDialogVisible = true
      },
      modifyHandleClose(done) {
        done()
      },
      openDrawer() {
        this.myInfo = this.$store.state.myInfo
        this.tokens.token = sessionStorage.getItem('token')
      },
      closeDrawer() {
        this.$emit('closeDrawerBox')
      }
    }
  }
</script>
<style scoped>
  .flex-box {
    display: flex;
    justify-content: space-evenly;
    align-items: center;
    margin-top: -20px;
    margin-bottom: 10px;
  }

  .head-portrait {
    width: 60px;
    height: 60px;
    border-radius: 50%;
    margin: 10px;
    box-shadow: 0 0 8px 5px #aaa;
  }
</style>