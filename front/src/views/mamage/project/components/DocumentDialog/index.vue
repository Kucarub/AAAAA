<template>
  <el-dialog :title="title" v-model="open" width="500px" append-to-body @close="handleClose">
    <el-form ref="documentRef" :model="form" :rules="rules" label-width="100px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="资料名称" prop="name">
            <el-input v-model="form.name" placeholder="请输入资料名称"/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="资料描述" prop="description">
            <el-input v-model="form.description" placeholder="请输入资料描述"/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="资料备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="附件" prop="file">
            <FileUpload v-model="form.attachmentId" :limit="1" :fileSize="100"></FileUpload>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="版本号" prop="version">
            <el-input v-model="form.version" placeholder="请输入版本号"/>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import FileUpload from '@/components/FileUpload/index.vue'

const { proxy } = getCurrentInstance()

const emit = defineEmits(['success'])

const open = ref(false)
const title = ref('')
const documentRef = ref(null)

const data = reactive({
  form: {},
  rules: {
    name: [
      { required: true, message: '资料名称不能为空', trigger: 'blur' }
    ],
    version: [
      { required: true, message: '版本号不能为空', trigger: 'blur' }
    ]
  }
})

const { form, rules } = toRefs(data)

function reset() {
  form.value = {
    id: null,
    name: null,
    description: null,
    remark: null,
    file: null,
    version: null
  }
  proxy.resetForm('documentRef')
}

function handleClose() {
  reset()
}

function cancel() {
  open.value = false
  reset()
}

function submitForm() {
  proxy.$refs['documentRef'].validate(valid => {
    if (valid) {
      emit('success', form.value)
      open.value = false
    }
  })
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
    form.value = { ...row }
  }
  open.value = true
}

defineExpose({ openDialog })
</script>
