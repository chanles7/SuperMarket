export default {
  data() {
    return {};
  },
  methods: {
    showMessage(res) {
      if (res.code == 200) {
        this.$message({
          title: '成功',
          message: res.msg,
          type: 'success',
        });
      } else {
        this.$message.error({
          title: '失败',
          message: res.msg,
        });
      }
    },
  },
};
