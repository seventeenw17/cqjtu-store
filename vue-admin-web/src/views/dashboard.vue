<template>
	<div>
		<el-row :gutter="20">
			<el-col :span="8">
				<el-card shadow="hover" class="mgb20" style="height: 224px">
					<div class="user-info">
						<el-avatar :size="120" :src="imgurl" />
						<div class="user-info-cont">
							<div class="user-info-name">{{ name }}</div>
							<div>{{ role }}</div>
						</div>
					</div>
					<div class="user-info-list">
						IP属地：
						<span>重庆</span>
					</div>
				</el-card>
			</el-col>
			<el-col :span="16">
				<el-row :gutter="20" class="mgb20">
					<el-col :span="8">
						<el-card shadow="hover" :body-style="{ padding: '0px' }">
							<div class="grid-content grid-con-1">
								<el-icon class="grid-con-icon"><User /></el-icon>
								<div class="grid-cont-right">
									<div class="grid-num">{{dataSet.userNum}}</div>
									<div>用户数量</div>
								</div>
							</div>
						</el-card>
					</el-col>
					<el-col :span="8">
						<el-card shadow="hover" :body-style="{ padding: '0px' }">
							<div class="grid-content grid-con-2">
								<el-icon class="grid-con-icon"><List /></el-icon>
								<div class="grid-cont-right">
									<div class="grid-num">{{dataSet.orderNum}}</div>
									<div>成交订单</div>
								</div>
							</div>
						</el-card>
					</el-col>
					<el-col :span="8">
						<el-card shadow="hover" :body-style="{ padding: '0px' }">
							<div class="grid-content grid-con-3">
								<el-icon class="grid-con-icon"><Goods /></el-icon>
								<div class="grid-cont-right">
									<div class="grid-num">{{dataSet.productNum}}</div>
									<div>商品数量</div>
								</div>
							</div>
						</el-card>
					</el-col>
				</el-row>
                <el-row :gutter="20" class="mgb20">
                    <el-col :span="8">
                        <el-card shadow="hover" :body-style="{ padding: '0px' }">
                            <div class="grid-content grid-con-4">
                                <el-icon class="grid-con-icon"><Star /></el-icon>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{dataSet.productNum}}</div>
                                    <div>收藏次数</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="8">
                        <el-card shadow="hover" :body-style="{ padding: '0px' }">
                            <div class="grid-content grid-con-5">
                                <el-icon class="grid-con-icon"><ShoppingCartFull /></el-icon>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{dataSet.cartNum}}</div>
                                    <div>加购次数</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="8">
                        <el-card shadow="hover" :body-style="{ padding: '0px' }">
                            <div class="grid-content grid-con-6">
                                <el-icon class="grid-con-icon"><Coin /></el-icon>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{dataSet.sumMoney}}</div>
                                    <div>交易金额</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>
			</el-col>
		</el-row>
		<el-row :gutter="20">
            <el-col :span="24">
                <el-card shadow="hover">
                    <schart ref="line" class="schart" canvasId="line" :options="options3"></schart>
                </el-card>
            </el-col>
<!--			<el-col :span="12">-->
<!--				<el-card shadow="hover">-->
<!--					<schart ref="bar" class="schart" canvasId="bar" :options="options1"></schart>-->
<!--				</el-card>-->
<!--			</el-col>-->
<!--			<el-col :span="12">-->
<!--				<el-card shadow="hover">-->
<!--					<schart ref="pie" class="schart" canvasId="pie" :options="options"></schart>-->
<!--				</el-card>-->
<!--			</el-col>-->
		</el-row>
	</div>
</template>

<script setup lang="ts" name="dashboard">
import Schart from 'vue-schart';
import { reactive } from 'vue';
import imgurl from '../assets/img/img.jpg';
import axios from "axios";

const name = localStorage.getItem('username');
const role: string = name === 'admin' ? '超级管理员' : '普通用户';


let options1 = reactive({
    type: 'bar',
    title: {
        text: '各类商品数量图'
    },
    xRorate: 25,
    labels: [],
    datasets: []
});

let options = reactive({
    type: 'pie',
    title: {
        text: '各类商品销售情况图'
    },
    legend: {
        position: 'left'
    },
    bgColor: '#fbfbfb',
    labels: [],
    datasets: []
})

