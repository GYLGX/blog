<template>
  <div class="blog-body">

    <!-- 登录注册组件 -->
    <DialogBox :displayLoginBox='displayLoginBox' :displayRegisterBox='displayRegisterBox'
      @closedRegister='closedRegister' @closedlogin='closedlogin'></DialogBox>
    <!-- 抽屉 -->
    <PersonalInformation :drawer='drawer' @closeDrawerBox='closeDrawerBox'></PersonalInformation>


    <!-- 滚动界面 -->
    <el-scrollbar class="blog-body-scrollbar" ref='scrollbar' id=rollbox>
      <div class="blog-head animated fadeInDown">
        <div style="max-width: 1300px;margin: 0 auto;">
          <div style="display: flex;justify-content: space-between;">
            <div class="on-center">
              <img src="~assets/img/guoli.png" alt="">
              <span class="blog-name">Guoli</span>
            </div>
            <div class="on-center" style="width: 400px;">
              <el-input placeholder="请输入内容" v-model="inputText" class="input-with-select">
                <el-button slot="append" icon="el-icon-search" @click='selectBtn'></el-button>
              </el-input>
            </div>
            <div class="on-center">
              <!-- 登录注册模块 -->
              <LoginModule @displayLogin='displayLogin' @displayRegister='displayRegister'
                @openDrawerBox='openDrawerBox'></LoginModule>
            </div>
          </div>
        </div>
      </div>

      <div style="margin: 0 auto;width: 1200px;">
        <div class="animated carousel zoomIn">
          <Carousel></Carousel>
        </div>
        <el-row style="margin-top: 10px;">
          <el-col :span="18">
            <el-card style="margin-right: 5px;" class=" animated fadeInUp">
              <BlogList :tobottom='tobottom' @Refresh='Refresh' :content='content' :tagInfo='tagInfo'></BlogList>
            </el-card>
          </el-col>
          <el-col :span=" 6">
            <el-card class="animated labels fadeInRight">
              <h3 style="color: forestgreen;"> 标签 ：</h3>
              <el-scrollbar style="height: 200px;">
                <el-tag style=" margin: 5px 10px;cursor:pointer;" v-for='item in labels' type="warning"
                  @click='tagClick(item)' :key='item.id'>{{item.groupame}}
                </el-tag>
              </el-scrollbar>
            </el-card>
            <div class="animated show-box slideInUp" v-if='showpeoplewx'>
              <div class="show-people "></div>
              <div style="text-align: center;color: #aaa;">有什么反馈或建议<br>可用添加博主WX</div>
            </div>
            <div class="friendship-chain animated  slideInUp" v-if='showpeoplewx'>
              <h4>友链：</h4>
              <ul style="margin-left: 20px;">
                <li>
                  <a href="https://karasawa.kim" style="color: skyblue"><i class="el-icon-paperclip"></i> 两仪猿的小屋~</a>
                </li>
                <li>
                  <a href="https://distantcjh.top" style="color: darkcyan"><i class="el-icon-paperclip"></i>
                    小陈有好多问号~</a>
                </li>
              </ul>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="blog-bottom">
        <p> GuoLi © {{date}} 不一样的开始 </p>
        <p>作者: guoli 邮箱: 13620366368@163.com</p>
        <p>当前用户量: {{totalNumber.userVolume}}人 、 评论量: {{totalNumber.commentVolume}}条 、 阅读量: {{ReadingVolum}}次</p>
      </div>
      <el-backtop target=".blog-body .el-scrollbar__wrap"></el-backtop>
    </el-scrollbar>
  </div>
