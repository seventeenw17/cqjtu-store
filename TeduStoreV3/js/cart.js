document.write("<script src=\"../js/qs.js\" type=\"text/javascript\" charset=\"utf-8\"></script>");

var typelist = [];
var idList = [];
var token = localStorage.getItem("token");
var account = [];
var oderDetails = [];
var AddressList = [];

async function initCart() {
    //将异步代码设置为同步
    await axios.get('http://localhost:6081/cart/list', {
        headers: {
            Authorization: `${token}`,
        },
    }).then(resp => {
        console.log("success")
        console.log(resp);
        typelist = resp.data.data;
    }).catch(err => {
        console.log(err)
    });
    for (var i = 1; i <= typelist.length; i++) {
        $(".cart-body").append($("<tr>\n" +
            "<td><input type=\"checkbox\" class=\"ckitem\" id=\"check" + i + "\" onclick=\"checkOne()\" /></td>\n" +
            "<td><img src=\'" + typelist[i - 1].image + "\collect.png ' class=\"img-responsive\" /></td>\n" +
            "<td>" + typelist[i - 1].title +
            "</td>\n" +
            "<td>¥<span id=\"goodsPrice" + i + "\">" + typelist[i - 1].price + "</span></td>\n" +
            "<td>\n" +
            "<input type=\"button\" value=\"-\" class=\"num-btn\" onclick=\"reduceNum(" + i + ")\" />\n" +
            "<input id=\"goodsCount" + i + "\" type=\"text\" value=\" " + typelist[i - 1].num + " \" size=\"2\" readonly=\"readonly\" class=\"num-text\">\n" +
            "<input type=\"button\" value=\"+\" class=\"num-btn\" onclick=\"addNum(" + i + ")\" />\n" +
            "</td>\n" +
            "<td>¥<span id=\"goodsCast" + i + "\">" + typelist[i - 1].sumPrice + "</span></td>\n" +
            "<td><input type=\"button\" onclick=\"selDelCart()\" class=\"cart-del-sel btn btn-default btn-xs\" value=\"删除\" /></td>\n" +
            "</tr>"))
    }
}

/*按加号数量增*/
function addNum(rid) {
    var n = parseInt($("#goodsCount" + rid).val());
    $("#goodsCount" + rid).val(n + 1);
    calcRow(rid);
    modifyNum(rid, n + 1);
}

/*按减号数量减*/
function reduceNum(rid) {
    var n = parseInt($("#goodsCount" + rid).val());
    if (n == 0)
        return;
    $("#goodsCount" + rid).val(n - 1);
    calcRow(rid);
    modifyNum(rid, n - 1);
}

function modifyNum(rid, num) {//修改数量
    const id = typelist[rid - 1].id; //购物车id
    const productId = typelist[rid - 1].productId //产品id
    console.log(id);
    axios.put('http://localhost:6081/cart/updatenum', {
        id: id,
        productId: productId,
        num: num,
    }, {
        headers: {
            Authorization: `${token}`,
        }
    }).then(resp => {
        console.log(resp);
        if (resp.data.code === 20000) {
            console.log("操作成功")
        }
    }).catch(err => {
        console.log(err)
    });
}

/*全选全不选*/
function checkall(ckbtn) {
    $(".ckitem").prop("checked", $(ckbtn).prop("checked"));
    calcTotal();
}

function checkOne() {
    calcTotal();
}

/*//删除按钮
function delCartItem(btn, rid) {
    const id = typelist[rid - 1].id;
    // const productId = typelist[rid - 1].productId //产品id
    $(btn).parents("tr").remove();
    axios.delete('http://localhost:6081/cart/delete', {
        params: {
            idList: idList
        },
        //productId:productId,
        headers: {
            Authorization: `${token}`,
        }
    }).then(resp => {
        console.log(resp);
        if (resp.data.code === 20000) {
            console.log("操作成功")
        }
    }).catch(err => {
        console.log(err)
    });
    calcTotal();
}*/

