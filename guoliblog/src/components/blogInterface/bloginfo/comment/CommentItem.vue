<template>
  <div class='comment-item'>

    <div class="content-box">
      <div>
        <img class="head-portrait" v-lazy="item.url" alt="">
      </div>
      <el-tag v-if='item.uid !== 121202013' type="warning" effect="plain">{{item.userName}}</el-tag>
      <el-tag v-else type="info">{{item.userName}}博主</el-tag>
    </div>

    <div class="content-text">
      <span class="text-content">{{item.content}}</span>
      <span class="block-box-right">
        <el-button v-if='item.uid !== $store.state.myInfo.id' type="primary" size="mini" plain class="clickbutton"
          style="margin:0 10px;" @click='replyClick'>回复</el-button>
        <el-button v-else type="warning" size="mini" plain style="margin:0 10px;" class="clickbutton"
          @click='withdrawClick'>撤回
        </el-button>
      </span>
    </div>

    <div class="comment-time" style="text-align: right; margin-right: 10px;">
      <i class="el-icon-time" style="color: #ccc;"> : {{item.commentTime}}</i>
    </div>


    <Reply :info='item' :replyData='replyData' :showReplyBox='showReplyBox' @closedReply='closedReply'> </Reply>

  </div>
</template>
<script>
  import {
    withdrawContent
  } from 'network/user'

  import Reply from './Reply'
  export default {
    name: 'CommentItem',
    components: {
      Reply
    },
    data() {
      return {
        //回复数据
        replyData: {
          uid: null,
          id: null,
          blogid: null,
          userName: '',
          // index: null
        },
        // 显示回复框
        showReplyBox: false
      }
    },
    props: {
      item: {
        type: Object,
        default: {}
      },
      index: {
        type: Number,
        default: null
      }
    },
    methods: {

      //关闭回复框回调
      closedReply() {
        this.showReplyBox = false
      },
      //回复
      replyClick() {
        if (!window.sessionStorage.getItem('token')) return this.$message.warning('先登录，后评论！');
        this.replyData.uid = this.item.uid
        this.replyData.id = this.item.id
        this.replyData.blogid = this.item.blogid
        this.replyData.userName = this.item.userName
        // this.replyData.index = this.index
        this.showReplyBox = true

      },
      //撤回
      withdrawClick() {
        withdrawContent(this.item.id).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            message: res.message,
            type: 'warning'
          });
          this.$emit('deleteContent', this.index)
        })
      },
    }
  }
</script>
<style scoped>
  .comment-item {
    box-shadow: 1px 1px 3px #ccc;
    padding-bottom: 10px;
  }

  .head-portrait {
    width: 40px !important;
    height: 40px;
    border-radius: 50%;
    margin: 10px;
    box-shadow: 0 0 8px 5px #ccc;
  }

  .content-text {
    color: #999;
  }

  .content-box {
    display: flex;
    align-items: center;
    ;
  }

  /* PC端 */
  @media only screen and (min-width: 1029px) {
    .comment-time {
      font-size: 14px
    }



    .content-text {
      font-size: 14px;
      margin-left: 10px;
    }
  }

  /* 移动端 */
  @media only screen and (min-width: 300px) and (max-width: 1024px) {
    .comment-time {
      font-size: 3vw;
    }

    .clickbutton {
      padding: 1.3vw;
      font-size: 2.9vw;
    }

    .block-box-right {
      display: block;
      text-align: right;
      margin-right: 2vw;
    }

    .text-content {
      display: block;
      font-size: 4vw;
      color: #aaa;
      margin: 0 1px;
      text-indent: 2em;
    }
  }
</style>