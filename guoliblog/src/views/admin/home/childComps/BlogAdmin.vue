<template>
  <el-card>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>blog管理</el-breadcrumb-item>
      <el-breadcrumb-item>内容管理</el-breadcrumb-item>
    </el-breadcrumb>
    <el-row style="text-align: center; margin: 10px;">
      <el-col :span="8">
        <el-input placeholder="查询标题"" v-model=" queryCriteria.condition.title" class="input-with-select" clearable>
          <el-button slot="append" icon="el-icon-search" @click='selectBtn'></el-button>
        </el-input>
      </el-col>
      <el-col :span="8">
        <el-select v-model="queryCriteria.condition.lid" placeholder="请选择" clearable @change='selectChange'>
          <el-option v-for="item in labels" :key="item.id" :label="item.groupame" :value="item.id">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="5">
        <el-button type="warning" plain icon="el-icon-circle-plus-outline" @click="addBlogBtn ">添加新的博客
        </el-button>
      </el-col>
      <el-col :span="3">
        <div class="refresh-icon">
          <i @click='iconClick' :class="icon"></i>
        </div>
      </el-col>
    </el-row>

    <el-table :data="blogList" stripe style="width: 100%">
      <el-table-column prop="title" label="标题" width="200">
      </el-table-column>
      <el-table-column prop="createTime" label="添加时间" width="230">
      </el-table-column>
      <el-table-column prop="upateTime" label="修改时间" width="230">
      </el-table-column>
      <el-table-column prop="imgUrl" label="首页图片">
        <template slot-scope="scope">
          <el-popover placement="top" width="500">
            <img class="maxImg" v-lazy="scope.row.imgUrl" alt="">
            <img slot="reference" class="minImg" v-lazy="scope.row.imgUrl" alt="">
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="是否发布">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enabled" active-color="#FFD700" inactive-color="#ccc"
            @change='changeSwitch(scope.row)'>
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="430">
        <template slot-scope="scope">
          <el-button size="mini" icon="el-icon-edit" type="primary" @click="handleEdit(scope.row)">修改</el-button>
          <el-button size="mini" icon="el-icon-delete" type="danger" @click='handleDelete(scope.row)'>删除</el-button>
          <el-button size="mini" icon="el-icon-circle-plus-outline" type="warning" @click="addCarousel(scope.row)">
            将该博客添加到走马灯列表里
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <center>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="queryCriteria.current" :page-sizes="[5,10,15,20]" :page-size='queryCriteria.size'
        layout="total, sizes, prev, pager, next, jumper" :total='total'>
      </el-pagination>
    </center>

  </el-card>
</template>
<script>
  import {
    getBlogList,
    updateBlogState,
    deleteBlog,
    getLabeList,
    addCarouselImg
  } from 'network/admin'
  export default {
    name: 'BlogAdmin',
    components: {},
    data() {
      return {
        queryCriteria: {
          current: 1,
          size: 5,
          condition: {
            lid: null,
            title: '',
            createTime: ''
          }
        },
        blogList: [],
        total: null,
        labels: [],
        icon: 'el-icon-refresh'
      }
    },
    created() {
      const obj = JSON.parse(window.sessionStorage.getItem("queryCriteria"))
      if (obj) {
        this.queryCriteria = obj
      }
      this.getBlogList()
      this.getLabeList()
    },
    destroyed() {
      window.sessionStorage.setItem("queryCriteria", JSON.stringify(this.queryCriteria))
    },
    methods: {
      addCarousel(row) {
        let Obj = {}
        Obj.blogid = row.id
        Obj.url = row.imgUrl
        addCarouselImg(Obj).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            type: 'success',
            message: res.message
          });
        })
      },
      iconClick() {
        this.icon = 'el-icon-loading'
        this.queryCriteria.current = 1
        this.queryCriteria.size = 5
        this.queryCriteria.condition.lid = null
        this.queryCriteria.condition.title = ''
        this.queryCriteria.condition.createTime = ''
        getBlogList(this.queryCriteria).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.total = res.data.total
          this.blogList = res.data.rows
          this.icon = 'el-icon-refresh'
          this.$message({
            type: 'success',
            message: '刷新最新数据'
          });
        })
      },
      selectBtn() {
        if (this.queryCriteria.condition.title) {
          this.getBlogList()
        }
      },
      selectChange() {
        this.getBlogList()
      },
      changeSwitch(row) {
        let type = ''
        if (row.enabled) {
          type = 'success'
        } else {
          type = 'warning'
        }
        updateBlogState(row).then(res => {
          if (res.status !== 200) {
            row.enabled = !row.enabled
            this.$message.error(res.message);
          } else {
            this.$message({
              type: type,
              message: res.message
            });
            this.getBlogList()
          }
        })
      },
      handleSizeChange(val) {
        this.queryCriteria.size = val
        this.getBlogList()
      },
      handleCurrentChange(val) {
        if (val) {
          this.queryCriteria.current = val
          this.getBlogList()
        }
      },
      getBlogList() {
        getBlogList(this.queryCriteria).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.total = res.data.total
          this.blogList = res.data.rows
        })
      },
      getLabeList() {
        getLabeList().then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.labels = res.data.Classificati
        })
      },
      addBlogBtn() {
        this.$router.push('/home/editArticle')
      },
      handleEdit(row) {
        let Obj = {}
        Obj.blogId = row.id
        this.$router.push({
          path: '/home/editArticle',
          query: Obj
        })
      },
      handleDelete(row) {
        this.$confirm(`此操作将永久删除【${row.title}】, 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteBlog(row.id).then(res => {
            if (res.status !== 200) return this.$message.error(res.message);
            this.$message({
              type: 'success',
              message: res.message
            });
            this.getBlogList()
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });

      },
    },
  }
</script>
<style scoped>
  .refresh-icon {
    text-align: right;
    font-size: 25px;
  }
</style>