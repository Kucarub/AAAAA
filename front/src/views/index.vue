<template>
  <div class="app-container home">
    首页
  </div>
</template>

<script setup name="Index">
const version = ref('3.9.2')

function goTarget(url) {
  window.open(url, '__blank')
}
</script>

<style scoped lang="scss">
.home {
  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;
  text-align: center;
}
</style>

