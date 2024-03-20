package com.backend.springstore.order.controller;

import com.backend.springstore.cart.mapper.CartMapper;
import com.backend.springstore.cart.pojo.entity.Cart;
import com.backend.springstore.security.JWTUtils;
import com.backend.springstore.common.Result;
import com.backend.springstore.page.PageDTO;
import com.backend.springstore.page.PageVO;
import com.backend.springstore.order.pojo.dto.CreatOrderDTO;
import com.backend.springstore.order.pojo.dto.CreatOrderItemDTO;
import com.backend.springstore.order.pojo.dto.OrderPaidDTO;
import com.backend.springstore.order.pojo.dto.UpdateOrderAddressDTO;
import com.backend.springstore.order.pojo.vo.OrderManageVO;
import com.backend.springstore.order.pojo.vo.OrderVO;
import com.backend.springstore.order.service.OrderService;
import com.backend.springstore.product.mapper.ProductMapper;
import com.backend.springstore.product.pojo.entity.Product;
import com.backend.springstore.user.pojo.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("order")
@Slf4j
@Api(tags = "订单模块")
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    @GetMapping("list")
    @ApiOperation("获取订单列表")

    public Result<List<OrderVO>> getOrderList(HttpServletRequest request) {
        // 获取用户
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        log.debug("用户信息：{}", userLoginVO);
        System.out.println(userLoginVO);
        List<OrderVO> orderVOList = orderService.getList(userLoginVO);
        return Result.ok(orderVOList);
    }

    /**
     * 完成支付接口
     * @param orderPaidDTO 订单信息
     * @param request token
     * @return
     */
    @PutMapping("paid")
    public Result<Void> orderPaid(@RequestBody  @Validated OrderPaidDTO orderPaidDTO, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        orderService.orderPaid(orderPaidDTO,userLoginVO);
        return Result.ok();
    }

    @PutMapping("takeDelivery")
    public Result<Void> takeDelivery(@RequestBody @Validated OrderPaidDTO orderPaidDTO, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        orderService.takeDelivery(orderPaidDTO,userLoginVO);
        return Result.ok();
    }

    @PutMapping("saleService")
    public Result<Void> orderSaleService(@RequestBody @Validated OrderPaidDTO orderPaidDTO,HttpServletRequest request){
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        orderService.orderSaleService(orderPaidDTO,userLoginVO);
        return Result.ok();
    }

    /**
     * 查询未付款订单
     * @param request token
     * @return
     */
    @GetMapping("noPaidList")
    public Result<List<OrderVO>> noPaidList(HttpServletRequest request){
        // 获取用户
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);

        List<OrderVO> orderVOList = orderService.getNoPaidList(userLoginVO);
        return Result.ok(orderVOList);
    }

    /**
     * 查询未到达订单
     * @param request
     * @return
     */
    @GetMapping("notArrivedList")
    public Result<List<OrderVO>> notArrivedList(HttpServletRequest request){
        // 获取用户
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);

        List<OrderVO> orderVOList = orderService.getNotArrivedList(userLoginVO);
        return Result.ok(orderVOList);
    }

    @GetMapping("receivedList")
    public Result<List<OrderVO>> receivedList(HttpServletRequest request){
        // 获取用户
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);

        List<OrderVO> orderVOList = orderService.getReceivedList(userLoginVO);
        return Result.ok(orderVOList);
    }

    @GetMapping("saleServiceList")
    public Result<List<OrderVO>> saleServiceList(HttpServletRequest request){
        // 获取用户
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);

        List<OrderVO> orderVOList = orderService.getSaleServiceList(userLoginVO);
        return Result.ok(orderVOList);
    }

    @PostMapping("creatOrder")
    public Result<Void> creatOrder(@RequestBody Integer[] id,HttpServletRequest request){
        CreatOrderDTO creatOrderDTO = new CreatOrderDTO();
        List<CreatOrderItemDTO>creatOrderItemDTOList = new ArrayList<>();
        int sum = id.length;
        for (int i = 0;i<sum;i++){
            System.out.println(3453454);
            System.out.println(id[i]);
            Cart cart = cartMapper.getCartById(id[i]);
            Product product = productMapper.getProductById(cart.getProductId());
            int num = product.getNum();
            CreatOrderItemDTO creatOrderItemDTO = new CreatOrderItemDTO();
            creatOrderItemDTO.setNum(num);
            creatOrderItemDTO.setProductId(product.getId());
            creatOrderItemDTOList.add(creatOrderItemDTO);
        }
        creatOrderDTO.setCreatOrderItemDTOList(creatOrderItemDTOList);


        log.info("66666:{}",creatOrderDTO.toString());
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        log.debug("订单信息：{}", creatOrderDTO);
        orderService.creatOrder(creatOrderDTO,userLoginVO);
        return Result.ok();
    }

    /*获取全部用户订单信息
     * @param request token
     * */
    @PostMapping("allList")
    @ApiOperation("获取全部订单列表")
    public Result<PageVO<List<OrderManageVO>>> getOrderList(@RequestBody PageDTO pageDTO, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        log.debug("管理员信息:{}",userLoginVO);
        PageVO<List<OrderManageVO>> listPageVO = orderService.getOrderList(pageDTO,userLoginVO);

        return Result.ok(listPageVO);
    }
    /*获取全部用户订单信息
     * @param request token
     * */
    @DeleteMapping("deleteOrder/{orderId}")
    @ApiOperation("删除订单")
    public Result<Void> deleteOrder(@PathVariable Integer orderId, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        log.debug("管理员信息:{}",userLoginVO);
        orderService.deleteOrder(orderId,userLoginVO);
        return Result.ok();
    }
    /*修改订单地址
     * */
    @PutMapping("updateOrder")
    @ApiOperation("修改订单地址")
    public Result<Void> updateOrder(@RequestBody UpdateOrderAddressDTO updateOrderAddressDTO, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        log.debug("管理员信息:{}",userLoginVO);
        orderService.updateOrderAddress(updateOrderAddressDTO,userLoginVO);
        return Result.ok();
    }
    /*获取全部用户订单信息
     * @param request token
     * */
    @GetMapping("getOrderListByName/{userName}")
    @ApiOperation("根据用户名查找订单列表")
    public Result<List<OrderManageVO>> getOrderListByName(@PathVariable String userName, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        log.debug("管理员信息:{}",userLoginVO);
        List<OrderManageVO> orderManageVOList = orderService.getUserOrderList(userName,userLoginVO);

        return Result.ok(orderManageVOList);
    }
    @GetMapping("getOrderListById/{orderId}")
    @ApiOperation("根据订单编号查找订单")
    public Result<OrderManageVO> getOrderByOrderId(@PathVariable String orderId, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        UserLoginVO userLoginVO = JWTUtils.getUserInfo(token);
        log.debug("管理员信息:{}", userLoginVO);
        OrderManageVO orderManageVO = orderService.getOrderByOrderId(orderId, userLoginVO);
        return Result.ok(orderManageVO);
    }
    @GetMapping("calOrderCategory")
    public Result<Map<String, Integer>> getOrderCategory() {
        Map<String, Integer> map = orderService.calOrderCategory();
        return Result.ok(map);
    }

    @GetMapping("count")
    public Result<Integer> countOrder() {
        Integer cnt = orderService.countOrder();
        return Result.ok(cnt);
    }

    @GetMapping("money")
    public Result<Double> sumMoney() {
        Double ans = orderService.sumMoney();
        return Result.ok(ans);
    }
}
