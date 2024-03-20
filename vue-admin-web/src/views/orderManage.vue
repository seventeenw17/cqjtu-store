<template>
    <!-- 外部用户 -->
    <div class="externalUsers">
        <el-form :inline="true" :model="queryParam" ref="queryParam" label-width="90px" class="demo-form-inline">
            <el-form-item label="订单号：" prop="position">
                <el-input v-model="queryParam.orderId" placeholder="请输入"></el-input>
            </el-form-item>
            <el-form-item label="用户名：" prop="position">
                <el-input v-model="queryParam.userName" placeholder="请输入"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary"  @click="conditionQuery">查询</el-button>
            </el-form-item>
            <el-form-item>
                <el-button  @click="resetQuery()">重置</el-button>
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
                            :data="orderTableData"
                            size="small"
                            height="63vh">
                            <el-table-column
                                prop="recvName"
                                label="收货人"
                                border = "true"
                                align = "center"
                                sortable
                            >
                            </el-table-column>
                            <el-table-column
                                prop="recvProvince"
                                label="省"
                                border = "true"
                                align = "center"
                                sortable
                            >
                            </el-table-column>
                            <el-table-column
                                prop="recvCity"
                                label="市"
                                border = "true"
                                align = "center"
                                sortable
                            >
                            </el-table-column>
                            <el-table-column
                                prop="recvArea"
                                label="区"
                                border = "true"
                                align = "center"
                                sortable
                            >
                            </el-table-column>
                            <el-table-column
                                prop="recvAddress"
                                label="详细地址"
                                border = "true"
                                align = "center"
                                sortable
                            >
                            </el-table-column>
                            <el-table-column
                                label="状态"
                                border = "true"
                                align = "center"
                                sortable
                            >
                                <template #default="scope">
                                    <div v-if="scope.row.status === 0">未支付</div>
                                    <div v-else-if="scope.row.status === 1">已支付</div>
                                    <div v-else-if="scope.row.status === 2">已收货</div>
                                    <div v-else-if="scope.row.status === 3">退货退款</div>
                                </template>
<!--                                <div v-if=" === '0'">-->

<!--                                </div>-->
                            </el-table-column>
                            <el-table-column
                                prop="totalPrice"
                                label="总价"
                                border = "true"
                                align = "center"
                                sortable
                            >
                            </el-table-column>
                            <el-table-column
                                label="修改地址"
                                border = "true"
                                align = "center"
                                sortable
                            >
                                <template #default = "scope">
                                    <el-button type="primary" icon="Edit" @click="getAddressList(scope.row)" circle />
                                </template>
                            </el-table-column>
                            <el-table-column
                                label="删除"
                                border = "true"
                                align = "center"
                                sortable
                            >
                                    <template #default = "scope">
                                        <el-button type="danger" icon="Delete" @click = "deleteOrder(scope.row)" circle ></el-button>
                                    </template>
                            </el-table-column>

                        </el-table>
                        <!-- 分页 -->
                        <div class="block"  style="height: 50px;display: flex;justify-content: flex-end;align-items: center; margin-right: 10px; margin-top: 10px">
                            <el-pagination
                                :hide-on-single-page = "true"
                                v-model:current-page="params.currentPage"
                                v-model:page-size="params.pageSize"
                                :page-sizes="[10, 20, 30, 40]"
                                layout="total, sizes, prev, pager, next, jumper"
                                v-model:total="params.totalCount"
                                @size-change="handleSizeChange"
                                @current-change="handleCurrentChange"
                            />
                        </div>
                    </div>
                </el-main>
            </el-container>
        </el-row>
    </div>
    <div>
        <el-dialog v-model="chooseAddressVisible" width="500px" style="margin-top: 200px">
            <el-form style="height: 200px">
                <el-form-item>
                    <el-select
                        v-model="addressId"
                        clearable
                        placeholder="选择地址"
                    >
                        <el-option
                            v-for="item in addressList"
                            :key="item.id"
                            :label="item.addressDetail"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item style="margin-top: 100px;margin-left: 300px" >
                    <el-button size="small" @click = "chooseAddressVisible = false">取消</el-button>
                    <el-button size="small" type="primary" @click ="updateAddress()">确认</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>

</template>
<script>

import axios from "axios";
import {ElMessage} from "element-plus";
import {Edit} from "@element-plus/icons-vue";

