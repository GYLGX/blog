<template>
  <div class="blog-item animated fadeInUp">
    <div class='blog-item-img' @mouseover='mouseover(bloginfo.id)' @mouseout='mouseout(bloginfo.id)'>
      <img class="blog-img" v-lazy="bloginfo.imgUrl" alt="">
      <img class="shadow" src="~/assets/img/shadow.png" alt="">
      <h3 class="create-time">{{setDate}}</h3>
      <div :id='bloginfo.id' class="show animated" v-show='hidden'>
        <img class="show-img" src="~/assets/img/show.png" alt="">
        <el-button size="mini" class="show-button" type="warning" round @click='toinfo(bloginfo.id)'>点击阅读</el-button>
      </div>
    </div>
    <div style="margin-top: 5px;margin-left: 10px;">
      <div class="bloginfo-title">
        <a :href="'/bloginfo?id='+bloginfo.id">{{bloginfo.title}}</a>
      </div>
      <div style="color: #ccc; margin-bottom: 10px;"><i class="el-icon-s-custom" /> Guoli</div>
      <div style="color: #aaa;font-size: 12px;font-family:Verdana, Geneva, Tahoma, sans-serif;">
        <span style="margin-right: 20px;">阅读量:{{bloginfo.readingVolume}} 次</span>
        <span>评论量:{{bloginfo.commentVolume}} 条</span>
      </div>
    </div>
  </div>
</template>
<script>
  export default {
    name: 'BlogItem',
    data() {
      return {
        hidden: false
      }
    },
    props: {
      bloginfo: {}
    },
    computed: {
      setDate() {
        return this.bloginfo.createTime.substring(0, 10)
      }
    },
    methods: {
      mouseover(id) {
        this.hidden = true
        this.ControlHover('fadeOutDown', 'fadeInUp', id)
      },
      mouseout(id) {
        this.ControlHover('fadeInUp', 'fadeOutDown', id)

      },
      ControlHover(animate1, animate2, id) {
        const hover = document.getElementById(`${id}`)
        if (hover) {
          hover.classList.remove(animate1)
          hover.classList.add(animate2)
        }
      },
      toinfo(id) {
        this.$router.push({
          path: '/bloginfo',
          query: {
            id: id
          }
        })
      }
    }
  }
</script>
<style scoped>
  a {
    color: #888;
    text-decoration: none;
    border-bottom: 1px solid #888;
  }

  a:hover {
    color: rgb(230, 190, 11);
    border-bottom: 1px solid rgb(230, 190, 11);
  }

  .bloginfo-title {
    width: 95%;
    margin: 10px 0;
    line-height: 150%;
  }

  .blog-item {
    width: 250px;
    padding: 10px;
    margin: 5px;
    box-shadow: 1px 2px 5px #ccc;
    margin-bottom: 15px;
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