package com.backend.springstore.order.service.impl;

import com.backend.springstore.address.mapper.AddressMapper;
import com.backend.springstore.address.pojo.entity.Address;
import com.backend.springstore.common.ServiceCode;
import com.backend.springstore.common.ServiceException;
import com.backend.springstore.page.PageDTO;
import com.backend.springstore.page.PageVO;
import com.backend.springstore.order.mapper.OrderMapper;
import com.backend.springstore.order.pojo.dto.CreatOrderDTO;
import com.backend.springstore.order.pojo.dto.NoPaidOrderDTO;
import com.backend.springstore.order.pojo.dto.OrderPaidDTO;
import com.backend.springstore.order.pojo.dto.UpdateOrderAddressDTO;
import com.backend.springstore.order.pojo.entity.Order;
import com.backend.springstore.order.pojo.entity.OrderItem;
import com.backend.springstore.order.pojo.vo.OrderManageVO;
import com.backend.springstore.order.pojo.vo.OrderItemVO;
import com.backend.springstore.order.pojo.vo.OrderVO;
import com.backend.springstore.order.service.OrderService;
import com.backend.springstore.product.mapper.ProductCategoryMapper;
import com.backend.springstore.product.mapper.ProductMapper;
import com.backend.springstore.user.mapper.UserMapper;
import com.backend.springstore.user.pojo.entity.User;
import com.backend.springstore.product.pojo.entity.Product;
import com.backend.springstore.product.pojo.entity.ProductCategory;
import com.backend.springstore.user.pojo.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @ProjectName: cqjtu-store
 * @Titile: OrderServiceImpl
 * @Author: Lucky
 * @Description: 订单业务类
 */
