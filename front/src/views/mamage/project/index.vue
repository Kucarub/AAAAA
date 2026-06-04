<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="项目名称" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入项目名称"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目编码" prop="code">
        <el-input
            v-model="queryParams.code"
            placeholder="请输入项目编码"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="Plus"
            @click="handleAdd"
            v-hasPermi="['mamage:project:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['mamage:project:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['mamage:project:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="projectList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="项目名称" align="center" prop="name">
        <template #default="scope">
          <el-button link type="primary" @click="viewDoc(scope.row)">
            <span>{{ scope.row.name }}</span>
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="项目编码" align="center" prop="code"/>
      <el-table-column label="项目描述" align="center" prop="description"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)"
                     v-hasPermi="['mamage:project:edit']">修改
          </el-button>
          <el-button link type="primary" icon="Edit" @click="handleDocAdd(scope.row)"
                     v-hasPermi="['mamage:project:edit']">添加资料
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['mamage:project:remove']">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
        v-show="total>0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <ProjectDialog ref="projectDialogRef" @success="handleProjectSuccess"/>
    <DocumentTableDialog ref="DocumentTableDialogRef" @success="handleTableDocumentSuccess"/>
    <DocumentDialog ref="documentDialogRef" @success="handleDocumentSuccess"/>
  </div>
</template>

<script setup name="Project">
import {listProject, getProject, delProject, addProject, updateProject} from "@/api/mamage/project"
import {listDocument, addDocument} from "@/api/mamage/document"
import ProjectDialog from './components/ProjectDialog'
import DocumentDialog from './components/DocumentDialog'
import DocumentTableDialog from './components/DocumentTableDialog'

const {proxy} = getCurrentInstance()

const projectList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)

const projectDialogRef = ref(null)
const documentDialogRef = ref(null)
const DocumentTableDialogRef = ref(null)
const currentProjectId = ref(null)

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    code: undefined,
    description: undefined,
    status: undefined,
    createdTime: undefined,
    updatedTime: undefined
  }
})

const {queryParams} = toRefs(data)

/** 查询项目列表 */
function getList() {
  loading.value = true
  listProject(queryParams.value).then(response => {
    projectList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  projectDialogRef.value.openDialog()
}

/** 修改按钮操作 */
function handleUpdate(row) {
  const _id = row.id || ids.value
  getProject(_id).then(response => {
    projectDialogRef.value.openDialog(response.data)
  })
}

function viewDoc(row) {
  currentProjectId.value = row.id
  DocumentTableDialogRef.value.openDialog('view', row)
}

function handleDocAdd(row) {
  currentProjectId.value = row.id
  documentDialogRef.value.openDialog('add', {projectId: row.id})
}

function handleTableDocumentSuccess(tableData) {
  console.log(tableData)
}

/** 项目对话框提交成功处理 */
function handleProjectSuccess(formData) {
  if (formData.id != null) {
    updateProject(formData).then(() => {
      proxy.$modal.msgSuccess("修改成功")
      getList()
    })
  } else {
    addProject(formData).then(() => {
      proxy.$modal.msgSuccess("新增成功")
      getList()
    })
  }
}

/** 资料对话框提交成功处理 */
function handleDocumentSuccess(formData) {
  formData.projectId = currentProjectId.value
  addDocument(formData).then(res => {
    proxy.$modal.msgSuccess("操作成功")
    // 刷新资料列表
    if (currentProjectId.value) {
      DocumentTableDialogRef.value.getDocumentList()
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除项目？').then(function () {
    return delProject(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {
  })
}

/** 导出按钮操作 */
function handleExport() {
  proxy.$modal.msg("正在开发中......")
  // proxy.download('mamage/project/export', {
  //   ...queryParams.value
  // }, `project_${new Date().getTime()}.xlsx`)
}

getList()
</script>