</template>
<script>
  import Carousel from 'components/carousel/Carousel'
  import BlogList from 'components/blogInterface/pcTerminal/childComps/BlogList'
  import LoginModule from 'components/blogInterface/loginmodule/LoginModule'
  import DialogBox from 'components/blogInterface/loginmodule/DialogBox'
  import PersonalInformation from 'components/blogInterface/loginmodule/PersonalInformation'

  import {
    getLabes,
    getTotalNumber
  } from 'network/user'
  export default {
    name: 'PCterminal',
    components: {
      Carousel,
      BlogList,
      LoginModule,
      DialogBox,
      PersonalInformation
    },
    data() {
      return {
        showpeoplewx: false,
        date: '',
        inputText: '',
        tobottom: false,
        direction: '', //上：upward  下：downward
        labels: [],
        tagInfo: null,
        content: '',
        // 登录注册
        displayLoginBox: false,
        displayRegisterBox: false,
        //抽屉
        drawer: false,
        totalNumber: {},
        ReadingVolum: 0,
      }
    },
    created() {
      var date = new Date();
      this.date = date.getFullYear();
      this.getTotalNumber()
    },
    mounted() {
      this.scrollFunc()
      this.getLabeList()
    },
    methods: {
      getTotalNumber() {
        getTotalNumber().then(res => {
          this.totalNumber = res.data
          res.data.ReadingList.forEach(item => {
            this.ReadingVolum += item * 1
          });
        })
      },
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
      selectBtn() {
        if (!this.inputText) return
        this.content = this.inputText
        this.inputText = ''
      },
      scrollFunc() {
        let scrollFunc = (e) => {
          e = e || window.event;
          if (e.wheelDelta) { //第一步：先判断浏览器IE，谷歌滑轮事件    
            if (e.wheelDelta > 0) { //当滑轮向上滚动时 
              this.direction = 'upward'
              this.getScrollInfo()
            }
            if (e.wheelDelta < 0) { //当滑轮向下滚动时 
              this.direction = 'downward'
              this.getScrollInfo()
            }
          } else if (e.detail) { //Firefox滑轮事件 
            if (e.detail > 0) { //当滑轮向上滚动时 
              this.direction = 'upward'
              this.getScrollInfo()
            }
            if (e.detail < 0) { //当滑轮向下滚动时 
              this.direction = 'downward'
              this.getScrollInfo()
            }
          }
        }
        //给页面绑定滑轮滚动事件 
        if (document.addEventListener) { //firefox 
          document.addEventListener('DOMMouseScroll', scrollFunc, false);
        }
        //滚动滑轮触发scrollFunc方法 //ie 谷歌 
        window.onmousewheel = document.onmousewheel = scrollFunc;

      },
      getScrollInfo() {
        if (this.$router.currentRoute.path !== '/bloginterface') return
        const scrollInfo = this.$refs.scrollbar.wrap
        let scrollT = scrollInfo.scrollTop;
        let scrollH = scrollInfo.scrollHeight;
        let clientH = scrollInfo.clientHeight;
        if (scrollT > 500) {
          this.showpeoplewx = true
        }
        if (scrollT === 0) {
          this.direction = 'upward'
        } else if (scrollT + 2 > scrollH - clientH && this.direction === 'downward') {
          this.tobottom = true
        }
      },
      getLabeList() {
        getLabes().then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.labels = res.data.Classificati
        })
      },
      tagClick(info) {
        this.tagInfo = info
      },
      Refresh() {
        this.tobottom = false
      },
    }

  }
</script>
<style scoped>
  h4 {
    margin: 0;
  }

  .friendship-chain {
    background-color: #fff;
    border-radius: 5px;
    box-shadow: 0 0 5px 5px #eee;
    padding: 10px;
  }

  .blog-body-scrollbar {
    position: fixed;
    top: 60px;
    left: 0;
    right: 0;
    bottom: 0;
  }

  .show-box {
    margin: 10px 0;
    width: 300px;
    height: 270px;
    background-color: #fff;
    border-radius: 5px;
    box-shadow: 0 0 5px 5px #eee;
    padding-top: 10px;
  }

  .show-people {
    width: 200px;
    height: 200px;
    border-radius: 5px;
    box-shadow: 0 0 5px 5px #eee;
    margin: 10px auto;
    background-image: url('~assets/img/wechat.jpg');
    background-size: 100%;
  }

  .labels {
    width: 298px;
    max-height: 250px;
    /* position: fixed; */
  }

  .blog-bottom {
    text-align: center;
    color: #888;
  }

  .on-center {
    display: flex;
    align-items: center;
    height: 60px;
  }

  .blog-name {
    margin-left: 10px;
    font-size: 40px;
    font-weight: bold;
    color: rgb(74, 75, 9);
  }

  .blog-head {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    height: 60px;
    background-color: #fff;
    box-shadow: 0 -2px 7px #ccc;
    z-index: 100;
  }

  .blog-head img {
    width: 80px;
    height: 60px;
  }

  .carousel {
    margin-top: 5px;
    padding: 10px;
    background-color: #fff;
    box-shadow: 0 -1px 8px #ccc;
  }

  .blog-body {
    height: 100%;
    background-color: rgb(236, 236, 236);
    font-family: 'Courier New', Courier, monospace;
  }

  .el-backtop {
    color: rgb(230, 190, 11) !important;
  }
</style>