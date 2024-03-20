<template>
  <div>
    <el-dialog
        :title="title"
        :visible.sync="dialogVisible"
        width="30%"
    >

      <com-form ref="realForm" @ok="submitCallback"></com-form>

      <span slot="footer" class="dialog-footer">
		    <el-button @click="dialogVisible = false">取 消</el-button>
		    <el-button type="primary" @click="handleOk">确 定</el-button>
		  </span>
    </el-dialog>
  </div>
</template>
<script>
import comForm from './comForm.vue'
export default{
  components: {
    comForm
  },
  data(){
    return{
      title:'',
      dialogVisible:false,
    }
  },
  methods:{
    add () {
      this.dialogVisible = true
    },
    // 确定
    handleOk(){
      this.$refs.realForm.submitForm();
    },

    // form 触发此方法
    submitCallback(){
      this.$emit('ok',this.$refs.realForm.model);	//触发list页面ok的方法
      this.dialogVisible = false;
    },
  }
}
</script>
<style>
</style>
