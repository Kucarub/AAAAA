<template>
  <el-dialog :title="title" center v-model="open" width="80%" append-to-body @close="handleClose">
    <div style="margin-bottom: 20px;">
      <el-form inline>
        <el-form-item label="版本选择">
          <el-select v-model="selectedVersion" placeholder="请选择版本" @change="handleVersionChange" clearable
                     style="width: 200px;">
            <el-option
                label="最新版本"
                value=""
            />
            <el-option
                v-for="v in allVersionList"
                :key="v"
                :label="v"
                :value="v"
            />
          </el-select>
        </el-form-item>
      </el-form>
    </div>

    <el-table v-loading="loading" :data="tableData">
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
          <span v-if="scope.row.fileName && scope.row.fileName !== 'placeholder'">{{ scope.row.fileName }}</span>
          <span v-else>--</span>
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
          <el-button link type="primary" icon="Download" v-if="scope.row.filePath && scope.row.filePath!=='placeholder'"
                     @click="handleDownload(scope.row)">下载附件
          </el-button>
          <span v-else>--</span>
        </template>
      </el-table-column>
    </el-table>
    <pagination
        v-show="total>0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getDocumentList"
    />
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="cancel">关 闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import {listDocument, getDocumentVersions, getDocumentDetailByNameAndVersion} from "@/api/mamage/document"

const {proxy} = getCurrentInstance()

const emit = defineEmits(['success'])

const open = ref(false)
const title = ref('')
const loading = ref(false)
const projectId = ref(null)
const tableData = ref([])
const allVersionList = ref([])
const selectedVersion = ref('')
const total = ref(0)
const queryParams = ref({
  pageNum: 1,
  pageSize: 10
})
const originalTableData = ref([])

function reset() {
  tableData.value = []
  originalTableData.value = []
  allVersionList.value = []
  selectedVersion.value = ''
  queryParams.value.pageNum = 1
}

function handleClose() {
  reset()
}

function cancel() {
  open.value = false
  reset()
}

async function getDocumentList() {
  if (!projectId.value) return
  loading.value = true
  try {
    const res = await listDocument({ projectId: projectId.value })
    originalTableData.value = res.rows || res.data || []
    total.value = res.total || originalTableData.value.length

    // 收集所有版本
    const versionSet = new Set()
    for (const doc of originalTableData.value) {
      if (doc.version) {
        versionSet.add(doc.version)
      }
    }
    allVersionList.value = Array.from(versionSet).sort().reverse() // 最新版本在前

    // 按版本过滤
    applyVersionFilter()
  } finally {
    loading.value = false
  }
}

function applyVersionFilter() {
  if (!selectedVersion.value) {
    // 最新版本
    tableData.value = originalTableData.value
  } else {
    // 指定版本，需要查询所有资料的该版本
    // 这里简化处理，只显示当前已有的该版本数据
    // 如果需要完整功能，需要后端支持按版本查询列表
    tableData.value = originalTableData.value.filter(doc => doc.version === selectedVersion.value)
  }
}

async function handleVersionChange(version) {
  // 如果需要更精确的查询，可以这里调用后端接口
  applyVersionFilter()
}

function handleDownload(row) {
  if (row.filePath) {
    const url = import.meta.env.VITE_APP_BASE_API + '/common/download/resource?resource=' + encodeURIComponent(row.filePath)
    window.open(url)
  } else {
    proxy.$modal.msgWarning('暂无附件可下载')
  }
}

// 判断是否是URL链接
function isUrl(text) {
  if (!text) return false
  const urlPattern = /^(https?:\/\/)/i
  return urlPattern.test(text)
}

// 拆分备注文本，分离链接和普通文本
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

function openDialog(type, row) {
  reset()
  if (type === 'view') {
    title.value = '查看资料'
  } else if (type === 'add') {
    title.value = '添加资料'
  } else if (type === 'edit') {
    title.value = '修改资料'
  }
  if (row) {
    projectId.value = row.id || row.projectId
    open.value = true
    nextTick(() => {
      getDocumentList()
    })
  } else {
    open.value = true
  }
}

defineExpose({openDialog})
</script>
