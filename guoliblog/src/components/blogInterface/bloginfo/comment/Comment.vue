<template>
  <el-card>
    <div style="display: flex;justify-content: space-between;margin-bottom: 10px;">
      <div>
        <i class="el-icon-edit-outline"></i> 评论 ：
      </div>
      <el-button type="primary" icon="el-icon-edit" size="mini" plain @click='addCommentClick'>评论</el-button>
    </div>

    <div v-if='contentList.length === 0' style="font-weight: bold; color: #ccc;font-size: 20px;text-align: center;">
      暂无评论 。 。 。
    </div>
    <!-- 添加评论 -->
    <div v-if='contentDialogVisible' style="margin: 10px 0;">

      <el-form :model="contentFrom" :rules="contentRules" ref="contentRef">
        <el-form-item prop="content">
          <el-input type="textarea" :rows="2" placeholder="请输入评论内容" v-model="contentFrom.content">
          </el-input>
        </el-form-item>
      </el-form>

      <div style="text-align: right;margin: 10px 0;">
        <el-button @click="cancelClick">取 消</el-button>
        <el-button type="primary" @click="confirmClick">确 定</el-button>
      </div>
    </div>

    <div v-if='contentList.length !== 0'>
      <!-- 评论item -->
      <CommentItem v-for='(item,index) in contentList' :index='index' :item='item'
        @deleteContent='deleteContent(index)'>
      </CommentItem>

    </div>
  </el-card>
</template>
<script>
  import {
    getcomment,
    addNewContent,
    withdrawContent
  } from 'network/user'
  import CommentItem from './CommentItem'
  export default {
    name: 'Comment',
    components: {
      CommentItem
    },
    data() {
      var validateText = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入评论内容'));
        } else {
          callback();
        }
      };
      return {
        contentList: [],
        contentDialogVisible: false,
        contentFrom: {
          blogid: null,
          content: '',
          uid: null,
        },
        contentRules: {
          content: [{
            validator: validateText,
            trigger: 'blur'
          }]
        }
      }
    },
    props: {
      blogid: {
        type: Number,
        default: null
      }
    },
    watch: {
      blogid() {
        getcomment(this.blogid).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.contentList = res.data.bybloidInfo
        })
      }
    },
    methods: {
      //撤回评论
      deleteContent(index) {
        this.contentList.splice(index, 1)
      },
      // 添加评论
      addContent() {
        if (!window.sessionStorage.getItem('token')) return this.$message.warning('先登录，后评论！');
        this.contentFrom.blogid = this.blogid
        this.contentFrom.uid = this.$store.state.myInfo.id
        let contentInfo = this.contentFrom
        addNewContent(contentInfo).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            message: res.message,
            type: 'success'
          });
          this.getNewComment()
          this.contentFrom.content = ''
        })
      },
      getNewComment() {
        getcomment(this.blogid).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.contentList.unshift(res.data.bybloidInfo[0])
        })
      },
      confirmClick() {
        this.$refs.contentRef.validate((valid) => {
          if (valid) {
            this.addContent()
          } else {
            return false;
          }
        });
      },
      cancelClick() {
        this.contentDialogVisible = false
        this.contentFrom.content = ''
      },
      addCommentClick() {
        if (!window.sessionStorage.getItem('token')) return this.$message.warning('先登录，后评论！');
        this.contentDialogVisible = true
      }
    }
  }
</script>
<style scoped>
  .head-portrait {
    width: 40px !important;
    height: 40px;
    border-radius: 50%;
    margin: 10px;
    box-shadow: 0 0 8px 5px #ccc;
  }

  .content-text {
    margin-top: 15px;
    min-height: 80px;
    font-size: 18px;
    color: #999;
  }
</style>