<template>
  <div class='mobile-end'>
    <MobileHead @byLabeId='byLabeId'></MobileHead>
    <BlogList :labeId='labeId' class="animated fadeInUp"></BlogList>
  </div>
</template>
<script>
  import MobileHead from './head/MobileHead'
  import BlogList from './list/BlogList'

  export default {
    name: 'MobileEnd',
    components: {
      MobileHead,
      BlogList,
    },
    data() {
      return {
        labeId: 0
      }
    },
    methods: {
      byLabeId(id) {
        this.labeId = id
      }
    }
  }
</script>
<style scoped>
  .mobile-end {
    height: 100%;
    font-family: '-apple-system', "Helvetica Neue", "Roboto", "Segoe UI", sans-serif;
    background-color: rgb(250, 250, 250);
  }
</style>