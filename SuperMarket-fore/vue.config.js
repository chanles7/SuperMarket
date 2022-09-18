const path = require('path');
function resolve(dir) {
  return path.join(__dirname, dir);
}

module.exports = {
  devServer: {
    host: '0.0.0.0',
    disableHostCheck: true,
    port: 12000,
    proxy: {
      '/api': {
        target: 'http://onljmail.com/back',
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          '^/api': '',
        },
      },
    },
  },
  publicPath: '/',
  outputDir: 'dist',
  assetsDir: './assets',
  lintOnSave: false,
  productionSourceMap: false,

  chainWebpack: (config) => {
    config.resolve.alias.set('@', resolve('src'));
  },
};
