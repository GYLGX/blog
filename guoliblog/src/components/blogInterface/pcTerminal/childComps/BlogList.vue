<template>
  <div>
    <div style="display: flex;">
      <h3 style="margin-right: 100px; color: rgb(0, 167, 0);">{{listHead}}
        <i v-if='refresh.info.content' class="el-icon-refresh" style="color: #999;" ref='refreshInco'
          @click='refreshBtn'></i>
      </h3>
      <div style="display: flex;align-items: center;" v-if='tagName'>
        <h3 style="color: rgb(224, 149, 9);margin-right: 10px;">查询标签:</h3>
        <el-tag closable type="warning" @close="handleClose" style="cursor:pointer;">
          {{tagName}}
        </el-tag>
      </div>
    </div>

    <center>
      <h2 style="color: #ccc;" v-if='blogList.length === 0'>暂无相关的博客。。。</h2>
    </center>
    <div class='blog-list'>
      <BlogItem v-for='(item,index) in blogList' :key='index' :bloginfo='item'></BlogItem>
    </div>
    <center>
      <el-button class="load-button" type="info" plain @click='loadBtn'>
        <i v-show='loadIcon' class="el-icon-loading"></i>
        {{loadText}}
      </el-button>
    </center>
  </div>
</template>
<script>
  import {
    getReleaseBlogList
  } from 'network/user'
  import BlogItem from 'components/blogInterface/pcTerminal/childComps/BlogItem'
  export default {
    name: 'BlogList',
    components: {
      BlogItem
    },
    data() {
      return {
        listHead: '已发布的博客 ：',
        listLength: 1,
        loadIcon: false,
        loadText: '加载更多',
        refresh: {
          current: 1,
          size: 6,
          info: {
            lid: null,
            content: '',
            createTime: ''
          }
        },
        blogList: [],
        tagName: ''
      }
    },
    props: {
      content: {
        type: String,
        default: ''
      },
      tobottom: {
        type: Boolean,
        default: false
      },
      tagInfo: {
        type: Object,
        default: {}
      }
    },
    watch: {
      content() {
        this.refresh.current = 1
        this.listLength = 1
        this.loadIcon = false
        this.loadText = '加载更多'
        this.refresh.info.content = this.content
        this.listHead = `正在搜索有关【${this.content}】的博客`
        this.getBlogList()
      },
      tagInfo() {
        this.tagName = this.tagInfo.groupame
        this.refresh.info.lid = this.tagInfo.id
        this.refresh.current = 1
        this.listLength = 1
        this.loadIcon = false
        this.loadText = '加载更多'
        this.getBlogList()
      },
      tobottom() {
        if (this.tobottom) {
          if (this.refresh.current > 1) return
          this.loadIcon = true
          this.loadText = '拼命加载中。。。'
          this.loadData()
        }
      }
    },
    created() {
      this.getBlogList()
    },
    methods: {
      refreshBtn() {
        this.reloadData()
      },
      handleClose() {
        this.refresh.current = 1
        this.tagName = ''
        this.refresh.info.lid = null
        this.listLength = 1
        this.loadIcon = false
        this.loadText = '加载更多'
        this.getBlogList()
      },
      reloadData() {
        this.refresh.current = 1
        this.tagName = ''
        this.refresh.info.lid = null
        this.refresh.info.content = ''
        this.refresh.info.createTime = ''
        this.listHead = `已发布的博客 ：`
        this.getBlogList()
      },
      loadBtn() {
        if (this.loadText === '重新加载') {
          this.reloadData()
          return
        }
        if (this.listLength === 0) return
        this.loadIcon = true
        this.loadText = '拼命加载中。。。'
        this.loadData()
      },
      loadData() {
        this.refresh.current += 1
        getReleaseBlogList(this.refresh).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.listLength = res.data.rows.length
          if (res.data.rows.length !== 0) {
            return new Promise((resolve, reject) => {
              resolve(this.blogList.push(...res.data.rows))
            }).then(res => {
              this.$emit('Refresh')
              this.loadIcon = false
              this.loadText = '加载更多'
            })
          } else {
            this.loadIcon = false
            this.loadText = '已经没有更多了'
          }
        })
      },
      getBlogList() {
        getReleaseBlogList(this.refresh).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.blogList = res.data.rows
          this.loadText = '加载更多'
          if (this.blogList.length === 0) {
            this.listLength = 1
            this.loadIcon = false
            this.loadText = '重新加载'
          }
        })
      }
    }
  }
</script>
<style scoped>
  .load-button {
    width: 70%;
  }

  .blog-list {
    display: flex;
    flex-wrap: wrap;
  }
</style>