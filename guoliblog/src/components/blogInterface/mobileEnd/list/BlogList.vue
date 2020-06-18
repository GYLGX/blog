<template>
  <PullRefresh class="pull-refresh" v-model="isLoading" @refresh="onRefresh" id='rollDiv'>
    <div class="input-box">
      <input type="text" class="input-text" v-model="refresh.info.content" placeholder="需要查询什么可以在此输入...">
      <i class="el-icon-search search-icon" @click='selectClick'></i>
    </div>
    <!-- 轮播图 -->
    <Swipe :autoplay="3000" class="animated fadeInRight">
      <SwipeItem v-for="(image, index) in swipeImages" :key="index">
        <a :href="'/bloginfo?id='+image.id">
          <img class="swipe-image" v-lazy="image.url" />
        </a>
      </SwipeItem>
    </Swipe>
    <!-- item -->
    <div v-if='blogList.length > 0'>
      <BlogItem v-for='info in blogList' :info='info'></BlogItem>
    </div>
    <div v-else style="text-align: center;color: #ccc;">
      <h2>暂无此类数据。。。</h2>
    </div>
    <Pagination v-if='blogList.length > 0' v-model="refresh.current" :total-items="total" :show-page-size="3"
      :items-per-page='refresh.size' @change='loadData'>
    </Pagination>
  </PullRefresh>
</template>
<script>
  import {
    getCarouselImgList
  } from 'network/admin'
  import {
    getReleaseBlogList
  } from 'network/user'
  import {
    PullRefresh,
    Toast,
    Field,
    Swipe,
    SwipeItem,
    Pagination
  } from 'vant'
  import BlogItem from './BlogItem'
  export default {
    name: 'BlogList',
    components: {
      BlogItem,
      PullRefresh,
      Field,
      Swipe,
      SwipeItem,
      Pagination
    },
    data() {
      return {
        text: '',
        isLoading: false,
        swipeImages: [],
        blogList: [],
        refresh: {
          current: 1,
          size: 6,
          info: {
            lid: null,
            content: '',
            createTime: ''
          }
        },
        total: 0
      }
    },
    props: {
      labeId: {
        type: Number,
        default: 0
      }
    },
    watch: {
      labeId() {
        this.refresh.info.lid = this.labeId
        this.getReleaseBlogList()
      }
    },
    created() {
      this.getCarouselImgList()
      this.getReleaseBlogList()
    },
    methods: {
      selectClick() {
        this.getReleaseBlogList()
      },
      loadData() {
        const rollDiv = document.getElementById('rollDiv');
        rollDiv.scrollTop = 0;
        this.getReleaseBlogList()
      },
      getCarouselImgList() {
        getCarouselImgList().then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.swipeImages = res.data.list
        })
      },
      getReleaseBlogList() {
        getReleaseBlogList(this.refresh).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.blogList = res.data.rows
          this.total = res.data.total

        })
      },
      onRefresh() {
        this.refresh.info.lid = null
        this.refresh.info.content = ''
        this.refresh.info.createTime = ''
        getReleaseBlogList(this.refresh).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.blogList = res.data.rows
          this.total = res.data.total
          Toast('刷新成功');
          this.isLoading = false;
        })
      },
    }
  }
</script>
<style scoped>
  .pull-refresh {
    position: fixed;
    top: 11.3vw;
    left: 0;
    right: 0;
    bottom: 0;
    overflow: auto;
  }

  .input-box {
    background-color: #eee;
    padding: 1vw;
    display: flex;
    align-items: center;
    border-radius: 4vw;
    margin-top: 0.4vw;
  }

  .input-text {
    flex: 1;
    border: none;
    margin: 0 2vw;
    background: none;
    padding-left: 2vw;
  }

  .search-icon {
    font-weight: bold;
    margin-right: 2vw;
    color: #fff;
  }

  .swipe-image {
    width: 100%;
    height: 60vw;
  }
</style>