<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="资料名称" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入资料名称"
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
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="documentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="所属项目" align="center" prop="projectName"/>
      <el-table-column label="资料名称" align="center" prop="name"/>
      <el-table-column label="资料描述" align="center" prop="description"/>
      <el-table-column label="资料备注" align="center" prop="remark">
        <template #default="scope">
          <span v-if="!scope.row.remark">-</span>
          <template v-else>
            <template v-for="(part, index) in splitRemark(scope.row.remark)" :key="index">
              <a v-if="isUrl(part)" :href="part" target="_blank" rel="noopener noreferrer" class="remark-link">
                {{ part }}
              </a>
              <span v-else>{{ part }}</span>
            </template>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="附件" align="center" prop="fileName">
        <template #default="scope">
          <span>{{ scope.row.fileName && scope.row.fileName !== 'placeholder' ? scope.row.fileName : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="版本号" align="center" prop="version"/>
      <el-table-column label="创建人" align="center" prop="nickName">
        <template #default="scope">
          <span>{{ scope.row.nickName || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createdTime">
        <template #default="scope">
          <span>{{ scope.row.createdTime ? parseTime(scope.row.createdTime) : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" v-if="scope.row.attachmentId" @click="handlePdfPreview(scope.row)">预览
          </el-button>
          <el-button link type="primary" icon="Download" v-if="scope.row.filePath" @click="handleDownload(scope.row)">下载
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['mamage:document:remove']">删除
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

    <!-- 附件预览对话框 -->
    <el-dialog
        v-model="pdfDialogVisible"
        title="附件预览"
        width="80%"
        top="5vh"
        :close-on-click-modal="false"
        destroy-on-close
    >
      <div style="height: 70vh; overflow: hidden;">
        <iframe
            v-if="pdfUrl"
            :src="pdfUrl"
            style="width: 100%; height: 100%; border: none;"
        ></iframe>
        <el-empty v-else description="暂无附件"></el-empty>
      </div>
    </el-dialog>
  </div>
</template>

<script setup name="Document">
import { listDocument, delDocument, getOrConvertPdf } from "@/api/mamage/document"

const { proxy } = getCurrentInstance()

const documentList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const pdfDialogVisible = ref(false)
const pdfUrl = ref('')

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined
  }
})

const { queryParams } = toRefs(data)

/** 查询资料列表 */
function getList() {
  loading.value = true
  listDocument(queryParams.value).then(response => {
    documentList.value = response.rows
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

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除资料？').then(function () {
    return delDocument(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {
  })
}

/** 下载附件 */
function handleDownload(row) {
  if (row.filePath) {
    const url = import.meta.env.VITE_APP_BASE_API + '/common/download/resource?resource=' + encodeURIComponent(row.filePath)
    window.open(url)
  } else {
    proxy.$modal.msgWarning('暂无附件可下载')
  }
}

/** 预览附件 */
function handlePdfPreview(row) {
  if (!row.attachmentId) {
    proxy.$modal.msgWarning('该资料无附件可预览')
    return
  }
  
  // 直接预览
  pdfUrl.value = import.meta.env.VITE_APP_BASE_API + '/system/pdf/preview/' + row.attachmentId
  pdfDialogVisible.value = true
}

/** 判断是否是URL链接 */
function isUrl(text) {
  if (!text) return false
  const urlPattern = /^(https?:\/\/)/i
  return urlPattern.test(text)
}

/** 拆分备注文本，分离链接和普通文本 */
function splitRemark(text) {
  if (!text) return []

  const urlPattern = /(https?:\/\/[^\s]+)/gi
  const result = []
  let lastIndex = 0
  let match

  while ((match = urlPattern.exec(text)) !== null) {
    if (match.index > lastIndex) {
      result.push(text.substring(lastIndex, match.index))
    }
    result.push(match[0])
    lastIndex = match.index + match[0].length
  }

  if (lastIndex < text.length) {
    result.push(text.substring(lastIndex))
  }

  return result.length > 0 ? result : [text]
}

getList()
</script>
