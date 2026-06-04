<template>
  <el-dialog :title="title" v-model="open" width="500px" append-to-body @close="handleClose">
    <el-form ref="projectRef" :model="form" :rules="rules" label-width="100px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="项目名称" prop="name">
            <el-input v-model="form.name" placeholder="请输入项目名称"/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="项目编码" prop="code">
            <el-input v-model="form.code" placeholder="请输入项目编码"/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="项目描述" prop="description">
            <el-input v-model="form.description" type="textarea" placeholder="请输入内容"/>
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
const { proxy } = getCurrentInstance()

const emit = defineEmits(['success'])

const open = ref(false)
const title = ref('')
const projectRef = ref(null)

const data = reactive({
  form: {},
  rules: {
    name: [
      { required: true, message: '项目名称不能为空', trigger: 'blur' }
    ]
  }
})

const { form, rules } = toRefs(data)

function reset() {
  form.value = {
    id: null,
    name: null,
    code: null,
    description: null,
    status: null,
    createdTime: null,
    updatedTime: null
  }
  proxy.resetForm('projectRef')
}

function handleClose() {
  reset()
}

function cancel() {
  open.value = false
  reset()
}

function submitForm() {
  proxy.$refs['projectRef'].validate(valid => {
    if (valid) {
      emit('success', form.value)
      open.value = false
    }
  })
}

function openDialog(row) {
  reset()
  if (row && row.id) {
    title.value = '修改项目'
    form.value = { ...row }
  } else {
    title.value = '添加项目'
  }
  open.value = true
}

defineExpose({ openDialog })
</script>
