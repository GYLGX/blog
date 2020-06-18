<template>
  <el-card class="botton-card">
    <el-input placeholder="请输入title内容" v-model="blog.title" class="input-with-select">
      <el-select v-model="blog.lid" slot="prepend" placeholder="请选择标签" clearable>
        <el-option v-for="item in Labels" :key="item.id" :label="item.groupame" :value="item.id">
        </el-option>
      </el-select>
    </el-input>

    <el-row>
      <el-col :span="20">
        <el-input class="imgurl" placeholder="此处为放图片url 或 者上传本地图片" v-model='blog.imgUrl'></el-input>
      </el-col>
      <el-col :span="4">
        <el-upload class="upload-demo" action="http://localhost:8881/blogoss/fileoss" :headers='token'
          :show-file-list='false' :on-change="handleChange">
          <el-button type="primary">上传本地图片</el-button>
        </el-upload>
      </el-col>
    </el-row>

    <div style="margin-bottom: 10px;text-align: center;">
      <el-button type="primary" plain @click='judge'>{{btnText}}</el-button>
      <el-button type="info" plain @click='cancel'>取消</el-button>
    </div>


    <!-- 编辑框 -->
    <MavonEditor :content='blog.content' ref='mavoneditor' v-highlight></MavonEditor>
    <el-row>
      <el-col :span="8" :offset="8">
        <div class="alter-button">
          <el-button type="primary" plain @click='judge'>{{btnText}}</el-button>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="cancel-button">
          <el-button type="info" plain @click='cancel'>取消</el-button>
        </div>
      </el-col>
    </el-row>

  </el-card>
</template>
<script>
  import {
    addBlog,
    updateBlog,
    getLabeList,
    getByIdBlog,
  } from 'network/admin'
  import MavonEditor from 'components/editbox/MavonEditor'

  export default {
    name: 'EditArticle',
    components: {
      MavonEditor
    },
    data() {
      return {
        token: {
          Authorization: sessionStorage.getItem('admintoken')
        },
        Labels: [],
        blog: {
          id: null,
          title: '',
          content: '',
          imgUrl: '',
          lid: null
        },
        blogId: this.$route.query,
        btnText: ''
      }
    },
    created() {
      this.getLabeList()
      if (this.$route.query.blogId) {
        this.btnText = '确认修改'
        this.blog.id = this.$route.query.blogId
        this.getByIdBlog()
      } else {
        this.btnText = '发布新博客'
      }
    },
    methods: {
      handleChange(file, fileList) {
        if (file.response) {
          if (file.response.status !== 200) return this.$message.error(file.response.message);
          this.$message({
            message: file.response.message,
            type: 'success'
          });
          this.blog.imgUrl = file.response.data.url
        }
      },
      //取消按钮
      cancel() {
        this.$router.back()
      },
      //根据id获取博客
      getByIdBlog() {
        getByIdBlog(this.blog.id).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          let blogInfo = res.data.blog
          this.blog.title = blogInfo.title
          this.blog.content = blogInfo.content
          this.blog.imgUrl = blogInfo.imgUrl
          this.blog.lid = blogInfo.lid
        })
      },

      //判断方法
      judge() {
        let blog = Object.assign({}, this.blog)
        blog.content = this.$refs.mavoneditor.handbook
        if (blog.title !== '' && blog.content !== '' && blog.imgUrl !== '' && blog.lid !== null) {
          if (blog.lid === '') return this.$message('您好！对不起！ 请填写完整！才能帮您发布');
          if (blog.id) {
            this.Updateblog(blog)
          } else {
            this.Issueblog(blog)
          }
        } else {
          this.$message('您好！对不起！ 请填写完整！才能帮您发布');
        }
      },
      //发布新博客
      Issueblog(blog) {
        addBlog(blog).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            type: 'success',
            message: res.message
          });
          this.$router.back()
        })
      },
      //修改博客
      Updateblog(blog) {
        updateBlog(blog).then(res => {
          if (res.status !== 200) return this.$message.error(res.message)
          this.$message({
            message: `您已经修改了【${blog.title}】该条博客`,
            type: 'success'
          });
          this.$router.back()
        })
      },
      //获取全部分类标签
      getLabeList() {
        getLabeList().then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.Labels = res.data.Classificati
        })
      }
    },
  }
</script>
<style scoped>
  .el-select {
    width: 130px;
  }

  .imgurl {
    margin: 10px 0;
  }

  .alter-button {
    margin-top: 20px;
    text-align: center;
  }

  .cancel-button {
    margin-top: 20px;
    text-align: right;
  }

  .upload-demo {
    text-align: center;
    line-height: 60px;
  }
</style>