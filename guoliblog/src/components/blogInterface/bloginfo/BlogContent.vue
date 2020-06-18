<template>
  <div class='blog-content'>
    <el-card>
      <div class="user-info">
        <h2>{{bloginfo.title}}</h2>
        <span class="block-box" style="margin-right: 20px; color: #aaa;"><i class="el-icon-s-custom" /> Guoli</span>

        <span class="block-box" style="margin-right: 20px; color: #ccc;"><span style="color: green;"><i
              class="el-icon-alarm-clock" />
            发布时间 :
          </span>{{bloginfo.createTime}}</span>

        <span class="block-box" v-if='bloginfo.upateTime' style="color: #ccc;">
          <span style="color: orange;">
            <i class="el-icon-alarm-clock" /> 最近更新时间 :
          </span> {{bloginfo.upateTime}}
        </span>

      </div>
      <div class="blog-text" v-html='text' v-highlight></div>
    </el-card>
    <Comment :blogid='bloginfo.id'></Comment>
  </div>
</template>
<script>
  import marked from 'marked'
  import Comment from './comment/Comment'
  export default {
    name: 'BlogContent',
    components: {
      Comment
    },
    data() {
      return {
        text: ''
      }
    },
    props: {
      bloginfo: {
        type: Object,
        default: {}
      },
    },
    watch: {
      bloginfo() {
        this.text = marked(this.bloginfo.content)
      }
    }
  }
</script>
<style scoped>
  /* PC端 */
  @media only screen and (min-width: 1029px) {
    .block-box {
      display: inline-block;
    }
  }

  /* 移动端 */
  @media only screen and (min-width: 320px) and (max-width: 1024px) {
    .block-box {
      display: block;
      margin: 3px 0;
    }
  }

  .blog-content {
    max-width: 1200px;
    margin: 5px auto;
    font-family: PingFang SC, Verdana, Helvetica Neue, Microsoft Yahei, Hiragino Sans GB, Microsoft Sans Serif, WenQuanYi Micro Hei, sans-serif;
  }

  .user-info {
    padding-bottom: 10px;
    padding-left: 20px;
    margin-bottom: 10px;
    box-shadow: 0 2px 1px #ccc;
  }

  .user-info h2 {
    margin: 10px;
    font-size: 1.6em;
  }

  .user-info span {
    font-size: 0.75em;
  }

  .el-card {
    margin-bottom: 10px;
  }
</style>