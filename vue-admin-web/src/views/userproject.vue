<template>
  <div>
    <el-form :inline="true" :model="selectform" ref="queryParam" label-width="90px" class="demo-form-inline">
      <el-form-item label="用户名：" prop="position">
        <el-input v-model="selectform.username" placeholder="username" ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="default" @click="selectUser">搜索</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" plain @click="insert = true">新增</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" plain @click="getData()">刷新</el-button>
      </el-form-item>
    </el-form>


    <el-table :data="tableData" style="width: 100%" :row-key="getRowKeys" >
      <el-table-column prop="id" label="账号uid" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="password" label="密码"/>
      <el-table-column prop="phone" label="电话" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="gender" label="性别" />
      <el-table-column prop="createdUser" label="创建用户" />
      <el-table-column prop="createdTime" label="创建时间" />
      <el-table-column prop="modifiedUser" label="最后修改用户" />
      <el-table-column prop="modifiedTime" label="最后修改时间" />
      <el-table-column label="操作" prop="compile" width="140dx">
        <template #default="scope">
          <el-button type="primary" size="small" @click="Update(scope.row)">编辑</el-button>
          <el-button type="primary" size="small" @click="Delete(scope.row)">删除</el-button>
        </template >
      </el-table-column>
      <!--      <div style="height: 50px;display: flex;justify-content: flex-end;align-items: center;margin-right: 10px;" class="myTranslationContent_page">-->
      <!--        &lt;!&ndash; 当前页数，支持 .sync 修饰符 1  &ndash;&gt;-->
      <!--        <el-pagination-->
      <!--            @size-change="handleSizeChange"-->
      <!--            @current-change="handleCurrentChange"-->
      <!--            :current-page.sync="params.currPage"-->
      <!--            :page-sizes="params.pageSizeArr"-->
      <!--            :page-size="params.pageSize"-->
      <!--            :layout="params.layout"-->
      <!--            :total="params.totalCount">-->
      <!--        </el-pagination>-->
      <!--      </div>-->
    </el-table>
  </div>
  <el-dialog v-model="insert"  width="500">
    <el-form :model="form">
      <el-form-item label="用户名" :label-width="formLabelWidth">
        <el-input v-model="form.username" placeholder="username" />
      </el-form-item>
      <el-form-item label="密码" :label-width="formLabelWidth">
        <el-input v-model="form.password" placeholder="password"/>
      </el-form-item>
      <el-form-item label="电话号码" :label-width="formLabelWidth">
        <el-input v-model="form.phone" placeholder="phone"/>
      </el-form-item>
      <el-form-item label="邮箱" :label-width="formLabelWidth">
        <el-input v-model="form.email" placeholder="email"/>
      </el-form-item>
      <el-form-item label="性别" :label-width="formLabelWidth">
        <el-input v-model="form.gender" placeholder="gender"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="addnewUser()">提交</el-button>
        <el-button @click="insert=false">取消</el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog v-model="handleUpdate"  width="500">
    <el-form :model="updateform">
      <el-form-item label="用户名" :label-width="formLabelWidth">
        <el-input v-model="updateform.username" placeholder="username" />
      </el-form-item>
      <el-form-item label="电话号码" :label-width="formLabelWidth">
        <el-input v-model="updateform.phone" placeholder="phone"/>
      </el-form-item>
      <el-form-item label="邮箱" :label-width="formLabelWidth">
        <el-input v-model="updateform.email" placeholder="email"/>
      </el-form-item>
      <el-form-item label="性别" :label-width="formLabelWidth">
        <el-input v-model="updateform.gender" placeholder="gender"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="updateData()">提交</el-button>
        <el-button @click="handleUpdate=false">取消</el-button>
      </div>
    </template>
  </el-dialog>


</template>

<script>
import { ElMessage} from 'element-plus';
import axios from "axios";

