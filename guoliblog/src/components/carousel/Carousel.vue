<template>
  <el-carousel :interval="4000" type="card" height="350px">
    <el-carousel-item v-for="item in list" :key="item.id">
      <img class="carousel-img" v-lazy="item.url" alt="" @click='imgClick(item.blogid)'>
    </el-carousel-item>
  </el-carousel>
</template>
<script>
  import {
    getCarouselImgList
  } from 'network/admin'
  export default {
    name: 'Carousel',

    data() {
      return {
        list: []
      }
    },
    created() {
      this.getCarouselImgList()
    },
    methods: {
      getCarouselImgList() {
        getCarouselImgList().then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.list = res.data.list
        })
      },
      imgClick(blogid) {
        this.$router.push({
          path: '/bloginfo',
          query: {
            id: blogid
          }
        })

      }
    }
  }
</script>
<style scoped>
  .carousel-img {
    width: 100%;
    height: 100%;
  }
</style>