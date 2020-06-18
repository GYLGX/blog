<template>
  <mavon-editor v-model="handbook" class="editbox" placeholder='输入内容 。。。' ref="md" @imgAdd="$imgAdd" />
</template>
<script>
  import {
    mavonEditor
  } from 'mavon-editor'
  import 'mavon-editor/dist/css/index.css'


  import {
    imgadd,
  } from 'network/admin'
  import axios from 'axios'
  export default {
    name: 'MavonEditor',
    components: {
      mavonEditor
    },
    props: {
      content: ''
    },
    data() {
      return {
        handbook: ''
      }
    },
    watch: {
      content() {
        this.handbook = this.content
      }
    },
    methods: {
      // 绑定@imgAdd event
      $imgAdd(pos, $file) {
        // 第一步.将图片上传到服务器.
        var formdata = new FormData();
        formdata.append('file', $file);
        imgadd(formdata).then(res => {
          if (res.status !== 200) return this.$message.error('上传图片失败');
          let $vm = this.$refs.md;
          $vm.$img2Url(pos, res.data.url);
        })
      }
    },
  }
</script>
<style scoped>
  .editbox {
    min-height: 650px;
  }
</style>