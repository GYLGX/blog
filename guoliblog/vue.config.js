module.exports = {
  configureWebpack: {
    resolve: {
      alias: {
        // '@': 'src', //默认已经配置了, 无需配置
        'assets': '@/assets',
        'common': '@/common',
        'components': '@/components',
        'network': '@/network',
        'views': '@/views',
      }
    }
  },
  publicPath: '/'
}