<template>
  <el-card>
    <div style="display: flex; justify-content: space-between;">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item>评论管理</el-breadcrumb-item>
        <el-breadcrumb-item>评论内容管理</el-breadcrumb-item>
      </el-breadcrumb>
      <el-button v-if='multipleSelection.length !== 0 && hiddenUserInfo' type="danger" size="mini" icon="el-icon-delete"
        @click='batchDeleteClickl' plain>批量删除
      </el-button>
    </div>




    <div v-if='!hiddenUserInfo'>
      <el-table :data="userInfo" stripe style="width: 100%" border>
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
              <i v-if='scope.row.warningContent' class="el-icon-warning-outline"
                style="color: red;font-size: 30px;"></i>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-tooltip content="删除用户" placement="bottom" effect="light">
              <el-button type="danger" icon="el-icon-delete" plain size="mini" @click='deleteUserBtn(scope.row)'>
              </el-button>
            </el-tooltip>
            <el-tooltip content="解除警告" placement="bottom" effect="light">
              <el-button type="success" icon="el-icon-check" plain size="mini" @click='disarmWarningBtn(scope.row)'>
              </el-button>
            </el-tooltip>
            <el-tooltip content="批量删除评论" placement="bottom" effect="light" v-if='multipleSelection.length !== 0'>
              <el-button type="danger" icon="el-icon-delete-solid" plain size="mini" @click='batchDeleteClickl'>
              </el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <div style="text-align: right;">
        <el-button style="padding: 2px;" type="info" icon="el-icon-circle-close" size="mini" round plain
          @click='removeUserInfoBtn'>移除</el-button>
      </div>
    </div>





    <el-table ref="multipleTable" :data="commentList" tooltip-effect="dark" style="width: 100%"
      @selection-change="handleSelectionChange">
      <el-table-column type="expand">
        <template slot-scope="scope">
          <div style="display: flex;">
            <div style="width: 70px;margin-top: 10px;">评论内容：</div>
            <div
              style="max-width: 700px;min-width: 300px; border: 1px solid #aaa;border-radius: 4px; padding: 10px;text-align: left;">
              {{scope.row.content}}
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="blogid" label="评论博客id" width="180">
      </el-table-column>
      <el-table-column prop="uid" label="评论者id">
      </el-table-column>
      <el-table-column label="回复" width="180">
        <template slot-scope="scope">
          <el-tag type="warning" effect="plain" v-if='scope.row.head'>
            {{scope.row.head}}
          </el-tag>
          <el-tag effect="plain" v-else>
            评论我的博客
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="commentTime" label="评论时间" width="180">
      </el-table-column>
    </el-table>
    <center>
      <el-pagination @current-change="handleCurrentChange" :current-page.sync="selectInfo.current"
        :page-size="selectInfo.size" layout="total, prev, pager, next" :total="total">
      </el-pagination>
    </center>

  </el-card>
</template>
<script>
  import {
    getCommentList,
    batchdelete,
    getByUid,
    updateUcenterInfo,
    deleteByuid
  } from 'network/admin'
  export default {
    name: 'CommentContent',
    components: {},
    data() {
      return {
        selectInfo: {
          current: 1,
          size: 10,
          uid: 0
        },
        userInfo: [],
        commentList: [],
        multipleSelection: [],
        total: null,
        hiddenUserInfo: true
      }
    },
    created() {
      if (this.$route.query.uid) {
        this.hiddenUserInfo = false
        this.selectInfo.uid = this.$route.query.uid
        this.getByUid()
      }
      this.getCommentList()
    },
    methods: {
      removeUserInfoBtn() {
        this.hiddenUserInfo = true
        this.selectInfo.uid = 0
        this.getCommentList()
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
      //解除警告
      disarmWarningBtn(row) {
        row.warningContent = ''
        updateUcenterInfo(row).then(res => {
          if (res.status !== 200) {
            this.$message.error(res.message);
            this.getByUid()
          } else {
            this.$message({
              type: 'success',
              message: '解除警告成功'
            });
            this.dialogVisible = false
          }
        })
      },
      //删除用户
      deleteUserBtn(row) {
        if (this.commentList.length !== 0) return this.$message.error('请将评论都删除，再删除用户！');
        this.$confirm(`此操作将永久删除【${row.userName}】该用户, 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteByuid(row.id).then(res => {
            if (res.status !== 200) return this.$message.error(res.message);
            this.$message({
              type: 'success',
              message: res.message
            });
            this.removeUserInfoBtn()
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      getByUid() {
        getByUid(this.selectInfo.uid).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.userInfo.push(res.data.userInfo)
        })
      },
      //当前页
      handleCurrentChange(val) {
        this.selectInfo.current = val
        this.getCommentList()
      },
      //批量删除
      batchDeleteClickl() {
        let Arr = []
        this.multipleSelection.forEach(item => {
          Arr.push(item.id)
        });
        batchdelete(Arr).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            type: 'success',
            message: res.message
          });
          this.getCommentList()
        })
      },

      handleSelectionChange(val) {
        this.multipleSelection = val;
      },

      getCommentList() {
        getCommentList(this.selectInfo).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.commentList = res.data.rows
          this.total = res.data.total
        })
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
</style>