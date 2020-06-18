<template>
  <el-card>

    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>评论管理</el-breadcrumb-item>
      <el-breadcrumb-item>评论用户管理</el-breadcrumb-item>
    </el-breadcrumb>
    <div style="display: flex;justify-content: space-between; align-items: center;">
      <el-input placeholder="查找用户昵称" v-model="selectInfo.likes.userName" style="margin-top: 20px;width: 300px;"
        clearable>
        <el-button slot="append" icon="el-icon-search" @click='selectBtn'></el-button>
      </el-input>

      <div class="refresh-icon">
        <i @click='iconClick' :class="icon"></i>
      </div>
    </div>

    <el-table :data="userList" stripe style="width: 100%" border>
      <el-table-column prop="id" label="id" width="100">
      </el-table-column>
      <el-table-column prop="mobile" label="绑定手机" width="120">
      </el-table-column>
      <el-table-column prop="userName" label="昵称">
      </el-table-column>
      <el-table-column label="头像" width="90">
        <template slot-scope="scope">
          <img class="head-portrait " v-lazy="scope.row.url" alt="">
        </template>
      </el-table-column>
      <el-table-column prop="registerTime" label="注册时间" width="180">
      </el-table-column>
      <el-table-column label="是否启用" width="90">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.state" active-color="#FFD700" inactive-color="#ccc"
            @change='changeSwitch(scope.row)'>
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="正被警告" width="90">
        <template slot-scope="scope">
          <el-tooltip :content="'警告内容 : '+scope.row.warningContent" placement="bottom" effect="light">
            <i v-if='scope.row.warningContent' class="el-icon-warning-outline" style="color: red;font-size: 30px;"></i>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-tooltip content="添加-编辑警告" placement="bottom" effect="light">
            <el-button type="danger" icon="el-icon-warning-outline" plain size="mini" @click='warningBtn(scope.row)'>
            </el-button>
          </el-tooltip>
          <el-tooltip content="解除警告" placement="bottom" effect="light">
            <el-button type="success" icon="el-icon-check" plain size="mini" @click='disarmWarningBtn(scope.row)'>
            </el-button>
          </el-tooltip>
          <el-tooltip content="监控评论" placement="bottom" effect="light">
            <el-button type="warning" icon="el-icon-view" plain size="mini" @click='monitorBtn(scope.row)'>
            </el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加警告框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="30%" :before-close="handleClose"
      @close='closeWarning'>
      <el-form :model="warning" :rules="warningRules" ref="warningRef">
        <el-form-item prop="text">
          <el-input type="textarea" :rows="2" placeholder="请输入警告内容" v-model="warning.text">
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmBtn">确 定</el-button>
      </span>
    </el-dialog>

    <center>
      <el-pagination @current-change="handleCurrentChange" :current-page.sync="selectInfo.current"
        :page-size="selectInfo.size" layout="total, prev, pager, next" :total="total">
      </el-pagination>
    </center>

  </el-card>
</template>
<script>
  import {
    getUcenterList,
    updateUcenterInfo,
    deleteUcenterById
  } from 'network/admin'
  export default {
    name: 'CommentUcenbter',
    components: {},
    data() {
      return {
        selectInfo: {
          current: 1,
          size: 10,
          likes: {
            userName: ''
          }
        },
        userList: [],
        dialogVisible: false,
        dialogTitle: '',
        warning: {
          text: ''
        },
        warningRules: {
          text: [{
            required: true,
            message: '请输入警告内容',
            trigger: 'blur'
          }]
        },
        modifyData: {},
        total: null,
        icon: 'el-icon-refresh'
      }
    },
    created() {
      this.getUcenterList()
    },
    methods: {
      monitorBtn(row) {
        let Obj = {}
        Obj.uid = row.id
        this.$router.push({
          path: '/home/commentContent',
          query: Obj
        })
        window.sessionStorage.setItem('activePath', '/home/commentContent')
      },
      iconClick() {
        this.icon = 'el-icon-loading'
        this.selectInfo.current = 1
        this.selectInfo.size = 10
        this.selectInfo.likes.userName = ''
        getUcenterList(this.selectInfo).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.total = res.data.total
          this.userList = res.data.rows
          this.icon = 'el-icon-refresh'
          this.$message({
            type: 'success',
            message: '刷新最新数据'
          });
        })
      },
      selectBtn() {
        this.getUcenterList()
      },
      //当前页
      handleCurrentChange(val) {
        this.selectInfo.current = val
        this.getCommentList()
      },
      //解除警告
      disarmWarningBtn(row) {
        row.warningContent = ''
        updateUcenterInfo(row).then(res => {
          if (res.status !== 200) {
            this.$message.error(res.message);
            this.getUcenterList()
          } else {
            this.$message({
              type: 'success',
              message: '解除警告成功'
            });
            this.dialogVisible = false
          }
        })
      },
      //警告
      warningBtn(row) {
        this.dialogTitle = `对【${row.userName}】用户添加警告`
        this.dialogVisible = true
        this.modifyData = row
      },
      //确认按钮
      confirmBtn() {
        this.$refs.warningRef.validate((valid) => {
          if (valid) {
            this.modifyData.warningContent = this.warning.text
            updateUcenterInfo(this.modifyData).then(res => {
              if (res.status !== 200) {
                this.modifyData.warningContent = ''
                this.$message.error(res.message);
              } else {
                this.$message({
                  type: 'success',
                  message: '警告成功'
                });
                this.dialogVisible = false
              }
            })
          } else {
            return false;
          }
        });
      },
      closeWarning() {
        this.modifyData = {}
        this.$refs.warningRef.resetFields()
      },
      //开关
      changeSwitch(row) {
        updateUcenterInfo(row).then(res => {
          if (res.status !== 200) {
            row.state = !row.state
            this.$message.error(res.message);
          } else if (row.state) {
            this.$message({
              type: 'success',
              message: '启用成功'
            });
          } else {
            this.$message({
              type: 'warning',
              message: '禁用成功'
            });
          }
        })
      },
      getUcenterList() {
        getUcenterList(this.selectInfo).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.userList = res.data.rows
          this.total = res.data.total
        })
      },
      handleClose(done) {
        done()
      }
    }
  }
</script>
<style scoped>
  .head-portrait {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    margin-top: 5px;
    box-shadow: 0 0 8px 2px #aaa;
  }

  .refresh-icon {
    text-align: right;
    font-size: 25px;
  }
</style>