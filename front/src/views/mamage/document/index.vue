<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属项目" prop="projectName">
        <el-input
            v-model="queryParams.projectName"
            placeholder="请输入所属项目"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
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
          <span class="remark-link" @click="handlePdfPreview(scope.row)">{{ scope.row.fileName && scope.row.fileName !== 'placeholder' ? scope.row.fileName : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="版本号" align="center" prop="version"/>
      <el-table-column label="创建人" align="center" prop="nickName">
        <template #default="scope">
          <span>{{ scope.row.nickName || '-' }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="创建时间" align="center" prop="createdTime">
        <template #default="scope">
          <span>{{ scope.row.createdTime ? parseTime(scope.row.createdTime) : '-' }}</span>
        </template>
      </el-table-column> -->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="280">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)">修改
          </el-button>
          <el-button link type="primary" icon="Download" v-if="scope.row.attachmentId" @click="handleDownload(scope.row)">下载
          </el-button>
          <el-popover
              v-if="canShare(scope.row)"
              placement="top"
              :width="180"
              trigger="click"
          >
            <template #reference>
              <el-button link type="primary" icon="Share">分享</el-button>
            </template>
            <div style="text-align: center;">
              <img :src="getQrCodeUrl(scope.row)" alt="二维码" style="width: 150px; height: 150px;">
              <p style="margin-top: 8px; font-size: 12px; color: #666;">扫码查看资料</p>
            </div>
          </el-popover>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)">删除
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

    <!-- 修改资料对话框 -->
    <el-dialog
        v-model="editDialogVisible"
        :title="editTitle"
        width="500px"
        :close-on-click-modal="false"
        destroy-on-close
    >
      <el-form ref="editRef" :model="editFormData" :rules="editRules" label-width="80px">
        <el-form-item label="资料名称" prop="name">
          <el-input v-model="editFormData.name" placeholder="请输入资料名称"/>
        </el-form-item>
        <el-form-item label="资料描述" prop="description">
          <el-input v-model="editFormData.description" type="textarea" :rows="3" placeholder="请输入资料描述"/>
        </el-form-item>
        <el-form-item label="资料备注" prop="remark">
          <el-input v-model="editFormData.remark" type="textarea" :rows="3" placeholder="请输入资料备注"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelEdit">取消</el-button>
          <el-button type="primary" @click="submitEdit">确认</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Document">
import { listDocument, getDocument, updateDocument, delDocument, getOrConvertPdf } from "@/api/mamage/document"
import QRCode from 'qrcode'

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
const editDialogVisible = ref(false)
const editOpen = ref(false)
const editTitle = ref('')
const qrCodeMap = ref({})

const editFormData = reactive({
  id: undefined,
  name: undefined,
  description: undefined,
  remark: undefined
})

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    projectName: undefined
  }
})

const { queryParams } = toRefs(data)

/** 查询资料列表 */
function getList() {
  loading.value = true
  listDocument(queryParams.value).then(async response => {
    documentList.value = response.rows
    total.value = response.total
    loading.value = false
    // 预生成可分享资料的二维码
    await generateQrCodes()
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
async function handlePdfPreview(row) {
  if (!row.attachmentId) {
    proxy.$modal.msgWarning('该资料无附件可预览')
    return
  }

  loading.value = true

  try {
    // 先调用转换接口确保PDF已生成
    await getOrConvertPdf(row.attachmentId)

    // 设置预览URL
    pdfUrl.value = import.meta.env.VITE_APP_BASE_API + '/system/pdf/preview/' + row.attachmentId
    pdfDialogVisible.value = true
  } catch (error) {
    proxy.$modal.msgError('转换或预览失败：' + (error.message || '未知错误'))
  } finally {
    loading.value = false
  }
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

/** 从备注中提取第一个URL */
function extractUrl(remark) {
  if (!remark) return null
  const match = remark.match(/(https?:\/\/[^\s]+)/i)
  return match ? match[0] : null
}

/** 判断资料是否可以分享（描述含"在线"且备注中有URL） */
function canShare(row) {
  if (!row.description || !row.remark) return false
  return row.description.includes('在线') && extractUrl(row.remark) !== null
}

/** 获取二维码图片URL */
function getQrCodeUrl(row) {
  return qrCodeMap.value[row.id] || ''
}

/** 预生成所有可分享资料的二维码 */
async function generateQrCodes() {
  const map = {}
  const promises = documentList.value
    .filter(row => canShare(row))
    .map(async row => {
      const url = extractUrl(row.remark)
      if (url) {
        try {
          map[row.id] = await QRCode.toDataURL(url, { width: 150, margin: 1 })
        } catch (e) {
          console.error('生成二维码失败', e)
        }
      }
    })
  await Promise.all(promises)
  qrCodeMap.value = map
}

const editRef = ref(null)

const editRules = {
  name: [
    { required: true, message: '资料名称不能为空', trigger: 'blur' }
  ]
}

/** 修改按钮操作 */
function handleUpdate(row) {
  editFormData.id = row.id
  editTitle.value = '修改资料 - ' + (row.name || '')
  editDialogVisible.value = true
  editOpen.value = true

  // 使用行数据回填表单（行数据已经包含了 name, description, remark）
  editFormData.name = row.name || ''
  editFormData.description = row.description || ''
  editFormData.remark = row.remark || ''
}

/** 取消修改 */
function cancelEdit() {
  editDialogVisible.value = false
  editOpen.value = false
  resetEditForm()
}

/** 重置编辑表单 */
function resetEditForm() {
  editFormData.id = undefined
  editFormData.name = undefined
  editFormData.description = undefined
  editFormData.remark = undefined
  editRef.value?.resetFields()
}

/** 提交修改 */
function submitEdit() {
  editRef.value.validate(valid => {
    if (valid) {
      updateDocument(editFormData).then(() => {
        proxy.$modal.msgSuccess('修改成功')
        editDialogVisible.value = false
        editOpen.value = false
        getList()
      }).catch(() => {
      })
    }
  })
}

getList()
</script>
