<template>
  <div class='bloginfo'>

    <!-- 登录注册组件 -->
    <DialogBox :displayLoginBox='displayLoginBox' :displayRegisterBox='displayRegisterBox'
      @closedRegister='closedRegister' @closedlogin='closedlogin'></DialogBox>
    <!-- 抽屉 -->
    <PersonalInformation :drawer='drawer' @closeDrawerBox='closeDrawerBox'></PersonalInformation>

    <div class="bloginfo-head">
      <div style="max-width: 1300px;margin: 0 auto;">
        <el-row>
          <el-col :span="12" class="on-center">
            <img src="~assets/img/guoli.png" alt="">
            <span class="blog-name">Guoli</span>
          </el-col>
          <el-col :span="12" class="on-center">
            <!-- 登录注册模块 -->
            <LoginModule @displayLogin='displayLogin' @displayRegister='displayRegister' @openDrawerBox='openDrawerBox'>
            </LoginModule>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 滚动界面 -->
    <el-scrollbar class="bloginfo-scrollbar">
      <BlogContent :bloginfo='bloginfo'></BlogContent>
      <el-backtop target=".bloginfo .el-scrollbar__wrap"></el-backtop>
    </el-scrollbar>
  </div>
</template>
<script>
  import {
    getReleaseBlog
  } from 'network/user'
  import BlogContent from 'components/blogInterface/bloginfo/BlogContent'
  import LoginModule from 'components/blogInterface/loginmodule/LoginModule'
  import DialogBox from 'components/blogInterface/loginmodule/DialogBox'
  import PersonalInformation from 'components/blogInterface/loginmodule/PersonalInformation'
  export default {
    name: 'BlogInfo',
    components: {
      DialogBox,
      PersonalInformation,
      BlogContent,
      LoginModule
    },
    data() {
      return {
        bloginfo: {},
        blogid: this.$route.query.id,

        // 登录注册
        displayLoginBox: false,
        displayRegisterBox: false,
        //抽屉
        drawer: false
      }
    },
    mounted() {
      if (this.blogid) {
        this.getReleaseBlog(this.blogid)
      }
    },
    methods: {
      closeDrawerBox() {
        this.drawer = false
      },
      openDrawerBox() {
        this.drawer = true
      },
      closedlogin() {
        this.displayLoginBox = false
      },
      closedRegister() {
        this.displayRegisterBox = false
      },
      displayLogin() {
        this.displayLoginBox = true
      },
      displayRegister() {
        this.displayRegisterBox = true
      },
      getReleaseBlog(id) {
        getReleaseBlog(id).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.bloginfo = res.data.blog
        })
      }
    }
  }
</script>
<style scoped>
  .blog-name {
    margin-left: 10px;
    font-size: 40px;
    font-weight: bold;
    color: rgb(74, 75, 9);
  }

  .bloginfo-scrollbar {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
  }

  .bloginfo {
    height: 100%;
    width: 100%;
    font-family: 'Courier New', Courier, monospace;
  }

  .bloginfo-head {
    position: fixed;
    background-color: #fff;
    top: 0;
    left: 0;
    right: 0;
    z-index: 100;
    box-shadow: 0 0 10px #ccc;
  }


  .on-center {
    display: flex;
    align-items: center;
  }

  /* PC端 */
  @media only screen and (min-width: 1029px) {
    .bloginfo-head {
      height: 60px !important;
    }

    .bloginfo-head img {
      width: 80px;
      height: 60px;
    }

    .on-center {
      height: 60px;
    }

    .bloginfo-scrollbar {
      top: 60px;

    }

    .el-backtop {
      opacity: 0.6;
    }
  }

  /* 移动端 */
  @media only screen and (min-width: 300px) and (max-width: 1024px) {
    .bloginfo-head {
      height: 11vw !important;
    }

    .bloginfo-head img {
      width: 15vw;
      height: 11vw;
    }

    .blog-name {
      display: none;
    }

    .on-center {
      height: 11vw;
    }

    .bloginfo-scrollbar {
      top: 11vw;

    }

    .el-backtop {
      right: 10px !important;
      opacity: 0.6;
    }
  }
</style>