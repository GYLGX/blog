<template>
  <el-card>
    <el-row>
      <el-col :span="12">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item>系统管理</el-breadcrumb-item>
          <el-breadcrumb-item>前端菜单管理</el-breadcrumb-item>
        </el-breadcrumb>
      </el-col>
      <el-col :span="12">
        <el-button size="mini" type="warning" plain icon="el-icon-circle-plus-outline" @click="addMemuBtn ">添加菜单
        </el-button>
      </el-col>
    </el-row>

    <tree-table :data='menuList' :columns='columns' show-index index-text='#' :selection-type='false'
      :expand-type='false' :is-fold='false' stripe border>
      <template slot="level" slot-scope="scope">
        <el-tag :type="scope.row.pid ? 'warning' : ''">{{scope.row.pid ? '二级' : '一级'}}</el-tag>
      </template>
      <template slot="operation " slot-scope="scope">
        <div class="float">
          <span style="margin-right: 10px;" v-if='hideBtn(scope.row.children)'>
            <el-button type="primary" icon="el-icon-edit" size="mini" @click='compileBtn(scope.row)'>编辑</el-button>
            <el-button type="danger" icon="el-icon-delete" size="mini" @click='deleteBtn(scope.row)'>删除</el-button>
          </span>
          <el-button v-if='!scope.row.pid' type="warning" icon="el-icon-circle-plus-outline" size="mini"
            @click='addSonBtn(scope.row)'>添加子菜单
          </el-button>
        </div>
      </template>
    </tree-table>

    <!-- 对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync=" dialogVisible" width="30%" :before-close="handleClose"
      @close='dialogClose'>

      <el-form :model="menuInfoFrom" :rules="menuInfoFromRules" ref="menuInfoFromRef" label-width="100px">
        <el-form-item label="父级菜单" v-show="dialogTitle !== '添加新菜单' ">
          <el-input v-model="menuInfoFrom.parentName" disabled></el-input>
        </el-form-item>
        <el-form-item label="菜单名称" prop="frontendMenuName">
          <el-input v-model="menuInfoFrom.frontendMenuName"></el-input>
        </el-form-item>
        <el-form-item label="路由跳转" prop="frontendMenuUrl" v-if="submitUrl">
          <el-input v-model="menuInfoFrom.frontendMenuUrl"></el-input>
        </el-form-item>
        <el-form-item label="序号" prop="frontendMenuSort">
          <el-input v-model="menuInfoFrom.frontendMenuSort" type='number'></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="menuInfoFrom.description"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmBtn">确 定</el-button>
      </span>
    </el-dialog>


  </el-card>
