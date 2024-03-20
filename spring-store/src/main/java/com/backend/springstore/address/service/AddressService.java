package com.backend.springstore.address.service;

import com.backend.springstore.address.pojo.dto.AddressDTO;
import com.backend.springstore.address.pojo.entity.Address;
import com.backend.springstore.address.pojo.vo.AddressUpdateVO;
import com.backend.springstore.address.pojo.vo.AddressVO;
import com.backend.springstore.user.pojo.vo.UserLoginVO;

import java.util.List;

public interface AddressService {
    List<AddressVO> getAddressList(UserLoginVO userLoginVO);
    void deleteAddress(Integer id, UserLoginVO userLoginVO);
    void setDefaultAddress(Integer id, UserLoginVO userLoginVO);
    void addAddress(AddressDTO addressDTO, UserLoginVO userLoginVO);
    AddressUpdateVO getAddressById(Integer id, UserLoginVO userLoginVO);
    void updateAddress(AddressDTO addressDTO, UserLoginVO userLoginVO);
    List<AddressVO> managerGetAddressList(Integer userId, UserLoginVO userLoginVO);
}