export default {
    data() {
        return {
            queryParam: {
                orderId:'',
                userName:''
            },		// 列表主页面查询条件

            orderTableData: [], 		// 表格数据

            // 分页参数
            params: {
                currentPage: 1, 	//初始页面
                pageSize: 10, 	// 每页的数据
                totalCount: 100,  //总共多少条数据
                // condition:{}
            },

            checkData: [], 	 // table 选中数据
            checkDataID: [], // table选中数据ID

            addressList:[], //获取的地址列表
            addressId: '',  //选中的地址id
            chooseAddressVisible:false,
            orderId:''
        }
    },

    computed: {

    },

    methods:{
        //加载全部订单
        loadOrderTableData(){
            axios.post("http://localhost:6081/order/allList",{
                "pageSize": this.params.pageSize,
                "pageIndex": this.params.currentPage
            },{
                headers:{
                    "Authorization":localStorage.getItem("token")
                }
            }).then(resp=>{
                if(resp.data.code === 20000){
                    if(resp.data.data ===null){
                        ElMessage({
                            message:"没有数据",
                            type:"error"
                        })
                    }
                    this.orderTableData = resp.data.data.data;
                    this.params.totalCount = resp.data.data.count;
                }
                console.log();
            }).catch(error=>{
                console.log(error)
            })
        },
        // 查询 根据用户名或者订单号查新
        conditionQuery() {
            if(this.queryParam.orderId === ""&&this.queryParam.userName === ""){
                ElMessage("请输入订单号或者用户名查询");
            }else if(this.queryParam.orderId !== ""){
                let url = "http://localhost:6081/order/getOrderListById/"+this.queryParam.orderId;
                //根据订单号查询
                axios.get(url,{
                    headers:{
                        "Authorization":localStorage.getItem("token")
                    }
                }).then(resp=>{
                    console.log(resp.data)
                    if(resp.data.data === null) {
                        ElMessage({
                            message:"订单不存在",
                            type:"error"
                        })
                    }
                    else if(resp.data.code === 20000){
                        this.params.currentPage = 1;
                        this.params.pageSize = 100;
                        ElMessage({
                            message:"查询成功",
                            type:"success"
                        })
                        this.orderTableData.length = 0;
                        this.orderTableData.push(resp.data.data);
                    }
                }).catch(error=>{
                    console.log(error)
                })
            }else if(this.queryParam.userName !== ""){
                let url = "http://localhost:6081/order/getOrderListByName/"+this.queryParam.userName;
                // console.log(url);
                //根据用户名查询
                axios.get(url,{
                    headers:{
                        "Authorization":localStorage.getItem("token")
                    }
                }).then(resp=>{
                    console.log(resp.data)
                    if(resp.data.data === null) {
                        ElMessage({
                            message:"该用户没有订单或用户不存在",
                            type:"error"
                        })
                    }
                    else if(resp.data.code === "20000"){
                        this.params.currentPage = 1;
                        this.params.pageSize = 100;
                        this.params.totalCount = resp.data.data.count;
                        ElMessage({
                            message:"查询成功",
                            type:"success"
                        })
                        let tableData = resp.data.data;
                        this.orderTableData.length = 0;
                        for(let i = 0; i < tableData.length;i++){
                            this.orderTableData.push(tableData[i]);
                        }

                    }
                }).catch(error=>{
                    console.log(error)
                })
            }
        },

        // 重置
        resetQuery() {
            this.queryParam = {};
            this.params.currentPage = 1;
            this.params.pageSize = 10;
            this.loadOrderTableData();
        },
        // 分页每页展示条数更改
        handleSizeChange (val){
            this.params.pageSize = val;
            this.loadOrderTableData();
        },
        //分页切换
        handleCurrentChange(val) {
            this.params.currentPage = val;
            this.loadOrderTableData();
        },
        //删除订单
        deleteOrder(row){
            console.log(row.status);
            if(row.status != 0){
                ElMessage({
                    message:"用户已支付不可删除",
                    type:"error"
                })
                return;
            }
            let url = "http://localhost:6081/order/deleteOrder/"+row.id;
            axios.delete(url,{
                headers:{
                    "Authorization":localStorage.getItem("token"),
                }
            }).then(resp=>{
                console.log(resp.data);
                if (resp.data.code === 20000){
                    ElMessage({
                        message:"删除成功",
                        type:"success"
                    })
                }
            })
           //重新加载订单列表
            this.loadOrderTableData();
        },
        getAddressList(row){
            this.orderId = row.id;
            console.log(this.orderId);
            //获取地址
            let url = "http://localhost:6081/address/getList/"+row.userId;
            this.chooseAddressVisible = true;
            axios.get(url,{
                headers:{
                    "Authorization":localStorage.getItem("token"),
                }
            }).then(resp=>{
                // console.log(resp.data);
                if(resp.data.code===20000){
                    this.addressList.length = 0;
                    for(let i=0; i < resp.data.data.length;i++){
                        this.addressList.push(resp.data.data[i]);
                    }
                }
                // console.log(this.addressList);
            }).catch(err=>{
                console.log(err);
            })
            // console.log(row.userName);
        },
        updateAddress(){
            let url = "http://localhost:6081/order/updateOrder";
            this.chooseAddressVisible = false;
            console.log(this.orderId+"   "+this.addressId)
            if(this.addressId === ""){
                return;
            }
            axios.put(url,{
                orderId: this.orderId,
                addressId:this.addressId,
            },{
                headers:{
                    "Authorization":localStorage.getItem("token"),
                }
            }).then(resp=>{
                // console.log(resp.data);
                if(resp.data.code === 20000){
                    ElMessage({
                        message:"修改地址成功",
                        type:"success"
                    })
                }else{
                    ElMessage({
                        message:"修改地址失败",
                        type:"error"
                    })
                }
            }).catch(error=>{
                console.log(error)
            })
            this.loadOrderTableData();
        }
    },
    //更改 订单地址
    mounted() {

        //重新加载用户订单列表
        this.loadOrderTableData();
    }
};



</script>

<style scoped>

</style>