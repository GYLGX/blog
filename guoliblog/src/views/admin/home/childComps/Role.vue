<template>
  <el-card>
    <el-row>
      <el-col :span="12">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item>系统管理</el-breadcrumb-item>
          <el-breadcrumb-item>角色管理</el-breadcrumb-item>
        </el-breadcrumb>
      </el-col>
      <el-col :span="12">
        <el-button size="mini" type="warning" plain icon="el-icon-circle-plus-outline" @click="addRoleBtn ">添加新角色
        </el-button>
      </el-col>
    </el-row>

    <el-table :data="roleLists" stripe style="width: 100%">
      <el-table-column prop="roleId" label="编号" width="400">
      </el-table-column>
      <el-table-column prop="roleName" label="角色名称">
      </el-table-column>
      <el-table-column prop="description" label="备注">
      </el-table-column>
      <el-table-column label="操作" width="650">
        <template slot-scope="scope">
          <span v-show="scope.row.roleId !== '1'" style="margin-right: 10px;">
            <el-button size="mini" icon="el-icon-edit" type="primary" @click="handleEdit(scope.row)">修改
            </el-button>
            <el-button size="mini" icon="el-icon-delete" type="danger" @click='handleDelete(scope.row)'>删除</el-button>
            <el-button size="mini" icon="el-icon-user" type="info" @click='assignUser(scope.row)'>分配角色用户
            </el-button>
            <el-button size="mini" icon="el-icon-setting" type="warning" @click='assignMenu(scope.row)'>分配菜单
            </el-button>
            <el-button size="mini" icon="el-icon-magic-stick" type="success" @click='distributionAPI(scope.row)'>
              分配API访问权限
            </el-button>
          </span>
        </template>
      </el-table-column>
    </el-table>

    <!-- 修改角色信息对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync=" dialogVisible" width="30%" :before-close="handleClose"
      @close='dialogClose'>
      <el-form :model="roleInfoFrom" :rules="roleInfoFromRules" ref="roleInfoFromRef" label-width="100px">
        <el-form-item label="角色编码号" v-show="dialogTitle === '修改角色信息' ">
          <el-input v-model="roleInfoFrom.roleId" disabled></el-input>
        </el-form-item>
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleInfoFrom.roleName"></el-input>
          后台自动添加 ROLE_
        </el-form-item>
        <el-form-item label="角色备注" prop="description">
          <el-input v-model="roleInfoFrom.description"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmBtn">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 授权操作对话框 -->
    <el-dialog :title="assignOperationTitle" :visible.sync="assignOperationVisible" width="30%"
      :before-close="assignOperationHandleClose" @close='assignOperationDialogClose'>
      <el-tree :data="assignOperationList" show-checkbox node-key="id" :props="assignOperationDefaultProps"
        ref='assignOperationTreeRef' :default-checked-keys="assignOperationDefaultKeys" default-expand-all>
      </el-tree>
      <span slot="footer" class="dialog-footer">
        <el-button @click="assignOperationVisible = false">取 消</el-button>
        <el-button type="primary" @click="assignOperationConfirmBtn">确 定</el-button>
      </span>
    </el-dialog>

  </el-card>
