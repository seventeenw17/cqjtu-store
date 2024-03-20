

var token = localStorage.getItem("token");
var AddressList = [];
var orderDetails = [];
var status = 0;

async function initOrderSite() {
    await axios.get('http://localhost:6081/address/list', {
        headers: {
            Authorization: `${token}`,
        },
    }).then(resp => {
        console.log(resp);
        AddressList = resp.data.data;
        console.log("成功");

    }).catch(err => {
        console.log(err)
    });
    for (var i = 1; i <= AddressList.length; i++) {
        $(".form-control").append($("<option>" +  AddressList[i - 1].name+"  /  "+AddressList[i - 1].tag+"  /  "+AddressList[i - 1].addressDetail +"  /  "+AddressList[i-1].phone+ "</option>"))
    }
}

async function initOrder() {
    await axios.get('http://localhost:6081/order/noPaidList', {
        params: {
            status: status
        },
        headers: {
            Authorization: `${token}`,
        },
    }).then(resp => {
        orderDetails = resp.data.data;
    }).catch(err => {
        console.log(err)
    });
    var vselectCount = 0;
    var vselectTotal = 0;
    for (var i = 1; i <= orderDetails.length; i++) {
        const orderItem = orderDetails[i - 1].orderItemVOList;
        vselectCount = vselectCount + orderItem.length;
        for (var j = 1; j <= orderItem.length; j++) {
            $(".cart-body").append($("<tr>\n" +
                "<td><img src=\'" + orderItem[j - 1].image + "\collect.png ' class=\"img-responsive\" /></td>\n" +
                "<td>" + orderItem[j - 1].title + "</td>\n" +
                "<td>¥<span>" + orderItem[j - 1].price + "</span></td>\n" +
                "<td>" + orderItem[j - 1].num + "</td>\n" +
                "<td>¥<span>" + orderItem[j - 1].total + "</span></td>\n" +
                "</tr>"))
            vselectTotal = vselectTotal + orderItem[j - 1].total;
        }
    }

    $("#selectTotal").html(vselectTotal);
    $("#selectCount").html(vselectCount);
    $(".payment").append("订单号：" + orderDetails[0].id + "，支付金额：¥" + vselectTotal + "，收款方达内学子商城")
    $(".lll").append("¥" + vselectTotal)
}

function onlinePay() {
    var obj = document.getElementById("mySelect");
    var index = obj.selectedIndex; //序号，取当前选中选项的序号
    var aid = AddressList[index].id;
    var oid = orderDetails[0].id;
    axios.put('http://localhost:6081/cart/buy', {
        aid: aid,
        oid: oid,
    }, {
        headers: {
            Authorization: `${token}`,
        }
    }).then(resp => {
        console.log(resp);
        if (resp.data.code === 20000) {
            console.log("成功");
        }
    }).catch(err => {
        console.log(err)
    });
}



$(function () {
    initOrderSite();
    initOrder();
    $(".link-pay").click(function () {
        location.href = "payment.html";
    })
})
$(function () {
    $(".link-success").click(function () {
        location.href = "paySuccess.html";
    })
})