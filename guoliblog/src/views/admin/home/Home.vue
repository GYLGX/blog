<template>
  <el-container class='home'>
    <el-header>
      <span>Guoli 博客后台管理</span>
      <span>
        <span>当前登录用户：</span>
        <el-dropdown @command="handleCommand">
          <span class="el-dropdown-link">
            <span>{{username}}</span>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item v-for='i in auth' class="Advanced" icon="el-icon-user">{{i.authority.slice(5)}}
            </el-dropdown-item>
            <el-dropdown-item class="Advanced" icon="el-icon-warning-outline" command="1">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </span>

    </el-header>
    <div class="flex">
      <!-- 导航栏 -->
      <el-aside width="200px">
        <el-scrollbar style="height: 100%;">
          <NavMenu style=" width: 200px" :navnenus='navmenus' />
        </el-scrollbar>
      </el-aside>

      <el-main>
        <el-scrollbar style="height: 100%;">
          <div>
            <router-view />
          </div>
        </el-scrollbar>
      </el-main>

    </div>

  </el-container>
</template>
<script>
  import NavMenu from 'components/navmenu/NavMenu'
  export default {
    name: 'Home',
    components: {
      NavMenu
    },
    data() {
      return {
        auth: [],
        navmenus: [],
        username: ''
      }
    },
    mounted() {
      this.getNavMenus()
    },
    methods: {
      handleCommand(command) {
        if (command === '1') {
          this.$confirm('此操作将退出用户登录 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.$message.success("退出用户成功")
            this.$router.replace('/login')
            window.sessionStorage.clear()
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消操作',
            });
          });
        }
      },
      getNavMenus() {
        let loginer = JSON.parse(window.sessionStorage.getItem('loginer'))
        this.auth = loginer.auth
        this.username = loginer.username
        var menus = loginer.menus
        let Arr = []
        menus.forEach(item1 => {
          if (!item1.pid) {
            Arr.push(item1)
            item1.Children = []
          }
        })
        Arr.forEach(item2 => {
          menus.forEach(item3 => {
            if (item2.frontendMenuId === item3.pid) {
              item2.Children.push(item3)
            }
          })
        })
        this.navmenus = Arr
      }
    },
  }
</script>
<style scoped>
  .home {
    height: 100%;
  }

  .el-header {
    background-color: rgb(8, 30, 54);
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .el-header span {
    color: #fff;
    font-size: 25px;
    font-family: monospace
  }

  .flex {
    display: flex;
    height: 100%;
    overflow-y: hidden;
  }


  .el-main {
    background-color: #eee;
    padding: 5px;
  }


  .el-aside {
    background-color: rgb(49, 55, 67);
  }

  .Advanced:hover {
    background-color: rgba(255, 217, 0, 0.096);
    color: #FFD700;
  }
</style>