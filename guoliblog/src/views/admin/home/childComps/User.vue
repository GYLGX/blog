<template>
  <el-card>
    <el-row>
      <el-col :span="12">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item>系统管理</el-breadcrumb-item>
          <el-breadcrumb-item>用户管理</el-breadcrumb-item>
        </el-breadcrumb>
      </el-col>
      <el-col :span="12">
        <el-button size="mini" type="warning" plain icon="el-icon-circle-plus-outline" @click="addUserBtn ">添加新用户
        </el-button>
      </el-col>
    </el-row>
    <el-table :data="tableData" stripe style="width: 100%" border stripe>
      <el-table-column prop="userId" label="编码号" width="380">
      </el-table-column>
      <el-table-column prop="userName" label="名称" width="180">
      </el-table-column>
      <el-table-column prop="description" label="备注" width="280">
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini" icon="el-icon-edit" type="primary" @click="handleEdit(scope.row)">修改</el-button>
          <el-button size="mini" icon="el-icon-delete" type="danger" @click='handleDelete(scope.row)'>删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync=" dialogVisible" width="30%" :before-close="handleClose"
      @close='dialogClose'>

      <el-form :model="userInfoFrom" :rules="userInfoFromRules" ref="userInfoFromRef" label-width="100px">
        <el-form-item label="用户编码号" v-show="dialogTitle !== '添加新用户' ">
          <el-input v-model="userInfoFrom.id" disabled></el-input>
        </el-form-item>
        <el-form-item label="用户名称" prop="name">
          <el-input v-model="userInfoFrom.name"></el-input>
        </el-form-item>
        <el-form-item label="用户密码" prop="passw">
          <el-input v-model="userInfoFrom.passw" type='password'></el-input>
          默认密码：000000
        </el-form-item>
        <el-form-item label="用户备注" prop="des">
          <el-input v-model="userInfoFrom.des"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmBtn">确 定</el-button>
      </span>
    </el-dialog>

  </el-card>
</template>
<script>
  import {
    getUserlList,
    deleteUser,
    addUser,
    updateUserInfo
  } from 'network/admin'
  export default {
    name: 'User',
    components: {},
    data() {
      return {
        //校验规则
        userInfoFromRules: {
          name: [{
              required: true,
              message: '请输入用户名称',
              trigger: 'blur'
            },
            {
              min: 2,
              max: 12,
              message: '长度在 2 到 12 个字符',
              trigger: 'blur'
            }
          ],
          passw: [{
              required: true,
              message: '请输入用户密码',
              trigger: 'blur'
            },
            {
              min: 6,
              max: 12,
              message: '长度在 6 到 12 个字符',
              trigger: 'blur'
            }
          ],
          des: [{
              required: true,
              message: '请输入用户备注',
              trigger: 'blur'
            },
            {
              min: 3,
              max: 15,
              message: '长度在 3 到 15 个字符',
              trigger: 'blur'
            }
          ],
        },
        dialogTitle: '',
        userInfoFrom: {
          id: '',
          name: '',
          passw: '000000',
          des: ''
        },
        dialogVisible: false,
        tableData: []
      }
    },
    created() {
      this.getUserlList()
    },
    methods: {
      confirmBtn() {
        this.$refs.userInfoFromRef.validate((valid) => {
          if (valid) {
            if (this.dialogTitle === '修改用户信息') {
              this.updateUserInfo(this.userInfoFrom)
            } else {
              delete this.userInfoFrom.id
              this.addUser(this.userInfoFrom)
            }
          } else {
            return false;
          }
        })
      },
      updateUserInfo(userInfo) {
        updateUserInfo(userInfo).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            type: 'success',
            message: res.message
          });
          this.dialogVisible = false
          this.getUserlList()
        })
      },
      addUser(userInfo) {
        addUser(userInfo).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            type: 'success',
            message: res.message
          });
          this.dialogVisible = false
          this.getUserlList()
        })
      },
      dialogClose() {
        this.userInfoFrom.id = ''
        this.userInfoFrom.name = ''
        this.userInfoFrom.passw = '000000'
        this.userInfoFrom.des = ''
      },
      addUserBtn() {
        this.dialogTitle = '添加新用户'
        this.dialogVisible = true
      },
      handleClose(done) {
        done()
      },
      getUserlList() {
        getUserlList().then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.tableData = res.data.info
        })
      },
      handleEdit(row) {
        this.dialogTitle = '修改用户信息'
        this.dialogVisible = true
        this.userInfoFrom.id = row.userId
        this.userInfoFrom.name = row.userName
        this.userInfoFrom.des = row.description
      },
      handleDelete(row) {
        this.$confirm(`此操作将永久删除【${row.userName}】该用户 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteUser(row.userId).then(res => {
            this.$message({
              type: 'info',
              message: res.message,
            });
            this.getUserlList()
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消操作',
          });
        });
      }
    },
  }
</script>
<style scoped>
</style>