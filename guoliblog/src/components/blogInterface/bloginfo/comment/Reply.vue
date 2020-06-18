<template>
  <div>
    <div class="reply" v-if='replyList.length !== 0 '>

      <div v-if='showCommentsBox' style="color: #aaa;cursor:pointer;" @click='showComments'> =》 显示 {{replyList.length}}
        回复评论
        . . . </div>
      <div v-else>
        <div style="color: #aaa;"> <i class="el-icon-edit-outline"></i> 共 {{replyList.length}} 条回复:</div>
        <ReplyItem v-for='(item,index) in replyList' :index='index' :info='item' @addreplytoArr='addreplytoArr'
          @deleteContent='deleteContent(index)'>
        </ReplyItem>
      </div>
    </div>

    <!-- 回复评论框 -->
    <el-dialog :title="replyTitle" :visible.sync="replyDialogVisible" width="300px" :before-close="replyHandleClose"
      @open='openReplyBox' @closed='closedReply' :modal='false'>
      <el-form :model="replyFrom" :rules="replyRules" ref="replyRef">
        <el-form-item prop="content">
          <el-input type="textarea" :rows="2" placeholder="请输入评论内容" v-model="replyFrom.content">
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="replyDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="replyConfirm">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
  import {
    getreply,
    addNewContent
  } from 'network/user'
  import ReplyItem from './ReplyItem'
  export default {
    name: 'Reply',
    components: {
      ReplyItem
    },
    data() {
      return {
        replyList: [],
        showCommentsBox: true,
        replyDialogVisible: false,
        replyTitle: '',
        //回复
        replyFrom: {
          pid: null,
          blogid: null,
          head: '',
          content: '',
          uid: null,
          puid: null
        },
        replyRules: {
          content: [{
            required: true,
            message: '请输入评论内容',
            trigger: 'blur'
          }]
        }
      }
    },
    props: {
      info: {
        type: Object,
        default: {}
      },
      showReplyBox: {
        type: Boolean,
        default: false
      }
    },
    watch: {
      showReplyBox() {
        if (this.showReplyBox) {
          this.replyDialogVisible = this.showReplyBox
        }
      }
    },
    mounted() {
      getreply(this.info.blogid, this.info.id, this.info.uid).then(res => {
        if (res.status !== 200) return this.$message.error(res.message);
        this.replyList = res.data.bybloidpuidInfo
      })
    },
    methods: {
      addreplytoArr() {
        this.addreply()
      },
      //撤回评论
      deleteContent(index) {
        this.replyList.splice(index, 1)
      },
      //打开回复框回调
      openReplyBox() {
        this.replyTitle = '@' + this.info.userName
      },
      // 确认回复
      replyConfirm() {
        this.$refs.replyRef.validate((valid) => {
          if (valid) {
            this.addNewContent()
          } else {
            return false;
          }
        });
      },
      addNewContent() {
        this.replyFrom.pid = this.info.id
        this.replyFrom.blogid = this.info.blogid
        this.replyFrom.head = this.info.userName
        this.replyFrom.uid = this.$store.state.myInfo.id
        this.replyFrom.puid = this.info.uid
        addNewContent(this.replyFrom).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            message: res.message,
            type: 'success'
          });
          this.addreply()
          this.replyDialogVisible = false
        })
      },
      addreply() {
        getreply(this.info.blogid, this.info.id, this.info.uid).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.replyList.unshift(res.data.bybloidpuidInfo[0])
        })
      },
      // 关闭回复框
      closedReply() {
        this.$refs.replyRef.resetFields()
        this.$emit('closedReply')
      },
      replyHandleClose(done) {
        done()
      },
      showComments() {
        this.showCommentsBox = false
      }
    }
  }
</script>
<style scoped>
  .reply {
    margin-left: 60px;
  }
</style>