@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public List<OrderVO> getList(UserLoginVO userLoginVO) {
        List<OrderVO> orderList = orderMapper.getOrderList(userLoginVO.getId());
        log.debug("订单列表：{}", orderList);
        if(orderList == null){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"此用户没有订单数据");
        }
        return orderList;
    }

    @Override
    public void orderPaid(OrderPaidDTO orderPaidDTO, UserLoginVO userLoginVO) {
        Order order =  orderMapper.getOrderById(orderPaidDTO.getId());
        if(order == null){
            throw new ServiceException(ServiceCode.ERROR_FORBIDDEN,"此订单不存在");
        }
        if(!Objects.equals(order.getUserId(), userLoginVO.getId())){
            throw new ServiceException(ServiceCode.ERROR_UNAUTHORIZED_DISABLED,"你没权限操作此订单");
        }
        if (order.getStatus()!=0){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"此订单不是未支付状态");
        }
        //是否被删除
        if (order.getIsDelete() == 1){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"此订单已删除");
        }
        //改变状态
        order.setStatus(1);
        order.setModifiedUser(userLoginVO.getUsername());

        int count = orderMapper.updateOrderPaid(order);

        if (count != 1){
            throw new ServiceException(ServiceCode.ERROR_UPDATE_FAILED,"设置支付状态失败");
        }
    }

    @Override
    public void takeDelivery(OrderPaidDTO orderPaidDTO, UserLoginVO userLoginVO) {
        Order order = orderMapper.getOrderById(orderPaidDTO.getId());
        if(order == null){
            throw new ServiceException(ServiceCode.ERROR_FORBIDDEN,"此订单不存在");
        }
        if(!Objects.equals(order.getUserId(), userLoginVO.getId())){
            throw new ServiceException(ServiceCode.ERROR_UNAUTHORIZED_DISABLED,"你没权限操作此订单");
        }
        if (order.getStatus()!=1){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"此订单不是待收货状态");
        }
        //是否被删除
        if (order.getIsDelete() == 1){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"此订单已删除");
        }

        order.setStatus(2);
        order.setModifiedUser(userLoginVO.getUsername());

        int count = orderMapper.updateOrderTakeDelivery(order);
        if(count != 1){
            throw new ServiceException(ServiceCode.ERROR_UPDATE_FAILED,"设置收货状态失败");
        }
    }

    @Override
    public void orderSaleService(OrderPaidDTO orderPaidDTO, UserLoginVO userLoginVO) {
        Order order = orderMapper.getOrderById(orderPaidDTO.getId());
        if(order == null){
            throw new ServiceException(ServiceCode.ERROR_FORBIDDEN,"此订单不存在");
        }
        if(!Objects.equals(order.getUserId(), userLoginVO.getId())){
            throw new ServiceException(ServiceCode.ERROR_UNAUTHORIZED_DISABLED,"你没权限操作此订单");
        }
        if (order.getStatus()!=2){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"此订单不是已收货状态");
        }
        //是否被删除
        if (order.getIsDelete() == 1){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"此订单已删除");
        }

        order.setStatus(3);
        order.setModifiedUser(userLoginVO.getUsername());

        int count = orderMapper.updateOrderTakeDelivery(order);
        if(count != 1){
            throw new ServiceException(ServiceCode.ERROR_UPDATE_FAILED,"设置订单状态失败");
        }
    }

    @Override
    public List<OrderVO> getNoPaidList(UserLoginVO userLoginVO) {
        NoPaidOrderDTO noPaidOrderDTO = new NoPaidOrderDTO();
        noPaidOrderDTO.setUserId(userLoginVO.getId());
        noPaidOrderDTO.setStatus(0);

        List<OrderVO> orderList = orderMapper.getNoPaidOrderList(noPaidOrderDTO);
        if(orderList == null){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"此用户没有未付款订单数据");
        }
        return orderList;
    }

    @Override
    public List<OrderVO> getNotArrivedList(UserLoginVO userLoginVO) {
        NoPaidOrderDTO noPaidOrderDTO = new NoPaidOrderDTO();
        noPaidOrderDTO.setUserId(userLoginVO.getId());
        noPaidOrderDTO.setStatus(1);

        List<OrderVO> orderList = orderMapper.getNoPaidOrderList(noPaidOrderDTO);
        if(orderList == null){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"此用户没有未到达订单数据");
        }
        return orderList;
    }

    @Override
    public List<OrderVO> getReceivedList(UserLoginVO userLoginVO) {
        NoPaidOrderDTO noPaidOrderDTO = new NoPaidOrderDTO();
        noPaidOrderDTO.setUserId(userLoginVO.getId());
        noPaidOrderDTO.setStatus(2);

        List<OrderVO> orderList = orderMapper.getNoPaidOrderList(noPaidOrderDTO);
        if(orderList == null){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"此用户没有已收货订单数据");
        }
        return orderList;
    }

    @Override
    public List<OrderVO> getSaleServiceList(UserLoginVO userLoginVO) {
        NoPaidOrderDTO noPaidOrderDTO = new NoPaidOrderDTO();
        noPaidOrderDTO.setUserId(userLoginVO.getId());
        noPaidOrderDTO.setStatus(3);

        List<OrderVO> orderList = orderMapper.getNoPaidOrderList(noPaidOrderDTO);
        if(orderList == null){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"此用户没有正在售后服务订单数据");
        }
        return orderList;
    }

    @Override
    public void creatOrder(CreatOrderDTO creatOrderDTO, UserLoginVO userLoginVO) {

        if (userLoginVO == null) {
            throw new ServiceException(ServiceCode.ERROR_FORBIDDEN, "用户未登录！");
        }
        if (creatOrderDTO == null) {
            throw new ServiceException(ServiceCode.ERROR_FORBIDDEN, "无效的数据传入！");
        }
        System.out.println(creatOrderDTO);
        if (creatOrderDTO.getCreatOrderItemDTOList() == null) {
            throw new ServiceException(ServiceCode.ERROR_FORBIDDEN, "订单明细不能为空！");
        }
        int size = creatOrderDTO.getCreatOrderItemDTOList().size();
        Order order = new Order();
        order.setUserId(userLoginVO.getId());
        order.setRecvName(creatOrderDTO.getRecvName());
        order.setRecvPhone(creatOrderDTO.getRecvPhone());
        order.setRecvProvince(creatOrderDTO.getRecvProvince());
        order.setRecvCity(creatOrderDTO.getRecvCity());
        order.setRecvArea(creatOrderDTO.getRecvArea());
        order.setRecvAddress(creatOrderDTO.getRecvAddress());
        order.setStatus(0);//表示未付款

        double totalPrice=0;//计算订单总价
        for (int i = 0;i <size;i++){
            int num = creatOrderDTO.getCreatOrderItemDTOList().get(i).getNum();
            if(num > productMapper.getProductById(creatOrderDTO.getCreatOrderItemDTOList().get(i).getProductId()).getNum()){
                throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"库存数量不足");
            }
            totalPrice += num* productMapper.getProductById(creatOrderDTO.getCreatOrderItemDTOList().get(i).getProductId()).getPrice();
        }
        order.setUserId(userLoginVO.getId());
        order.setTotalPrice((long) totalPrice);
        order.setIsDelete(0);//默认没有删除
        order.setCreatedUser(userLoginVO.getUsername());

        int count = orderMapper.creatOrder(order);
        if (count != 1||order.getId()==null){
            throw new ServiceException(ServiceCode.ERROR_IS_ASSOCIATED,"创建订单失败");
        }

        for (int i = 0;i <size;i++) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(creatOrderDTO.getCreatOrderItemDTOList().get(i).getProductId());
            orderItem.setNum(creatOrderDTO.getCreatOrderItemDTOList().get(i).getNum());
            //Double转Integer
            orderItem.setPrice(productMapper.getProductById(creatOrderDTO.getCreatOrderItemDTOList().get(i).getProductId()).getPrice().intValue());
            orderItem.setImage(productMapper.getProductById(creatOrderDTO.getCreatOrderItemDTOList().get(i).getProductId()).getImage());
            orderItem.setTitle(productMapper.getProductById(creatOrderDTO.getCreatOrderItemDTOList().get(i).getProductId()).getTitle());
            orderItem.setCreatedUser(userLoginVO.getUsername());
            int c = orderMapper.creatOrderItem(orderItem);
            if (c != 1){
                throw new ServiceException(ServiceCode.ERROR_IS_ASSOCIATED,"创建订单失败");
            }
        }
    }

    @Override
    public PageVO<List<OrderManageVO>> getOrderList(PageDTO pageDTO, UserLoginVO userLoginVO) {
        User user = userMapper.getUserByName(userLoginVO.getUsername());
        if(user == null){
            throw new ServiceException(ServiceCode.ERROR_EXISTS,"用户不存在");
        }
        if( !user.getUsername().equals("admin")){
            throw new ServiceException(ServiceCode.ERROR_BAD_REQUEST,"用户没有管理员权限");
        }
        List<OrderManageVO> orderManageVOList = orderMapper.getAllOrderList(pageDTO);
        Integer count = orderMapper.countList();
        if(count == 0){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"没有查询到订单");
        }
        PageVO pageVO = new PageVO<>(pageDTO.getPageIndex(),pageDTO.getPageSize(),count,orderManageVOList);
        return pageVO;
    }
    /*删除订单
     * */
    @Override
    public void deleteOrder(Integer orderId, UserLoginVO userLoginVO) {
        User user = userMapper.getUserByName(userLoginVO.getUsername());
        if(user == null){
            throw new ServiceException(ServiceCode.ERROR_EXISTS,"用户不存在");
        }
        if( !user.getUsername().equals("admin")){
            throw new ServiceException(ServiceCode.ERROR_BAD_REQUEST,"用户没有管理员权限");
        }
        Integer result1 = orderMapper.deleteOrder(orderId);
        Integer result2 = orderMapper.deleteOrderItem(orderId);
        if(result1 == 0){
            throw new ServiceException(ServiceCode.ERROR_DELETE_FAILED,"删除数据失败");
        }
    }

    @Override
    public List<OrderManageVO> getUserOrderList(String userName, UserLoginVO userLoginVO) {
        User admin = userMapper.getUserByName(userLoginVO.getUsername());
        if(userName==null){
            throw new ServiceException(ServiceCode.ERROR_BAD_REQUEST,"请求数据为空");
        }
        User user = userMapper.getUserByName(userName);
        if(user == null){
            throw new ServiceException(ServiceCode.ERROR_EXISTS,"用户不存在");
        }
        if( !admin.getUsername().equals("admin")){
            throw new ServiceException(ServiceCode.ERROR_BAD_REQUEST,"用户没有管理员权限");
        }
        List<OrderManageVO> orderManageVOList = orderMapper.getUserOrderList(user.getId());
        if(orderManageVOList.size()==0){
            throw new ServiceException(ServiceCode.ERROR_EXISTS,"该用户没有订单存在");
        }
        return orderManageVOList;
    }

    @Override
    public OrderManageVO getOrderByOrderId(String orderId, UserLoginVO userLoginVO) {
        User admin = userMapper.getUserByName(userLoginVO.getUsername());
        if(orderId==null){
            throw new ServiceException(ServiceCode.ERROR_BAD_REQUEST,"请求数据为空");
        }
        if( !admin.getUsername().equals("admin")){
            throw new ServiceException(ServiceCode.ERROR_BAD_REQUEST,"用户没有管理员权限");
        }
        OrderManageVO manageVO = orderMapper.getOrderBuOrderId(orderId);
        if(manageVO.equals(null)){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"没有找到该订单");
        }
        return manageVO;
    }

    @Override
    public void updateOrderAddress(UpdateOrderAddressDTO updateOrderAddressDTO,UserLoginVO userLoginVO) {
        User admin = userMapper.getUserByName(userLoginVO.getUsername());
        if( !admin.getUsername().equals("admin")){
            throw new ServiceException(ServiceCode.ERROR_BAD_REQUEST,"用户没有管理员权限");
        }
        if(updateOrderAddressDTO.getOrderId() == null||updateOrderAddressDTO.getAddressId() == null){
            throw new ServiceException(ServiceCode.ERROR_BAD_REQUEST,"请求数据为空");
        }
        Address address = addressMapper.getAddressById(updateOrderAddressDTO.getAddressId());
        Order order = orderMapper.getOrderById(updateOrderAddressDTO.getOrderId());
        if(address == null){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"对应地址不存在");
        }
        if(order == null){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND,"对应订单不存在");
        }
        order.setRecvProvince(address.getProvinceName());
        order.setRecvCity(address.getCityName());
        order.setRecvArea(address.getAreaName());
        order.setRecvAddress(address.getAddressDetail());
        order.setModifiedUser(userLoginVO.getUsername());
        Integer result = orderMapper.updateOrderAddress(order);
        System.out.println("---------------------------------------------------"+result);
        if(result != 1){
            throw new ServiceException(ServiceCode.ERROR_UPDATE_FAILED,"修改失败");
        }
    }


    public List<OrderVO> getAllOrderVO() {
        List<OrderVO> orderVOList = orderMapper.getAllList();
        return orderVOList;
    }

    @Override
    public Map<String, Integer> calOrderCategory() {
        Map<String, Integer> map = new HashMap<>();
        List<OrderVO> orderVOList = new ArrayList<>();
        // 获取全部订单
        orderVOList = getAllOrderVO();
        for (OrderVO orderVO: orderVOList) {
            List<OrderItemVO> orderItemVOList = orderVO.getOrderItemVOList();
            for (OrderItemVO orderItemVO: orderItemVOList) {
                Product product = productMapper.getProductById(orderItemVO.getProductId());
                System.out.println("商品" + orderItemVO.getProductId() + "  " + product);
                Integer ancestorCategoryId = product.getCategoryId();
                Integer num = orderItemVO.getNum();
                /*while (!productCategoryMapper.getParentIdInteger(ancestorCategoryId).equals(0)) {
                    ancestorCategoryId = productCategoryMapper.getParentIdInteger(ancestorCategoryId);
                }*/
                ProductCategory productCategory = productCategoryMapper.getCategoryById(ancestorCategoryId);

                String categoryName = productCategory.getName();
                map.merge(categoryName, num, Integer::sum);
            }
        }
        return map;
    }

    @Override
    public Integer countOrder() {
        Integer cnt = orderMapper.countOrder();
        return cnt;
    }

    @Override
    public Double sumMoney() {
        List<Double> priceList = orderMapper.getPriceList();
        Double ans = 0.0;
        for (Double price: priceList) {
            ans += price;
        }
        return ans;
    }
}
