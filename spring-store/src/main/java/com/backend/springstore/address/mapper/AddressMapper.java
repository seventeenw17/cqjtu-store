package com.backend.springstore.address.mapper;

import com.backend.springstore.address.pojo.entity.Address;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressMapper {
    List<Address> getAddressListByUserId(Integer userId);
    Address getAddressById(Integer id);
    int deleteAddressById(Integer id);
    int setIsNotDefaultAllByUserId(Integer userId);
    int setIsDefaultById(@Param("id") Integer id, @Param("modifiedUser") String modifiedUser);
    int countAddressByUserId(Integer userId);
    int addAddress(Address address);
    int updateAddress(Address updateAddress);
}