//批量删除按钮
function selDelCart() {
    //遍历所有按钮
    for (var i = $(".ckitem").length - 1; i >= 0; i--) {
        //如果选中
        if ($(".ckitem")[i].checked) {
            //删除
            $($(".ckitem")[i]).parents("tr").remove();
            idList.push(typelist[i].id);
        }
    }
    console.log(idList);
    console.log(token);

    axios.delete("http://localhost:6081/cart/delete", {
        data: {
            "idList": idList
        },
        headers: {
            Authorization: `${token}`,
        }
    }).then(resp => {
        console.log(resp);
        if (resp.data.code === 20000) {
            console.log("操作成功")
        }
    }).catch(err => {
        console.log(err)
    });
    calcTotal();
}

//计算单行小计价格的方法
function calcRow(rid) {
    //取单价
    var vprice = parseFloat($("#goodsPrice" + rid).html());
    //取数量
    var vnum = parseFloat($("#goodsCount" + rid).val());
    //小计金额
    var vtotal = vprice * vnum;
    //赋值
    $("#goodsCast" + rid).html(vtotal);
}

//计算总价格的方法
function calcTotal() {
    //选中商品的数量
    var vselectCount = 0;
    //选中商品的总价
    var vselectTotal = 0;

    //循环遍历所有tr
    for (var i = 0; i < $(".cart-body tr").length; i++) {
        //计算每个商品的价格小计开始
        //取出1行
        var $tr = $($(".cart-body tr")[i]);
        //取单价
        var vprice = parseFloat($tr.children(":eq(3)").children("span").html());
        //取数量
        var vnum = parseFloat($tr.children(":eq(4)").children(".num-text").val());
        //小计金额
        var vtotal = vprice * vnum;
        //赋值
        $tr.children(":eq(5)").children("span").html(vtotal);
        //计算每个商品的价格小计结束

        //检查是否选中
        if ($tr.children(":eq(0)").children(".ckitem").prop("checked")) {
            //计数
            vselectCount++;
            //计总价
            vselectTotal += vtotal;
        }
        //将选中的数量和价格赋值
        $("#selectTotal").html(vselectTotal);
        $("#selectCount").html(vselectCount);
    }
}




  /*  //返回链接
    $(".link-account").click(function () {
        //循环遍历所有tr
        for (var i = 0; i < $(".cart-body tr").length; i++) {
            //计算每个商品的价格小计开始
            //取出1行
            var $tr = $($(".cart-body tr")[i]);

            //检查是否选中
            if ($tr.children(":eq(0)").children(".ckitem").prop("checked")) {
                const x = {
                    id: typelist[i].id,
                    productId: typelist[i].productId,
                    num: typelist[i].num,
                }
                account.push(x);
            }

        }
        console.log(account);


        if (account != null) {
            /!*var oid = 0;*!/
            axios.post("http://localhost:6081/cart/buy", account, {
                headers: {
                    Authorization: `${token}`,
                }
            }).then(resp => {
                console.log(token);
                console.log(resp);
                if (resp.data.code === 20000) {
                    console.log("操作成功")
                    /!*oid = resp.data.data;
                    console.log(oid);*!/
                    var url = "orderCreate.html";
                    //location.href = url;
                }
            }).catch(err => {
                console.log(err)
            });

        }*/


$(function () {
    //单选一个也得算价格
    $(".ckitem").click(function () {
        calcTotal();
    })
    //返回链接
    $(".link-account").click(function () {
        location.href = "orderCreate.html";
    })
    //开始时计算价格
    calcTotal();
    initCart();
})

function Account() {
    localStorage.removeItem("account2")
    var account2 = [];
    //循环遍历所有tr
    for (var i = 0; i < $(".cart-body tr").length; i++) {
        //计算每个商品的价格小计开始
        //取出1行
        var $tr = $($(".cart-body tr")[i]);

        //检查是否选中
        if ($tr.children(":eq(0)").children(".ckitem").prop("checked")) {
            /*const x = {
                id: typelist[i].id,
            }*/
            account2.push(typelist[i].id);

        }
    }
    localStorage.setItem("account2",JSON.stringify(account2))
    var a = JSON.parse(localStorage.getItem("account2"))
    console.log(a.length)
    axios.post("http://localhost:6081/order/creatOrder",account2,{
            headers: {
                Authorization: localStorage.getItem("token"),
            }
        }
    ).then(resp => {
        console.log(resp);
        if (resp.data.code == 20000) {
            console.log("操作成功")
        }
    }).catch(err => {
        console.log(err)
    });

}