</template>
<script>
  import {
    getRoleList,
    addRole,
    updateRole,
    deleteRole,
    getRoleInfoList,
    setRoleInfo
  } from 'network/admin'
  export default {
    name: 'Role',
    data() {
      return {
        //需要设置的角色id
        roleId: null,
        requestRrl: '',
        assignOperationTitle: '',
        assignOperationList: [],
        assignOperationDefaultKeys: [],
        assignOperationDefaultProps: {
          children: 'children',
          label: 'name'
        },
        assignOperationVisible: false,
        // 修改角色校验结果
        roleInfoFromRules: {
          roleName: [{
              required: true,
              message: '请输入角色名称',
              trigger: 'blur'
            },
            {
              min: 2,
              max: 12,
              message: '长度在 2 到 12 个字符',
              trigger: 'blur'
            }
          ],
          description: [{
              required: true,
              message: '请输入角色备注',
              trigger: 'blur'
            },
            {
              min: 2,
              max: 12,
              message: '长度在 2 到 12 个字符',
              trigger: 'blur'
            }
          ]
        },
        roleInfoFrom: {
          roleId: '',
          roleName: '',
          description: ''
        },
        dialogTitle: '',
        dialogVisible: false,
        total: 0,
        roleLists: []
      }
    },
    created() {
      this.getRoleList()

    },
    methods: {
      //角色信息操作
      assignOperationDialogClose() {
        this.assignOperationList = []
        this.assignOperationDefaultKeys = []
      },
      assignOperationHandleClose(done) {
        done()
      },
      getRoleInfoList() {
        getRoleInfoList(this.requestRrl, this.roleId).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          let list = res.data.list
          let Arr = []
          list.forEach(item1 => {
            if (!item1.pid) {
              item1.children = []
              Arr.push(item1)
            }
            if (item1.roleId && item1.pid && this.assignOperationTitle !== '给用户赋予角色') {
              this.assignOperationDefaultKeys.push(item1.id)
            } else if (item1.roleId && this.assignOperationTitle === '给用户赋予角色') {
              this.assignOperationDefaultKeys.push(item1.id)
            }
          })
          Arr.forEach(item2 => {
            list.forEach(item3 => {
              if (item2.id === item3.pid) {
                item2.children.push(item3)
              }
            })
          })
          this.assignOperationList = Arr
        })
      },
      //确认点击事件
      assignOperationConfirmBtn() {
        const keys = [
          ...this.$refs.assignOperationTreeRef.getCheckedKeys(),
          ...this.$refs.assignOperationTreeRef.getHalfCheckedKeys()
        ]
        const Arr = []
        keys.forEach(item => {
          let itemObject = {
            id: item,
            roleId: this.roleId
          }
          Arr.push(itemObject)
        })
        setRoleInfo(this.requestRrl, this.roleId, Arr).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            type: 'success',
            message: res.message
          });
          this.getRoleList()
          this.assignOperationVisible = false
        })
      },
      //分配用户角色
      assignUser(row) {
        this.roleId = row.roleId
        this.assignOperationVisible = true
        this.requestRrl = 'roleUser'
        this.getRoleInfoList()
        this.assignOperationTitle = '给用户赋予角色'
      },
      // 分配菜单
      assignMenu(row) {
        this.roleId = row.roleId
        this.assignOperationVisible = true
        this.requestRrl = 'roleMenu'
        this.getRoleInfoList()
        this.assignOperationTitle = '给角色分配访问菜单权限'
      },
      // 分配API
      distributionAPI(row) {
        this.roleId = row.roleId
        this.assignOperationVisible = true
        this.requestRrl = 'roleApi'
        this.getRoleInfoList()
        this.assignOperationTitle = '给角色分配API访问权限'
      },
      // 修改角色
      dialogClose() {
        this.$refs.roleInfoFromRef.resetFields()
        this.roleInfoFrom.roleId = ''
        this.roleInfoFrom.roleName = ''
        this.roleInfoFrom.description = ''
      },
      handleClose(done) {
        done()
      },
      handleDelete(row) {
        this.$confirm(`此操作将永久删除【${row.roleName}】该角色 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteRole(row.roleId).then(res => {
            if (res.status !== 200) return this.$message.error(res.message);
            this.$message({
              type: 'info',
              message: res.message
            });
            this.getRoleList()
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消操作',
          });
        });


      },
      confirmBtn() {
        if (this.dialogTitle === '修改角色信息') {
          this.updateRole()
        } else {
          this.addRole()
        }
      },
      handleEdit(row) {
        this.dialogTitle = '修改角色信息'
        this.dialogVisible = true
        this.roleInfoFrom.roleId = row.roleId
        this.roleInfoFrom.roleName = row.roleName
        this.roleInfoFrom.description = row.description
      },
      addRoleBtn() {
        this.dialogTitle = '添加新角色'
        this.dialogVisible = true
      },
      updateRole() {
        updateRole(this.roleInfoFrom).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            type: 'success',
            message: res.message
          });
          this.getRoleList()
          this.dialogVisible = false
        })
      },
      addRole() {
        addRole(this.roleInfoFrom).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.$message({
            type: 'success',
            message: res.message
          });
          this.getRoleList()
          this.dialogVisible = false
        })
      },
      getRoleList() {
        getRoleList(1, 10).then(res => {
          if (res.status !== 200) return this.$message.error(res.message);
          this.total = res.data.total
          this.roleLists = res.data.items
        })
      }
    },
  }
</script>
<style scoped>

</style>