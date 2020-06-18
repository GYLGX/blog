<template>
  <el-card>
    <el-row>
      <el-col :span="12">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item>系统管理</el-breadcrumb-item>
          <el-breadcrumb-item>后端API管理</el-breadcrumb-item>
        </el-breadcrumb>
      </el-col>
      <el-col :span="12">
        <el-button size="mini" type="warning" plain icon="el-icon-circle-plus-outline" @click="addApiBtn ">添加新的API
        </el-button>
      </el-col>
    </el-row>
    <tree-table :data='apiList' :columns='columns' show-index index-text='#' :selection-type='false'
      :expand-type='false' :is-fold='false' stripe border>
      <template slot="level" slot-scope="scope">
        <el-tag :type="scope.row.pid ? 'warning' : ''">{{scope.row.pid ? '二级' : '一级'}}</el-tag>
      </template>

      <template slot="operation " slot-scope="scope">
        <div class="float" v-if='scope.row.backendApiSort !== 100'>
          <el-button type="primary" icon="el-icon-edit" size="mini" @click='compileBtn(scope.row)'>编辑</el-button>
          <span style="margin: 10px;" v-if='hideBtn(scope.row.children)'>
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

      <el-form :model="apiInfoFrom" :rules="apiInfoFromRules" ref="apiInfoFromRef" label-width="100px">
        <el-form-item label="父级" v-show="dialogTitle !== '添加新的API' ">
          <el-input v-model="apiInfoFrom.parentName" disabled></el-input>
        </el-form-item>
        <el-form-item label="API名称" prop="backendApiName">
          <el-input v-model="apiInfoFrom.backendApiName"></el-input>
        </el-form-item>
        <el-form-item label="请求路劲" prop="backendApiUrl" v-if="submitUrl">
          <el-input v-model="apiInfoFrom.backendApiUrl"></el-input>
        </el-form-item>
        <el-form-item label="提交方式">
          <el-checkbox-group v-model="apiInfoFrom.backendApiMethod">
            <el-checkbox label="GET" name="GET"></el-checkbox>
            <el-checkbox label="POST" name="POST"></el-checkbox>
            <el-checkbox label="PUT" name="PUT"></el-checkbox>
            <el-checkbox label="DELETE" name="DELETE"></el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="序号" prop="backendApiSort">
          <el-input v-model="apiInfoFrom.backendApiSort" type='number'></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="apiInfoFrom.description"></el-input>
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
    getApiList,
    addApi,
    updateApi,
    deleteApi
  } from 'network/admin'
  export default {
    name: 'Api',
    data() {
      return {
        submitUrl: false,
        apiInfoFrom: {
          backendApiId: '',
          parentName: '',
          pid: '',
          backendApiName: '',
          backendApiUrl: '',
          backendApiMethod: ['GET'],
          backendApiSort: '',
          description: ''
        },
        apiInfoFromRules: {
          backendApiName: [{
              required: true,
              message: '请输入API的名称',
              trigger: 'blur'
            },
            {
              min: 2,
              max: 12,
              message: '长度在 2 到 12 个字符',
              trigger: 'blur'
            }
          ],
          backendApiUrl: [{
              required: true,
              message: '请输入路由请求路径',
              trigger: 'blur'
            },
            {
              min: 3,
              max: 35,
              message: '长度在 3 到 30 个字符',
              trigger: 'blur'
            }
          ],
          backendApiSort: [{
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
        dialogVisible: false,
        dialogTitle: '',
        columns: [{
            label: '父级',
            prop: 'parentName',
            width: 150
          }, {
            label: 'API名称',
            prop: 'backendApiName',
            width: 130
          }, {
            label: '请求地址',
            prop: 'backendApiUrl',
            width: 200
          }, , {
            label: '请求方式',
            prop: 'backendApiMethod',
            width: 170
          }, {
            label: '序号',
            prop: 'backendApiSort',
            width: 80
          }, {
            label: '备注',
            prop: 'description'
          }, {
            label: '级别',
            type: 'template',
            template: 'level',
            width: 90
          },
          {
            label: '操作',
            type: 'template',
            template: 'operation ',
            width: 300
          }
        ],
        apiList: []
      }
    },
    created() {
      this.getApiList()
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
        this.$refs.apiInfoFromRef.validate((valid) => {
          if (valid) {
            if (this.dialogTitle === '修改API信息') {
              this.updateApi()
            } else {
              this.addApi()
            }
          } else {
            return false;
          }
        });
      },
      updateApi() {
        this.apiInfoFrom.backendApiMethod = this.apiInfoFrom.backendApiMethod.join(',')
        updateApi(this.apiInfoFrom).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            type: 'success',
            message: res.message
          });
          this.dialogVisible = false
          this.getApiList()
        })
      },
      addApi() {
        this.apiInfoFrom.backendApiMethod = this.apiInfoFrom.backendApiMethod.join(',')
        addApi(this.apiInfoFrom).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            type: 'success',
            message: res.message
          });
          this.dialogVisible = false
          this.getApiList()
        })
      },
      addSonBtn(row) {
        this.apiInfoFrom.parentName = row.backendApiName
        this.apiInfoFrom.pid = row.backendApiId
        this.submitUrl = true
        this.dialogVisible = true
      },
      compileBtn(row) {
        this.dialogTitle = '修改API信息'
        this.apiInfoFrom.backendApiMethod = row.backendApiMethod.split(',')
        this.apiInfoFrom.backendApiId = row.backendApiId
        this.apiInfoFrom.parentName = row.parentName
        this.apiInfoFrom.pid = row.pid
        this.apiInfoFrom.backendApiName = row.backendApiName
        this.apiInfoFrom.backendApiUrl = row.backendApiUrl
        this.apiInfoFrom.backendApiSort = row.backendApiSort + ''
        this.apiInfoFrom.description = row.description
        this.submitUrl = true
        this.dialogVisible = true
      },
      deleteBtn(row) {
        this.$confirm(`此操作将永久删除【${row.backendApiName}】菜单, 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteApi(row.backendApiId).then(res => {
            if (res.status !== 200) return this.$message.error(res.message);
            this.$message({
              type: 'success',
              message: res.message
            });
            this.dialogVisible = false
            this.getApiList()
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });

      },
      addApiBtn() {
        this.dialogTitle = '添加新的API'
        this.dialogVisible = true
        this.submitUrl = true

      },

      dialogClose() {
        this.submitUrl = false
        this.$refs.apiInfoFromRef.resetFields()
        this.apiInfoFrom.backendApiId = ''
        this.apiInfoFrom.parentName = ''
        this.apiInfoFrom.pid = ''
        this.apiInfoFrom.backendApiName = ''
        this.apiInfoFrom.backendApiUrl = ''
        this.apiInfoFrom.backendApiMethod = ['GET']
        this.apiInfoFrom.backendApiSort = ''
        this.apiInfoFrom.description = ''
      },
      handleClose(done) {
        done()
      },
      getApiList() {
        getApiList().then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
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
              if (item2.backendApiId === item3.pid) {
                item2.children.push(item3)
              }
            })
          })
          this.apiList = Arr
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