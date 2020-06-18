<template>
  <div class='reply-item'>
    <div class="reply-box">
      <div>
        <img class="head-portrait" v-lazy="info.url" alt="">
      </div>
      <el-tag v-if='info.uid !== 121202013' type="warning" effect="plain">{{info.userName}}</el-tag>
      <el-tag v-else type="info">{{info.userName}}博主</el-tag>
    </div>

    <el-tag style="margin-left: 10px;" type="success" :type="info.uid === 121202013 ? 'warning':'info'" size="small">@
      {{info.head}}{{ info.uid === 121202013 ? '':'博主'}}:</el-tag>
    <span class="text-content block-box"> {{info.content}}</span>

    <span class="block-box-right">
      <el-button v-if='info.uid !== $store.state.myInfo.id' type="primary" size="mini" plain class="clickbutton"
        @click='replyClick'>回复</el-button>
      <el-button v-else type="warning" size="mini" plain class="clickbutton" @click='withdrawClick'>撤回
      </el-button>
    </span>


    <div class="comment-time">
      <i class=" el-icon-time" style="color: #ccc;"> : {{info.commentTime}}</i>
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
    withdrawContent,
    addNewContent
  } from 'network/user'
  export default {
    name: 'ReplyItem',
    data() {
      return {
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
      index: {
        type: Number,
        default: null
      }
    },
    methods: {
      replyClick() {
        if (!window.sessionStorage.getItem('token')) return this.$message.warning('先登录，后评论！');
        this.replyDialogVisible = true
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
        if (!window.sessionStorage.getItem('token')) return this.$message.warning('先登录，后评论！');
        this.replyFrom.pid = this.info.pid
        this.replyFrom.blogid = this.info.blogid
        this.replyFrom.head = this.info.userName
        this.replyFrom.uid = this.$store.state.myInfo.id
        this.replyFrom.puid = this.info.puid
        addNewContent(this.replyFrom).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            message: res.message,
            type: 'success'
          });
          this.$emit('addreplytoArr')
          this.replyDialogVisible = false
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
      withdrawClick() {
        withdrawContent(this.info.id).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            message: res.message,
            type: 'warning'
          });
          this.$emit('deleteContent', this.index)
        })
      }
    }
  }
</script>
<style scoped>
  .reply-item {
    box-shadow: 2px 2px 3px #ccc;
    margin: 10px;
    padding: 5px;
    padding-bottom: 10px;
  }

  .head-portrait {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    margin: 10px;
    box-shadow: 0 0 8px 5px #ccc;
  }

  .reply-box {
    display: flex;
    align-items: center;
  }


  /* PC端 */
  @media only screen and (min-width: 1029px) {
    .text-content {
      font-size: 15px;
      color: #aaa;
      margin-right: 10px;
    }


    .comment-time {
      margin-right: 10px;
      text-align: right;
    }
  }

  /* 移动端 */
  @media only screen and (min-width: 300px) and (max-width: 1024px) {

    .text-content {
      display: block;
      font-size: 3.3vw;
      color: #aaa;
      margin: 0 1px;
      text-indent: 2em;
    }

    .comment-time {
      margin-top: 1vw;
      margin-right: 1vw;
      font-size: 3.2vw;
      text-align: right;
    }

    .clickbutton {
      padding: 1vw;
      font-size: 2.6vw;
    }

    .block-box-right {
      display: block;
      text-align: right;
      margin-right: 2vw;
    }

    .reply-item {
      padding-bottom: 1.5vw;
    }

  }
</style>