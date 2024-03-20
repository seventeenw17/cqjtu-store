<template>
  <!-- 外部用户 -->
  <div class="externalUsers">
    <el-form :inline="true" :model="queryParam" ref="queryParam" label-width="90px" class="demo-form-inline">
      <el-form-item label="商品分类：" prop="position">
        <el-select v-model="queryParam.categoryId" style="width: 200px" placeholder="请选择">
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="商品编号：" prop="position">
        <el-input v-model="queryParam.productId" placeholder="请输入"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="small" @click="conditionQuery">查询</el-button>
      </el-form-item>
      <el-form-item>
        <el-button size="small" @click="resetQuery()">重置</el-button>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="small" @click="addFormVisible=true">新建</el-button>
      </el-form-item>
    </el-form>

    <!-- 内容区 -->
    <el-row>
      <el-container class="mian">

        <!-- table表格 -->
        <el-main>
          <div class="table">
            <el-table
                ref="table"
                :stripe="true"
                :data="productTableData"
                :row-key="getRowKeys"
                size="small"
                height="63vh"
                @selection-change="handleSelectionChange">


              <el-table-column

                  prop="id"
                  label="商品id"
                  border = "true"
                  align = "center"
                  width="100"
                  sortable
              >
              </el-table-column>
              <el-table-column
                  prop="categoryId"
                  label="商品分类id"
                  border = "true"
                  align = "center"
                  width="80"
                  sortable
              >
              </el-table-column>
              <el-table-column
                  prop="itemType"
                  label="商品系列"
                  border = "true"
                  align = "center"
                  sortable
              >
              </el-table-column>
              <el-table-column
                  prop="title"
                  label="商品标题"
                  border = "true"
                  align = "center"
                  width="200"
                  sortable
              >
              </el-table-column>
              <el-table-column
                  prop="sellPoint"
                  label="商品卖点"
                  border = "true"
                  align = "center"
                  width="200"
                  sortable
              >
              </el-table-column>
              <el-table-column
                  prop="price"
                  label="商品价格"
                  border = "true"
                  align = "center"
                  sortable
              >
              </el-table-column>
              <el-table-column
                  prop="num"
                  label="商品库存"
                  border = "true"
                  align = "center"
                  sortable
              >
              </el-table-column>
              <el-table-column
                  prop="image"
                  label="图片"
                  border = "true"
                  align = "center"
                  sortable
              >
                <template #default="scope">
                <div class="demo-image">
                  <div class="block">
                    <span class="demonstration"></span>
                    <el-image :src="'http://localhost:81/img'+scope.row.image+'1.jpg'">

                    </el-image>
                  </div>
                </div>
                </template>
              </el-table-column>
              <el-table-column
                  prop="priority"
                  label="显示优先级"
                  border = "true"
                  align = "center"
                  sortable
              >
              </el-table-column>
              <el-table-column
                  prop="status"
                  label="状态"
                  border = "true"
                  align = "center"
                  sortable
              >
                <template #default="scope">
                  <el-switch
                      v-model="scope.row.status"
                      disabled>
                  </el-switch>
                </template>

              </el-table-column>
              <el-table-column
                  label="修改商品"
                  border = "true"
                  align = "center"
                  sortable
              ><template #default="scope">
                <el-button type="primary" icon="Edit" circle @click="editProduct(scope.row)"></el-button>
              </template>
              </el-table-column>
              <el-table-column
                  label="删除"
                  border = "true"
                  align = "center"
                  sortable
              >
                <template #default="scope">
                  <el-button type="danger" icon="Delete" circle @click="deleteOrder(scope.row)"></el-button>
                </template>

              </el-table-column>
              <!--                        </el-table>-->
              <!--                            <el-table-column fixed="right" label="删除" sortable align="center">-->
              <!--                                <template slot-scope="scope">-->
              <!--                                    <el-button type="text" @click="changeStatus('D',scope.row.order_num)">删除</el-button>-->
              <!--                                </template>-->
              <!--                            </el-table-column>-->
            </el-table>

            <!-- 分页 -->
            <div style="height: 50px;display: flex;justify-content: flex-end;align-items: center;margin-right: 10px;" class="myTranslationContent_page">
              <!-- 当前页数，支持 .sync 修饰符 1  -->
              <el-pagination
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange"
                  :current-page.sync="params.currPage"
                  :page-sizes="params.pageSizeArr"
                  :page-size="params.pageSize"
                  :layout="params.layout"
                  :total="params.totalCount">
              </el-pagination>
            </div>
          </div>
        </el-main>
      </el-container>
    </el-row>

  </div>
  <el-dialog title="图片" v-model="photo">
    <el-image v-for="url in urls" :key="url" :src="url" lazy></el-image>
  </el-dialog>


  <el-dialog title="修改商品" v-model="dialogFormVisible">
    <el-form :model="form">
      <el-form-item label="分类id" :label-width="formLabelWidth">
        <el-input v-model="form.categoryId"></el-input>
      </el-form-item>
      <el-form-item label="商品系列" :label-width="formLabelWidth">
        <el-input v-model="form.itemType"></el-input>
      </el-form-item>
      <el-form-item label="商品标题" :label-width="formLabelWidth">
        <el-input v-model="form.title"></el-input>
      </el-form-item>
      <el-form-item label="商品卖点" :label-width="formLabelWidth">
        <el-input v-model="form.sellPoint"></el-input>
      </el-form-item>
      <el-form-item label="单价" :label-width="formLabelWidth">
        <el-input v-model="form.price"></el-input>
      </el-form-item>
      <el-form-item label="数量" :label-width="formLabelWidth">
        <el-input-number v-model="form.num" @change="handleChange" :min="0" :max="10000000" label="描述文字"></el-input-number>
      </el-form-item>
      <el-form-item label="图片" :label-width="formLabelWidth">
        <el-input v-model="form.image"></el-input>
      </el-form-item>
      <el-form-item label="状态" :label-width="formLabelWidth">
      <el-tooltip :content="'是否上架: ' + form.status" >
        <el-switch
            v-model="form.status"
            active-color="#13ce66"
            inactive-color="#ff4949"
            >
        </el-switch>
      </el-tooltip>
      </el-form-item>
      <el-form-item label="显示优先级" :label-width="formLabelWidth">
        <el-input v-model="form.priority" ></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="editProductReady()">确 定</el-button>
    </div>
  </el-dialog>

  <el-dialog title="添加商品" v-model="addFormVisible">
    <el-form :model="addForm">
      <el-form-item label="分类id" :label-width="formLabelWidth">
        <el-input v-model="addForm.categoryId"></el-input>
      </el-form-item>
      <el-form-item label="商品系列" :label-width="formLabelWidth">
        <el-input v-model="addForm.itemType"></el-input>
      </el-form-item>
      <el-form-item label="商品标题" :label-width="formLabelWidth">
        <el-input v-model="addForm.title"></el-input>
      </el-form-item>
      <el-form-item label="商品卖点" :label-width="formLabelWidth">
        <el-input v-model="addForm.sellPoint"></el-input>
      </el-form-item>
      <el-form-item label="单价" :label-width="formLabelWidth">
        <el-input v-model="addForm.price"></el-input>
      </el-form-item>
      <el-form-item label="数量" :label-width="formLabelWidth">
        <el-input-number v-model="addForm.num" @change="handleChange" :min="0" :max="10000000" label="描述文字"></el-input-number>
      </el-form-item>
      <el-form-item label="图片" :label-width="formLabelWidth">
        <el-input v-model="addForm.image"></el-input>
      </el-form-item>
      <el-form-item label="状态" :label-width="formLabelWidth">
        <el-tooltip :content="'是否上架： ' + addForm.status" >
          <el-switch
              v-model="addForm.status"
              active-color="#13ce66"
              inactive-color="#ff4949"
              active-value="1"
              inactive-value="0"
          >
          </el-switch>
        </el-tooltip>
      </el-form-item>
      <el-form-item label="显示优先级" :label-width="formLabelWidth">
        <el-input v-model="addForm.priority" ></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="addFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="addProduct">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>

