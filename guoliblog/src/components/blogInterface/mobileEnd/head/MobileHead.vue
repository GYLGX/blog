<template>
  <div>
    <div class="head animated fadeInDown">
      <div style="display: flex;justify-content: space-between;">
        <div class="on-center">
          <img class="blog-img" src="~assets/img/guoli.png" alt="">
          <span class="blog-name">Guoli</span>
        </div>
        <div class="on-center">
          <Icon name="bars" size="26" color="#aaaaaa" @click="showPopup" />
        </div>
      </div>
    </div>
    <Popup v-model="show" position="right" style="z-index: 999;" :style="{ width: '37vw' , height: '100%'}">
      <Tabs>
        <Tab title="分类">
          <Sidebar v-model="activeKey" style="width: 100%;">
            <SidebarItem title='全部' @click='labeClick(0)'></SidebarItem>
            <SidebarItem :title="item.groupame" v-for='(item,index) in labels' :key='index' @click='labeClick(item.id)'>
            </SidebarItem>
          </Sidebar>
        </Tab>
        <Tab title="友链">
          <Tag class="tag-htef" plain type="success" size="large">
            <a href="https://karasawa.kim"><i class="el-icon-paperclip"></i> 两仪猿的小屋~</a>
          </Tag>
          <Tag class="tag-htef" plain type="primary" size="large"><i class="el-icon-paperclip"></i> 小陈有好多问号~</a>
          </Tag>
        </Tab>
      </Tabs>
    </Popup>
  </div>
</template>
<script>
  import {
    getLabes
  } from 'network/user'
  import {
    Icon,
    Popup,
    Sidebar,
    SidebarItem,
    Tab,
    Tabs,
    Tag
  } from 'vant'
  export default {
    name: 'MobileHead',
    components: {
      Icon,
      Popup,
      Sidebar,
      SidebarItem,
      Tab,
      Tabs,
      Tag
    },
    data() {
      return {
        show: false,
        labels: [],
        activeKey: 0
      }
    },
    created() {
      this.getLabes()
    },
    methods: {
      labeClick(id) {
        this.$emit('byLabeId', id)
        this.show = false;
      },
      showPopup() {
        this.show = true;
      },
      getLabes() {
        getLabes().then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.labels = res.data.Classificati
        })
      }
    }
  }
</script>
<style scoped>
  .labe-item {
    font-size: 4.3vw;
    color: #888;
    margin: 2vw;
    padding-left: 1vw;
    padding-bottom: 1vw;
    border-bottom: 1px solid #ccc;
  }

  .head {
    max-width: 1300px;
    margin: 0 auto;
    background-color: #fff;
    box-shadow: 0 2px 10px #eee;
  }

  .blog-img {
    width: 17vw;
    height: 11vw;
  }

  .on-center {
    display: flex;
    align-items: center;
  }

  .blog-name {
    font-size: 6vw;
    color: rgb(74, 75, 9);
  }

  .tag-htef {
    display: block;
    margin: 3vw 1vw;
  }
</style>