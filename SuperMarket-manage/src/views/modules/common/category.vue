<template>
  <el-tree :data=categotyTree :props="defaultProps" :expand-on-click-node="false" node-key="catId" @node-click="nodeClick">
    <span class="custom-tree-node" slot-scope="{ node, data }">
      <span>{{ node.label }}</span>
    </span>
  </el-tree>
</template>

<script>
export default {
  data() {
    return {
      categotyTree: [],
      defaultProps: {
        children: "children",
        label: "name",
      },
    };
  },
  methods: {
    // 获取树形结构
    getCategoryTree() {
      this.$http({
        url: this.$http.adornUrl("/product/category/list/tree"),
        method: "get",
        params: this.$http.adornParams(),
      }).then(({ data }) => {
        this.categotyTree = data.data;
      });
    },
    nodeClick(data, node, component) {
      this.$emit("tree-node-click", data, node, component);
    },
  },
  created() {
    this.getCategoryTree();
  },
};
</script>

<style>
</style>