export default {
  data(){
    return{
      insert:false,
      form:{
        username: '',
        password: '',
        phone: '',
        email: '',
        gender: '',
      },

      formLabelWidth:'140dx',
      handleUpdate:false,
      updateform:{
        id:'',
        username: '',
        phone: '',
        email: '',
        gender: '',
      },

      selectform:{
        username:'',
      },

      tableData: [],
      params: {
        currPage: 1, 	//初始页面
        pageSize: 5, 	// 每页的数据
        totalCount: 0,  //总共多少条数据
        pageSizeArr:[2, 5, 10, 50,100,300],	//条数切换
        layout:'total,sizes, prev, pager, next, jumper',
        condition:{}
      },

      checkData: [], 	 // table 选中数据
      checkDataID: [], // table选中数据ID
    }
  },

  methods:{
    getData(){
      axios.post("http://localhost:6081/user/getlist",{
        // "pageSize": this.params.pageSize,
        // "pageIndex": this.params.currPage,
      },{
        headers:{
          "Authorization":localStorage.getItem("token")
        }
      }).then(resp=>{
        console.log(resp.data)
        if (resp.data.code="20000"){
          //this.params.totalCount=resp.data.data.count;
          this.tableData=resp.data.data;
        }
      }).catch(error=>{
        console.log(error)
      })
    },

    Delete(row){
      axios.post("http://localhost:6081/user/delete",{
        "username":row.username,
      },{
        headers:{
          "Authorization":localStorage.getItem("token")
        }
      }).then(resp=>{
        console.log(resp.data)
        if (resp.data.code="20000"){
          ElMessage({
            message:"删除成功",
            type:"success"
          })
          this.getData()
        }
      }).catch(error=>{
        ElMessage({
          message:"删除失败",
          type:"error"
        })
        console.log(error)
      })
    },
    //更新用户
    Update(row){
      this.updateform.id=row.id;
      this.updateform.username=row.username;
      this.updateform.phone=row.phone;
      this.updateform.email=row.email;
      this.updateform.gender=row.gender;
      this.handleUpdate=true;
    },
    updateData(){
      axios.post("http://localhost:6081/user/update",{
        "id":this.updateform.id,
        "username":this.updateform.username,
        "phone":this.updateform.phone,
        "email":this.updateform.email,
        "gender":this.updateform.gender
      },{
        headers:{
          "Authorization":localStorage.getItem("token")
        }
      }).then(resp=>{
        console.log(resp.data)
        if (resp.data.code="20000"){
          ElMessage({
            message:"修改成功",
            type:"success"
          })
          this.getData(),
              this.handleUpdate = false
        }
      }).catch(error=>{
        ElMessage({
          message:"修改失败",
          type:"error"
        })
        console.log(error)
        this.handleUpdate = false
      })
    },
    //新增用户
    addnewUser(){
      axios.post("http://localhost:6081/user/insert",{
        "username":this.form.username,
        "password":this.form.password,
        "phone":this.form.phone,
        "email":this.form.email,
        "gender":this.form.gender
      },{
        headers:{
          "Authorization":localStorage.getItem("token")
        }
      }).then(resp=>{
        console.log(resp.data)
        if (resp.data.code="20000"){
          ElMessage({
            message:"添加成功",
            type:"success"
          })
          this.getData(),
              this.insert= false
        }
      }).catch(error=>{
        ElMessage({
          message:"添加失败",
          type:"error"
        })
        console.log(error)
        this.insert = false
      })
    },

    //查询
    selectUser(){
      axios.post("http://localhost:6081/user/select",{
        // "pageSize": this.params.pageSize,
        // "pageIndex": this.params.currPage,
        "username": this.selectform.username,
      },{
        headers:{
          "Authorization":localStorage.getItem("token")
        }
      }).then(resp=>{
        console.log(resp.data)
        if (resp.data.code="20000"){
          //this.params.totalCount=resp.data.data.count;

          this.tableData=resp.data.data;
        }

      }).catch(error=>{
        console.log(error)
      })
    },

    getRowKeys(row) {
      return row.order_num; // n_ID为列表数据的唯一标识
    },
    //分页每页展示条数更改
    handleSizeChange(val) {
      this.params.currPage = 1;
      this.params.pageSize = val;
      if(this.queryParam.categoryId == '')
        this.loadData();
      else
        this.findData();
    },
    //分页切换
    handleCurrentChange(val) {
      console.log(val);
      this.params.currPage = val;
      if(this.queryParam.categoryId == '')
        this.loadData();
      else
        this.findData();

    },
  },
  created() {
    this.getData();
  },
}

</script>


<style scoped>
.search-box {
  margin-bottom: 20px;
}
.search-input {
  width: 200px;
}
.mr10 {
  margin-right: 10px;
}

</style>