//修改这个变量为实际控制器的地址，如../showGoods.do
var reqpath = "search.html"
/*ajax获得的json对象*/
var typelist = [];
// 	{
// 		"id": "1",
// 		"parentId": "0",
// 		"name": "图书音像"
// 	}, {
// 		"id": "2",
// 		"parentId": "0",
// 		"name": "家用电器"
// 	}, {
// 		"id": "3",
// 		"parentId": "0",
// 		"name": "电脑办公"
// 	}, {
// 		"id": "4",
// 		"parentId": "0",
// 		"name": "个护化妆"
// 	}, {
// 		"id": "5",
// 		"parentId": "0",
// 		"name": "钟表"
// 	}, {
// 		"id": "6",
// 		"parentId": "0",
// 		"name": "母婴"
// 	}, {
// 		"id": "7",
// 		"parentId": "0",
// 		"name": "食品饮料"
// 	}, {
// 		"id": "8",
// 		"parentId": "0",
// 		"name": "汽车用品"
// 	}, {
// 		"id": "9",
// 		"parentId": "0",
// 		"name": "玩具乐器"
// 	}, {
// 		"id": "10",
// 		"parentId": "0",
// 		"name": "手机"
// 	}, {
// 		"id": "11",
// 		"parentId": "0",
// 		"name": "数码"
// 	}, {
// 		"id": "12",
// 		"parentId": "0",
// 		"name": "家居家装"
// 	}, {
// 		"id": "13",
// 		"parentId": "0",
// 		"name": "厨具"
// 	}, {
// 		"id": "14",
// 		"parentId": "0",
// 		"name": "服饰内衣"
// 	}, {
// 		"id": "15",
// 		"parentId": "0",
// 		"name": "鞋靴"
// 	}, {
// 		"id": "16",
// 		"parentId": "0",
// 		"name": "礼品箱包"
// 	}, {
// 		"id": "17",
// 		"parentId": "0",
// 		"name": "珠宝"
// 	}, {
// 		"id": "18",
// 		"parentId": "0",
// 		"name": "运动健康"
// 	}, {
// 		"id": "19",
// 		"parentId": "0",
// 		"name": "充值票务"
// 	}, {
// 		"id": "20",
// 		"parentId": "3",
// 		"name": "电脑整机"
// 	}, {
// 		"id": "21",
// 		"parentId": "3",
// 		"name": "电脑配件"
// 	}, {
// 		"id": "22",
// 		"parentId": "3",
// 		"name": "外设产品"
// 	}, {
// 		"id": "23",
// 		"parentId": "3",
// 		"name": "网络产品"
// 	}, {
// 		"id": "24",
// 		"parentId": "3",
// 		"name": "办公设备"
// 	}, {
// 		"id": "25",
// 		"parentId": "3",
// 		"name": "文具耗材"
// 	}, {
// 		"id": "26",
// 		"parentId": "3",
// 		"name": "服务产品"
// 	}, {
// 		"id": "27",
// 		"parentId": "20",
// 		"name": "笔记本"
// 	}, {
// 		"id": "28",
// 		"parentId": "20",
// 		"name": "超极本"
// 	}, {
// 		"id": "29",
// 		"parentId": "20",
// 		"name": "游戏本"
// 	}, {
// 		"id": "30",
// 		"parentId": "20",
// 		"name": "平板电脑"
// 	}, {
// 		"id": "31",
// 		"parentId": "20",
// 		"name": "平板电脑配件"
// 	}, {
// 		"id": "32",
// 		"parentId": "20",
// 		"name": "台式机"
// 	}, {
// 		"id": "33",
// 		"parentId": "20",
// 		"name": "服务器工作站"
// 	}, {
// 		"id": "34",
// 		"parentId": "20",
// 		"name": "笔记本配件"
// 	},
// ]
//加载json数据的到一级分类的方法
async function initMenu() {
	await axios.get('http://localhost:6081/product/category/list').then(resp=>{
		console.log("ok----------------------------------------")
		console.log(resp);
		typelist=resp.data.data;
	}).catch(err=>{
		console.log(err)
	});

	for (var i = 0; i < typelist.length; i++) {

		if (typelist[i].parentId == "0") {

			$(".index-menu").append($("<li data='" + typelist[i].id + "'>" + typelist[i].name + "</li>"))
		}
	}
}
$(function() {
	initMenu();

})


