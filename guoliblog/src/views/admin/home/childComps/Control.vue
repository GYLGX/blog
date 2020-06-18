<template>
  <el-card>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>驾驶舱</el-breadcrumb-item>
      <el-breadcrumb-item>方向盘</el-breadcrumb-item>
    </el-breadcrumb>
    <h2>首页轮播图 ：</h2>
    <center style="border-top: 1px solid #eee;">
      <Carousel style="width: 1400px; margin-top: 20px;" ref='carousel'></Carousel>
    </center>
    <center style="margin: 10px auto;border-top: 1px solid #eee;">
      <h4>控制走马灯图片数据</h4>
      <el-table :data="carouselList" stripe style="width: 500px" border>
        <el-table-column type="index" label="#" width="50">
        </el-table-column>
        <el-table-column label="图片">
          <template slot-scope="scope">
            <el-popover placement="top" width="500">
              <img class="maxImg" v-lazy="scope.row.url" alt="">
              <img slot="reference" class="minImg" v-lazy="scope.row.url" alt="">
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" icon="el-icon-delete" type="danger" @click='handleDelete(scope.row)'>删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </center>

    <el-card style="margin-top: 30px;">
      <div class=" browse">
        <div>
          <i class="el-icon-s-platform published display "></i>
          <div class="data-frame">
            <h4 class="text">未发布博客数</h4>
            <div class="nunber"><span style="color: orangered;">{{BlogData.unpublishedSize}}</span> 篇</div>
          </div>
        </div>
        <div>
          <i class="el-icon-s-platform unpublished display"></i>
          <div class="data-frame">
            <h4 class="text">已发布博客数</h4>
            <div class="nunber"><span style="color: rgb(60, 255, 0);">{{BlogData.releaseSize}}</span> 篇</div>
          </div>
        </div>
        <div>
          <i class="el-icon-s-platform total display"></i>
          <div class="data-frame">
            <h4 class="text">总博客数</h4>
            <div class="nunber"><span style="color: rgb(78, 75, 75);">{{BlogData.totalSize}}</span> 篇</div>
          </div>
        </div>
      </div>
    </el-card>

  </el-card>
</template>
<script>
  import {
    getBlogData,
    getCarouselImgList,
    deleteCarouselImg
  } from 'network/admin'
  import Carousel from 'components/carousel/Carousel'
  export default {
    name: 'Control',
    components: {
      Carousel
    },
    data() {
      return {
        carouselList: [],
        BlogData: {}
      }
    },
    created() {
      this.getCarouselImgList()
      this.getBlogData()
    },
    methods: {
      getBlogData() {
        getBlogData().then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.BlogData = res.data.BlogData
        })
      },
      handleDelete(row) {
        deleteCarouselImg(row.id).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            type: 'warning',
            message: res.message
          });
          this.getCarouselImgList()
          this.$refs.carousel.getCarouselImgList()
        })
      },
      getCarouselImgList() {
        getCarouselImgList().then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.carouselList = res.data.list
        })
      }
    }
  }
</script>
<style scoped>
  .text {
    margin-top: 20px;
    font-size: 20px;
    color: #555;
  }

  .nunber {
    margin-top: 60px;
    font-size: 40px;
  }

  .data-frame {
    width: 320px;
    height: 320px;
    margin-top: -320px;
    text-align: center;
  }

  .browse {
    display: flex;
    justify-content: space-evenly;
  }

  .display {
    display: block;
    font-size: 300px;
    border-radius: 10px;
    border: 10px solid #eee;
    box-shadow: 10px 0 20px #ccc;
  }

  .total {
    background: linear-gradient(rgb(255, 60, 0), rgb(243, 171, 62));
    -webkit-background-clip: text;
    color: transparent;
  }

  .unpublished {
    background: linear-gradient(rgb(0, 119, 255), rgb(0, 255, 221));
    -webkit-background-clip: text;
    color: transparent;
  }

  .published {
    background: linear-gradient(rgb(156, 156, 156), rgb(63, 63, 62));
    -webkit-background-clip: text;
    color: transparent;
  }
</style>