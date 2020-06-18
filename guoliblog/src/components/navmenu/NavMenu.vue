<template>
  <el-menu class="navmenu" :default-active="activePath" background-color="#333744" text-color="#fff"
    active-text-color="#FFD700" router>
    <el-submenu :index="items.frontendMenuId" v-for='items in navnenus'>
      <template slot="title">
        <i style="color:rgba(241, 231, 187, 0.548);" :class="icons[items.frontendMenuName]"></i>
        <span style="color:rgba(248, 232, 159, 0.548);">{{items.frontendMenuName}}</span>
      </template>
      <el-menu-item-group>
        <el-menu-item :index="item.frontendMenuUrl" v-for='item in items.Children'
          @click='saveNavState(item.frontendMenuUrl)'>
          <template slot="title">
            <i :class="icons[item.frontendMenuName]"></i>
            <span>{{item.frontendMenuName}}</span>
          </template>
        </el-menu-item>
      </el-menu-item-group>
    </el-submenu>
  </el-menu>
</template>
<script>
  export default {
    name: 'NavMenu',
    data() {
      return {
        activePath: '',
        icons: {
          '驾驶舱': 'el-icon-odometer',
          '方向盘': 'el-icon-loading',
          'blog管理': 'el-icon-set-up',
          '内容管理': 'el-icon-tickets',
          '标签管理': 'el-icon-paperclip',
          '评论管理': 'el-icon-edit',
          '评论用户管理': 'el-icon-user-solid',
          '评论内容管理': 'el-icon-edit-outline',
          '系统管理': 'el-icon-cpu',
          '用户管理': 'el-icon-s-custom',
          '角色管理': 'el-icon-user',
          '前端菜单管理': 'el-icon-notebook-2',
          '后端API管理': 'el-icon-magic-stick'
        }
      }
    },
    props: {
      navnenus: {
        type: Array,
        default: []
      }
    },
    created() {
      if (window.sessionStorage.getItem('activePath')) {
        this.activePath = window.sessionStorage.getItem('activePath')
      } else {
        this.activePath = '/home/control'
      }
    },
    methods: {
      saveNavState(activePath) {
        window.sessionStorage.setItem('activePath', activePath)
        this.activePath = activePath
      }
    }
  }
</script>
<style scoped>
  .el-menu {
    margin-bottom: 30px;
  }
</style>