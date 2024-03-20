import Vue from 'vue'

export const CommonJs = {
    data(){
        return {

            queryParam: {
                order_num:'',
                create_name:''
            },		// 列表主页面查询条件

            tableData: [], 		// 表格数据

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
    created() {
        this.loadData();
    },
    methods:{
// ********************一、【新增】 弹框出现到保存成功，重新回调列表方法******************************************************************
        // 新增
        handleAdd(m) {
            this.$refs.modalForm.add();
            this.$refs.modalForm.title = "新建" + m;
            this.$refs.modalForm.disableSubmit = false;
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




// ********************二、【列表】*********************************
        // 初始化获取列表数据
        loadData(){
            var _this = this;
            this.$http
                .post(this.url.list, this.params)
                .then(function(res) {
                    if (res.data.resultcode == 200) {
                        _this.tableData = res.data.result;
                        _this.params.totalCount = Number(res.data.total)
                    }
                })
                .catch((error) => {});
        },
        // 查询
        conditionQuery() {
            // 对象拼接
            // this.params = Object.assign(this.params,this.queryParam);

            this.params.condition = this.queryParam;
            this.loadData();
        },

        // 重置
        resetQuery() {
            this.queryParam = {};

            this.params.condition = {}
            this.params.currPage = 1
            this.params.pageIndex = 1;
            this.loadData();

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
            this.loadData();
        },
        //分页切换
        handleCurrentChange(val) {
            this.params.pageIndex = val;
            this.loadData();
        },
    }
}