const options3 = {
    type: 'line',
    title: {
        text: '14天内用户访问量统计',
    },
    bgColor: '#fbfbfb',
    labels: [
        '2024-3-1',
        '2024-3-2',
        '2024-3-3',
        '2024-3-4',
        '2024-3-5',
        '2024-3-6',
        '2024-3-7',
        '2024-3-8',
        '2024-3-9',
        '2024-3-10',
        '2024-3-11',
        '2024-3-12',
        '2024-3-13',
        '2024-3-14'
    ],
    datasets: [{
        data: [4, 2, 2, 9, 3, 5, 6, 3, 1, 4, 10, 3, 7, 5]
    }]
};

axios.get('http://localhost:6081/order/calOrderCategory').then(resp => {
    console.log(resp);
    if (resp.data.data != null) {
        options.labels = Object.keys(resp.data.data);
        console.log(options.labels);
        options.datasets.push({
            data: Object.values(resp.data.data)
        });
        console.log(options.datasets);
    }
}).catch(err => {
    console.log(err);
})

console.log(options.labels);
console.log(options.datasets);

axios.get('http://localhost:6081/product/calProduct').then(resp => {
    console.log(resp);
    if (resp.data.data != null) {
        options1.labels = Object.keys(resp.data.data);
        console.log(options1.labels);
        options1.datasets.push({
            data: Object.values(resp.data.data)
        });
        console.log(options1.datasets);
    }
}).catch(err => {
    console.log(err);
})

let dataSet = reactive({
    userNum: 0,
    orderNum: 0,
    productNum: 0,
    favoriteNum: 0,
    cartNum: 0,
    sumMoney: 0.0
})

axios.get('http://localhost:6081/user/count').then(resp => {
    console.log(resp);
    dataSet.userNum = resp.data.data;
}).catch(err => {
    console.log(err);
})

axios.get('http://localhost:6081/order/count').then(resp => {
    console.log(resp);
    dataSet.orderNum = resp.data.data;
}).catch(err => {
    console.log(err);
})

axios.get('http://localhost:6081/product/count').then(resp => {
    console.log(resp);
    dataSet.productNum = resp.data.data;
}).catch(err => {
    console.log(err);
})

axios.get('http://localhost:6081/favorite/count').then(resp => {
    console.log(resp);
    dataSet.favoriteNum = resp.data.data;
}).catch(err => {
    console.log(err);
})

axios.get('http://localhost:6081/cart/count').then(resp => {
    console.log(resp);
    dataSet.cartNum = resp.data.data;
}).catch(err => {
    console.log(err);
})

axios.get('http://localhost:6081/order/money').then(resp => {
    console.log(resp);
    dataSet.sumMoney = resp.data.data;
}).catch(err => {
    console.log(err);
})

</script>

<style scoped>
.el-row {
	margin-bottom: 20px;
}

.grid-content {
	display: flex;
	align-items: center;
	height: 100px;
}

.grid-cont-right {
	flex: 1;
	text-align: center;
	font-size: 14px;
	color: #999;
}

.grid-num {
	font-size: 30px;
	font-weight: bold;
}

.grid-con-icon {
	font-size: 50px;
	width: 100px;
	height: 100px;
	text-align: center;
	line-height: 100px;
	color: #fff;
}

.grid-con-1 .grid-con-icon {
	background: rgb(45, 140, 240);
}

.grid-con-1 .grid-num {
	color: rgb(45, 140, 240);
}

.grid-con-2 .grid-con-icon {
	background: rgb(100, 213, 114);
}

.grid-con-2 .grid-num {
	color: rgb(100, 213, 114);
}

.grid-con-3 .grid-con-icon {
	background: rgb(242, 94, 67);
}

.grid-con-3 .grid-num {
	color: rgb(242, 94, 67);
}

.grid-con-4 .grid-con-icon {
    background: rgb(244, 179, 63);
}

.grid-con-4 .grid-num {
    color: rgb(244, 179, 63);
}

.grid-con-5 .grid-con-icon {
    background: rgb(0, 200, 180);
}

.grid-con-5 .grid-num {
    color: rgb(0, 200, 180);
}

.grid-con-6 .grid-con-icon {
    background: rgb(200, 0, 255);
}

.grid-con-6 .grid-num {
    color: rgb(200, 0, 255);
}

.user-info {
	display: flex;
	align-items: center;
	padding-bottom: 20px;
	border-bottom: 2px solid #ccc;
	margin-bottom: 20px;
}

.user-info-cont {
	padding-left: 50px;
	flex: 1;
	font-size: 14px;
	color: #999;
}

.user-info-cont div:first-child {
	font-size: 30px;
	color: #222;
}

.user-info-list {
	font-size: 14px;
	color: #999;
	line-height: 25px;
}

.user-info-list span {
	margin-left: 70px;
}

.mgb20 {
	margin-bottom: 20px;
}

.schart {
	width: 100%;
	height: 300px;
}
</style>