import axios from 'axios'
import {ElMessage} from "element-plus";
export default {
  data() {
    return {
      options: [{
        value: '163',
        label: '笔记本'
      }, {
        value: '238',
        label: '本册/便签'
      }, {
        value: '241',
        label: '笔类'
      }, {
        value: '236',
        label: '文件管理'
      }, {
        value: '241',
        label: '笔类'
      }],
      value: '',

      addFormVisible: false,
      addForm:{
        categoryId:'',
        itemType:'',
        title:'',
        sellPoint:'',
        price:'',
        num:'',
        image:'',
        status:'',
        priority:'',
      },

      dialogFormVisible: false,
      form: {
        id:'',
        categoryId:'',
        itemType:'',
        title:'',
        sellPoint:'',
        price:'',
        num:'',
        image:'',
        status:'',
        priority:'',

        name: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: ''
      },
      formLabelWidth: '120px',
      //dsfg
      queryParam: {
        productId:'',
        categoryId:''
      },		// 列表主页面查询条件

      productTableData: [],

      // 分页参数
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

  computed: {

  },
  mounted() {

  },
  methods:{

    handleChange(value) {
      console.log(value);
    },
    // 新增弹框 保存方法（获取form值；调用保存接口；清空列表勾选）
    modalFormOk(params){
      console.log(params)	//$emit传参
      // 新增/修改 成功时，重载列表
      this.loadData();
      //清空列表选中
      this.$refs.table.clearSelection()
      this.onClearSelected()
    },
    // 新增弹窗 保存方法清空列表选中的值
    onClearSelected(){
      this.checkData = [];	 // table 选中数据
      this.checkDataID = []; // table选中数据ID
    },
//删除
    async deleteOrder(row){
      axios.delete("http://localhost:6081//product/deleteProduct/"+row.id,{
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
              this.loadData();
            }
      }).catch(error=>{
        ElMessage({
          message:"删除失败",
          type:"error"
        })
        console.log(error)

      })
    },
    //编辑商品
    editProduct(row){
      this.form.id=row.id;
      this.form.categoryId=row.categoryId;
      this.form.itemType=row.itemType;
      this.form.title=row.title;
      this.form.sellPoint=row.sellPoint;
      this.form.price=row.price;
      this.form.num=row.num;
      this.form.image=row.image;

      this.form.status=row.status;

      this.form.priority=row.priority;
      this.dialogFormVisible = true;
    },
    editProductReady(){
      if (this.form.status==true)
        this.form.status=1;
      else
        this.form.status=0;
      axios.post("http://localhost:6081//product/modify",{
        "id":this.form.id,
        "categoryId": this.form.categoryId,
        "itemType": this.form.itemType,
        "title": this.form.title,
        "sellPoint": this.form.sellPoint,
        "price": this.form.price,
        "num": this.form.num,
        "image": this.form.image,
        "status": this.form.status,
        "priority": this.form.priority
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
          this.loadData(),
              this.dialogFormVisible = false

        }

      }).catch(error=>{
        ElMessage({
          message:"修改失败",
          type:"error"
        })
        console.log(error)
        this.dialogFormVisible = false
      })
    },
    //新建
    addProduct(){
      axios.post("http://localhost:6081//product/addProduct",{
        "categoryId": this.addForm.categoryId,
        "itemType": this.addForm.itemType,
        "title": this.addForm.title,
        "sellPoint": this.addForm.sellPoint,
        "price": this.addForm.price,
        "num": this.addForm.num,
        "image": this.addForm.image,
        "status": this.addForm.status,
        "priority": this.addForm.priority
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
          this.loadData(),
          this.addFormVisible = false
        }

      }).catch(error=>{
        ElMessage({
          message:"添加失败",
          type:"error"
        })
        console.log(error)
        this.addFormVisible = false
      })
    },


// ********************二、【列表】*********************************
    // 初始化获取列表数据
    loadData(){
      axios.post("http://localhost:6081//product/getList",{
        "pageSize": this.params.pageSize,
        "pageIndex": this.params.currPage,
      },{
        headers:{
          "Authorization":localStorage.getItem("token")
        }
      }).then(resp=>{
        console.log(resp.data)
        if (resp.data.code="20000"){
          this.params.totalCount=resp.data.data.count;
          this.productTableData=resp.data.data.data;
        }

      }).catch(error=>{
        console.log(error)
      })
    },
    //修改
    modifyProduct(){

    },
    // 查询
    conditionQuery() {
      // 对象拼接
      // this.params = Object.assign(this.params,this.queryParam);

      this.params.condition = this.queryParam;
      if(this.queryParam.categoryId === ''&&this.queryParam.productId==='')
      this.loadData();
      else if (this.queryParam.productId===''&&this.queryParam.categoryId !== '')
        this.findData();
      else if(this.queryParam.categoryId === ''&&this.queryParam.productId!=='')
        this.findIdData();
      else{
        this.resetQuery();
        ElMessage({
          message:"不能同时查询",
          type:"error"
        })
      }

      // this.queryParam.categoryId = '';
      // this.queryParam.productId='';
    },
    findIdData(){
      axios.get("http://localhost:6081//product/findIdProduct/"+this.queryParam.productId,{
        headers:{
          "Authorization":localStorage.getItem("token")
        }
      }).then(resp=>{
        console.log(resp.data.data)
        if (resp.data.code="20000"){
          this.params.totalCount=1;
          this.productTableData.length = 0;
          this.productTableData.push(resp.data.data);
          ElMessage({
            message:"查询成功",
            type:"success"
          })
        }

      }).catch(error=>{
        console.log(error)
        ElMessage({
          message:"查询失败",
          type:"error"
        })
        location.reload();
      })
    },
    findData(){
      axios.post("http://localhost:6081//product/getCategoryList",{
        "pageSize": this.params.pageSize,
        "pageIndex": this.params.currPage,
        "categoryId": this.queryParam.categoryId,
      },{
        headers:{
          "Authorization":localStorage.getItem("token")
        }
      }).then(resp=>{
        console.log(resp.data)
        if (resp.data.code="20000"){
          this.params.totalCount=resp.data.data.count;
          this.productTableData=resp.data.data.data;
        }

      }).catch(error=>{
        console.log(error)
        ElMessage({
          message:"查询失败",
          type:"error"
        })
      })
    },
    // 重置
    resetQuery() {
      this.queryParam = {};

      this.params.condition = {}
      this.params.currPage = 1

      this.productTableData.length=0;
      this.loadData();
      this.queryParam.categoryId = '';
      this.queryParam.productId='';
    },

    // 列表复选框选中赋值
    handleSelectionChange(val) {
      this.checkData = val;
    },
    /*
        分页的记忆功能，分页保存选中的数据
        :row-key="getRowKeys"
        :reserve-selection="true"
    */
    getRowKeys(row) {
      return row.order_num; // n_ID为列表数据的唯一标识
    },
    // 分页每页展示条数更改
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
    this.loadData();
  },
};



</script>
