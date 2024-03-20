<template>
  <div>
    <el-form ref="form" :model="model" label-width="80px">
      <el-form-item label="活动名称">
        <el-input v-model="model.name" placeholder="请输入活动名称"></el-input>
      </el-form-item>
      <el-form-item label="活动区域">
        <el-select v-model="model.region" placeholder="请选择活动区域">
          <el-option label="区域一" value="shanghai"></el-option>
          <el-option label="区域二" value="beijing"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      model:{}
    }
  },
  created() {
    this.modelDefault = JSON.parse(JSON.stringify(this.model));
  },
  methods:{
    submitForm(){
      //console.log(this.model);
      this.$emit('ok',this.model);	//触发父页面的submitCallback方法
    },
  }
}
</script>

<style>
</style>
