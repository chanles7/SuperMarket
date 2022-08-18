<template>
  <div>
    <el-tree :data=categotyTree :props="defaultProps" :expand-on-click-node="false" node-key="catId" show-checkbox>
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button type="text" size="mini" @click="() => append(data)" v-show="data.catLevel != 3">
            添加
          </el-button>
          <el-button type="text" size="mini" @click="() => edit(data)">
            编辑
          </el-button>
          <el-button type="text" size="mini" @click="() => remove(node, data)" v-show="data.catLevel == 3">
            删除
          </el-button>
        </span>
      </span>
    </el-tree>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="30%" :close-on-click-modal='false'>
      <el-form :model="category">
        <el-form-item label="分类名称" label-width="100px">
          <el-input v-model="category.name" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submit">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
export default {
  data() {
    return {
      categotyTree: [],
      defaultProps: {
        children: "categoryEntityVOs",
        label: "name",
      },

      //对话框相关
      dialogVisible: false,
      dialogTitle: "",
      category: {
        name: "",
        catId: null,
        catLevel: 0,
        parentCid: 0,
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
    // 新增
    append(data) {
      this.dialogTitle = "添加";
      let category = this.category;
      category.parentCid = data.catId;
      category.catLevel = data.catLevel + 1;
      category.name = "";
      this.dialogVisible = true;
    },
    // 编辑
    edit(data) {
      this.dialogTitle = "编辑";
      let category = this.category;
      category.catId = data.catId;
      category.name = data.name;
      this.dialogVisible = true;
    },
    // 确定
    submit() {
      this.$http({
        url: this.$http.adornUrl(
          this.category.catId
            ? "/product/category/update"
            : "/product/category/add"
        ),
        method: "post",
        data: this.$http.adornData(this.category, false),
      }).then(() => {
        this.dialogVisible = false;
      });
    },
    // 删除
    remove(node, data) {
      this.$confirm(`确定要删除商品分类【${node.label}】吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let catId = [data.catId];
          this.$http({
            url: this.$http.adornUrl("/product/category/delete"),
            method: "post",
            data: this.$http.adornData(catId, false),
          }).then((res) => {
            this.$message.success("1");
          });
        })
        .catch(() => {});
    },
  },
  created() {
    this.getCategoryTree();
  },
};
</script>

<style>
</style>