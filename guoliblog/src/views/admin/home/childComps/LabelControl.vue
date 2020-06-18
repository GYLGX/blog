<template>
  <el-card>
    <el-row>
      <el-col :span="12">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item>blog管理</el-breadcrumb-item>
          <el-breadcrumb-item>标签管理</el-breadcrumb-item>
        </el-breadcrumb>
      </el-col>
      <el-col :span="12">
        <el-button size="mini" type="warning" plain icon="el-icon-circle-plus-outline" @click="addLabeBtn ">添加新的标签
        </el-button>
        <el-input style="margin-left: 20px;" class=" input-new-tag" v-if="inputVisible" v-model="inputValue"
          ref="saveTagInput" size="small" @keyup.enter.native="addInputConfirm" placeholder="请输入标签名">
        </el-input>
      </el-col>
    </el-row>

    <el-table :data="LabeList" stripe style="width: 100%">
      <el-table-column type="expand">
        <template slot-scope="scope">
          <ul style="width: 100%;">
            <li style="width: 100%;" v-for='item in scope.row.info.blog'>
              <tr style="width: 100%;">
                <td style="width:300px">{{item.title}}</td>
                <td style="width: 200px;">{{item.createTime}}</td>
              </tr>
            </li>
          </ul>
        </template>
      </el-table-column>
      <el-table-column type="index" label="#" width="50">
      </el-table-column>
      <el-table-column label="标签名称">
        <template slot-scope="scope">
          <el-tag :key='scope.row.id' v-if="scope.row.inputVisible" :disable-transitions='true'
            @dblclick="handleEdit(scope.row)">
            {{scope.row.groupame}}</el-tag>
          <el-input class="input-new-tag" v-else="!scope.row.inputVisible" v-model="scope.row.inputValue"
            ref="saveTagInput" size="small" @keyup.enter.native="handleInputConfirm(scope.row)"></el-input>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini" icon="el-icon-edit" type="primary" @click="handleEdit(scope.row)">修改
          </el-button>
          <el-button size="mini" icon="el-icon-delete" type="danger" @click='handleDelete(scope.row)'>删除</el-button>
        </template>
      </el-table-column>
    </el-table>


  </el-card>
</template>
<script>
  import {
    getLabeBlogList,
    getLabeList,
    addLabe,
    updateLabe,
    deleteLabe
  } from 'network/admin'
  export default {
    name: 'LabelControl',
    components: {},
    data() {
      return {
        LabeList: [],
        inputVisible: false,
        inputValue: ''
      }
    },
    created() {
      this.getLabeList()
    },
    methods: {
      addInputConfirm() {
        this.inputVisible = false
        if (this.inputValue !== '') {
          let Obj = {}
          Obj.groupame = this.inputValue
          addLabe(Obj).then(res => {
            if (res.status !== 200) return this.$message.error(res.message);
            this.$message({
              type: 'success',
              message: res.message
            });
            this.inputValue = ''
            this.getLabeList()
          })
        }
      },
      handleInputConfirm(row) {
        row.inputVisible = true
        let info = {}
        info.id = row.id
        info.groupame = row.inputValue
        updateLabe(info).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            type: 'success',
            message: res.message
          });
          row.groupame = row.inputValue
        })
      },
      addLabeBtn() {
        this.inputVisible = true
      },
      handleEdit(row) {
        row.inputVisible = false
        row.inputValue = row.groupame
      },
      handleDelete(row) {
        this.$confirm(`此操作将永久删除【${row.groupame}】标签, 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteLabe(row.id).then(res => {
            if (res.status !== 200) return this.$message.error(res.message);
            this.$message({
              type: 'warning',
              message: res.message
            });
            this.getLabeList()
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      getLabeList() {
        getLabeList().then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          res.data.Classificati.forEach(item => {
            item.info = []
            item.inputVisible = true
            item.inputValue = ''
            getLabeBlogList(item.id).then(res => {
              item.info = res.data
            })
          });
          this.LabeList = res.data.Classificati
        })
      }
    }
  }
</script>
<style scoped>
  .el-tag {
    width: 150px;
  }

  .input-new-tag {
    width: 150px;
  }
</style>