</template>
<script>
  import {
    getMenuList,
    addMenu,
    updateMenu,
    deleteMenu
  } from 'network/admin'
  export default {
    name: 'Menu',
    data() {
      return {
        submitUrl: false,
        menuInfoFromRules: {
          frontendMenuName: [{
              required: true,
              message: '请输入菜单名称',
              trigger: 'blur'
            },
            {
              min: 2,
              max: 12,
              message: '长度在 2 到 12 个字符',
              trigger: 'blur'
            }
          ],
          frontendMenuUrl: [{
              required: true,
              message: '请输入路由跳转路径',
              trigger: 'blur'
            },
            {
              min: 3,
              max: 22,
              message: '长度在 3 到 22 个字符',
              trigger: 'blur'
            }
          ],
          frontendMenuSort: [{
              required: true,
              message: '请输入序号',
              trigger: 'blur'
            },
            {
              min: 2,
              max: 6,
              message: '长度在 2 到 6 个字符',
              trigger: 'blur'
            }
          ]
        },
        menuInfoFrom: {
          frontendMenuId: '',
          parentName: '',
          pid: '',
          frontendMenuName: '',
          frontendMenuUrl: '',
          frontendMenuSort: '',
          description: ''
        },
        dialogVisible: false,
        dialogTitle: '',
        menuList: [],
        columns: [{
            label: '父级菜单',
            prop: 'parentName'
          }, {
            label: '菜单名称',
            prop: 'frontendMenuName'
          }, {
            label: '路由跳转路径',
            prop: 'frontendMenuUrl'
          }, {
            label: '序号',
            prop: 'frontendMenuSort'
          }, {
            label: '备注',
            prop: 'description'
          }, {
            label: '级别',
            type: 'template',
            template: 'level'
          },
          {
            label: '操作',
            type: 'template',
            template: 'operation ',
            width: 300
          }
        ]
      }
    },
    created() {
      this.getMenuList()
    },
    computed: {
      hideBtn() {
        return function (data) {
          if (data) {
            return data.length === 0
          } else {
            return true
          }
        }
      }
    },
    methods: {
      confirmBtn() {
        this.$refs.menuInfoFromRef.validate((valid) => {
          if (valid) {
            if (this.dialogTitle === '修改菜单') {
              this.updateMenu()
            } else {
              this.addMenu()
            }
          } else {
            return false;
          }
        });

      },
      updateMenu() {
        updateMenu(this.menuInfoFrom).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            type: 'success',
            message: res.message
          });
          this.dialogVisible = false
          this.getMenuList()
        })
      },
      addMenu() {
        addMenu(this.menuInfoFrom).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            type: 'success',
            message: res.message
          });
          this.dialogVisible = false
          this.getMenuList()

        })
      },
      dialogClose() {
        this.submitUrl = false
        this.$refs.menuInfoFromRef.resetFields()
        this.menuInfoFrom.frontendMenuId = ''
        this.menuInfoFrom.parentName = ''
        this.menuInfoFrom.pid = ''
        this.menuInfoFrom.frontendMenuName = ''
        this.menuInfoFrom.frontendMenuUrl = ''
        this.menuInfoFrom.frontendMenuSort = ''
        this.menuInfoFrom.description = ''
      },
      handleClose(done) {
        done()
      },
      addMemuBtn() {
        this.dialogTitle = '添加新菜单'
        this.dialogVisible = true
      },
      compileBtn(row) {
        this.menuInfoFrom.frontendMenuId = row.frontendMenuId
        this.menuInfoFrom.parentName = row.parentName
        this.menuInfoFrom.pid = row.pid
        this.menuInfoFrom.frontendMenuName = row.frontendMenuName
        this.menuInfoFrom.frontendMenuUrl = row.frontendMenuUrl
        this.menuInfoFrom.frontendMenuSort = row.frontendMenuSort + ''
        this.menuInfoFrom.description = row.description
        if (row.frontendMenuUrl) {
          this.submitUrl = true
        }
        this.dialogTitle = '修改菜单'
        this.dialogVisible = true
      },
      deleteBtn(row) {
        this.$confirm(`此操作将永久删除【${row.frontendMenuName}】菜单, 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteMenu(row.frontendMenuId)

        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      deleteMenu(id) {
        deleteMenu(id).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            type: 'success',
            message: res.message
          });
          this.dialogVisible = false
          this.getMenuList()
        })
      },
      addSonBtn(row) {
        this.submitUrl = true
        this.menuInfoFrom.parentName = row.frontendMenuName
        this.menuInfoFrom.pid = row.frontendMenuId
        this.dialogTitle = '添加新的子菜单'
        this.dialogVisible = true
      },
      getMenuList() {
        getMenuList().then(res => {
          let list = res.data.list
          let Arr = []
          list.forEach(item1 => {
            if (!item1.pid) {
              item1.children = []
              Arr.push(item1)
            }
          })
          Arr.forEach(item2 => {
            list.forEach(item3 => {
              if (item2.frontendMenuId === item3.pid) {
                item2.children.push(item3)
              }
            })
          })
          this.menuList = Arr
        })
      }
    }
  }
</script>
<style scoped>
  .float {
    float: left;
  }

  .el-row {
    margin-bottom: 10px;
  }
</style>