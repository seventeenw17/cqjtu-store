<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="24">
                <el-card class="chart-card">
                    <schart ref="bar" class="schart" canvasId="bar" :options=options1></schart>
                </el-card>
                <el-card class="chart-card">
                    <schart ref="pie" class="schart" canvasId="pie" :options=options></schart>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script setup lang="ts" name="statistics">
import Schart from 'vue-schart';
import axios from "axios";
import { reactive } from "vue";

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

console.log("o1"+options1.labels);
console.log("o1"+options1.datasets);

</script>

<style scoped>
.chart-card {
    margin-top: 20px;
    margin-bottom: 20px;
}
.schart {
    width: 100%;
    height: 300px;
}
</style>