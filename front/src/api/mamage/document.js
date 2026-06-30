import request from '@/utils/request'

// 查询资料列表
export function listDocument(params) {
  return request({
    url: '/system/document/list',
    method: 'get',
    params: params
  })
}

// 查询资料详细
export function getDocument(id) {
  return request({
    url: '/system/document/' + id,
    method: 'get'
  })
}

// 新增资料
export function addDocument(data) {
  return request({
    url: '/system/document',
    method: 'post',
    data: data
  })
}

// 修改资料
export function updateDocument(data) {
  return request({
    url: '/system/document',
    method: 'put',
    data: data
  })
}

// 删除资料
export function delDocument(id) {
  return request({
    url: '/system/document/' + id,
    method: 'delete'
  })
}

// 查询资料的所有版本
export function getDocumentVersions(documentId) {
  return request({
    url: '/system/document/versions/' + documentId,
    method: 'get'
  })
}

// 根据资料名称和版本号查询详情
export function getDocumentDetailByNameAndVersion(name, version) {
  return request({
    url: '/system/document/detail',
    method: 'get',
    params: { name, version }
  })
}

// 获取或转换PDF
export function getOrConvertPdf(attachmentId) {
  return request({
    url: '/system/pdf/convert/' + attachmentId,
    timeout:0,
    method: 'get'
  })
}

// 获取或转换PDF
export function getPdfInfo(attachmentId) {
  return request({
    url: '/system/pdf/' + attachmentId,
    method: 'get'
  })
}
