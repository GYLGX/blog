<template>
  <div style="height: 100%;">
    <!-- PC端 -->
    <PCterminal class="pc-terminal"></PCterminal>
    <!-- 移动端 -->
    <MobileEnd class="mobile-end"></MobileEnd>
  </div>
</template>
<script>
  import PCterminal from 'components/blogInterface/pcTerminal/PCterminal'
  import MobileEnd from 'components/blogInterface/mobileEnd/MobileEnd'

  export default {
    name: 'BlogInterface',
    components: {
      PCterminal,
      MobileEnd
    },
    data() {
      return {}
    }
  }
</script>
<style scoped>
  .pc-terminal {
    display: none;
  }

  .mobile-end {
    display: none;
  }


  /* PC端 */
  @media only screen and (min-width: 1029px) {
    .pc-terminal {
      display: block !important;
    }
  }

  /* 移动端 */
  @media only screen and (min-width: 300px) and (max-width: 1024px) {
    .mobile-end {
      display: block !important;
    }
  }
</style>