<template>
  <div class='blog-item animated bounceIn'>
    <a :href="'/bloginfo?id='+info.id">
      <img :src="info.imgUrl" alt="">
    </a>
    <div style="margin-top: 5px;margin-left: 10px;">
      <div class="bloginfo-title">
        <a class="border-a" :href="'/bloginfo?id='+info.id">{{info.title}}</a>
      </div>
      <div style="color: #ccc; margin-bottom: 10px;"><i class="el-icon-s-custom" /> Guoli</div>
      <div style="color: #aaa;font-size: 12px;font-family:Verdana, Geneva, Tahoma, sans-serif;">
        <span style="margin-right: 20px;">阅读量:{{info.readingVolume}} 次</span>
        <span>评论量:{{info.commentVolume}} 条</span>
      </div>
    </div>
  </div>
</template>
<script>
  export default {
    name: 'BlogItem',
    components: {},
    props: {
      info: {
        type: Object,
        default: {}
      }
    }
  }
</script>
<style scoped>
  .blog-item {
    width: 95%;
    padding: 2vw 0;
    text-align: center;
    margin: 2vw auto;
    box-shadow: 1px 1px 5px #ccc;
  }

  .blog-item img {
    width: 95%;
    border-radius: 2vw;
    box-shadow: 0 0 10px #ccc;
  }

  .border-a {
    color: #888;
    text-decoration: none;
    border-bottom: 1px solid #888;
  }

  .border-a:hover {
    color: rgb(230, 190, 11);
    border-bottom: 1px solid rgb(230, 190, 11);
  }

  .bloginfo-title {
    width: 95%;
    margin: 10px 0;
    line-height: 150%;
  }

  .show {
    position: relative;
    top: -270px;
    left: 0px;
    height: 210px;
    width: 250px;
  }

  .show-img {
    height: 100%;
    width: 100%;
  }

  .blog-item-img {
    width: 250px;
    height: 200px;
    background-color: #fff;
    border-radius: 5px;
    box-shadow: 0 0 5px 1px #ccc;
    overflow: hidden;
  }

  .show-button {
    position: relative;
    top: -130px;
    left: 85px;
  }

  .blog-item-img .blog-img {
    width: 100%;
    height: 100%;
  }

  .shadow {
    width: 250px;
    height: 30px;
    position: relative;
    top: -35px;
  }

  .create-time {
    overflow: hidden;
    text-overflow: ellipsis;
    position: relative;
    top: -65px;
    left: 10px;
    color: rgb(243, 227, 194);
  }
</style>