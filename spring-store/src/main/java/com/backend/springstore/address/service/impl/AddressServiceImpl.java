package com.backend.springstore.address.service.impl;

import com.backend.springstore.address.mapper.AddressMapper;
import com.backend.springstore.address.mapper.DistrictMapper;
import com.backend.springstore.address.pojo.dto.AddressDTO;
import com.backend.springstore.address.pojo.entity.Address;
import com.backend.springstore.address.pojo.entity.District;
import com.backend.springstore.address.pojo.vo.AddressUpdateVO;
import com.backend.springstore.address.pojo.vo.AddressVO;
import com.backend.springstore.address.service.AddressService;
import com.backend.springstore.common.Constants;
import com.backend.springstore.common.ServiceCode;
import com.backend.springstore.common.ServiceException;
import com.backend.springstore.user.pojo.entity.User;
import com.backend.springstore.user.pojo.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private DistrictMapper districtMapper;
    /**
     * 获取地址列表
     */
    @Override
    public List<AddressVO> getAddressList(UserLoginVO userLoginVO) {
        // 获取列表
        List<Address> addressList = addressMapper.getAddressListByUserId(userLoginVO.getId());
        if (addressList.isEmpty()){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "该用户没有地址！");
        }
        // 将实体类封装的数据转成VO供控制层调用
        List<AddressVO> addressVOList = new ArrayList<>();
        addressList.forEach(address -> {
            AddressVO addressVO = new AddressVO();
            addressVO.setId(address.getId());
            addressVO.setName(address.getName());
            addressVO.setPhone(address.getPhone());
            addressVO.setTag(address.getTag());
            addressVO.setIsDefault(address.getIsDefault());
            // 详细地址：省份 + 城市 + 区县 + 小区编号
            addressVO.setAddressDetail(address.getProvinceName()
                    + address.getCityName()
                    + address.getAreaName()
                    + address.getAddressDetail());
            addressVOList.add(addressVO);
        });
        log.debug("地址信息：{}", addressList);
        return addressVOList;
    }

    /**
     * 删除地址
     */
    @Override
    public void deleteAddress(Integer id, UserLoginVO userLoginVO) {
        Address address = addressMapper.getAddressById(id);
        // 判断地址是否存在
        if (address == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "该地址不存在！");
        }
        // 判断地址是否属于当前用户
        if (!Objects.equals(address.getUserId(), userLoginVO.getId())) {
            throw new ServiceException(ServiceCode.ERROR_FORBIDDEN, "无操作权限！");
        }
        // 删除数据
        int res = addressMapper.deleteAddressById(id);
        if (res != 1) {
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED, "删除失败！");
        }
    }

    /**
     * 设置默认地址
     */
    @Override
    public void setDefaultAddress(Integer id, UserLoginVO userLoginVO) {
        // 判断ID是否为空
        if (id == null) {
            throw new ServiceException(ServiceCode.ERROR_BAD_REQUEST, "请传入地址编号！");
        }
        Address address = addressMapper.getAddressById(id);
        // 判断地址是否存在
        if (address == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "该地址不存在！");
        }
        // 判断地址是否属于当前用户
        if (!Objects.equals(address.getUserId(), userLoginVO.getId())) {
            throw new ServiceException(ServiceCode.ERROR_FORBIDDEN, "无操作权限！");
        }
        // 判断地址是否已经是默认地址
        if (address.getIsDefault() == 1) {
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED, "该地址已为默认地址！");
        }
        // 将该用户的默认地址取消
        addressMapper.setIsNotDefaultAllByUserId(userLoginVO.getId());
        // 设为默认地址，原默认地址取消
        int res = addressMapper.setIsDefaultById(id, userLoginVO.getUsername());
        if (res == 0) {
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED, "设置默认地址失败！");
        }
    }

    /**
     * 保存地址信息
     */
    @Override
    public void addAddress(AddressDTO addressDTO, UserLoginVO userLoginVO) {
        // 限制每个用户最多5条地址
        // 判断地址数量
        int count = addressMapper.countAddressByUserId(userLoginVO.getId());
        if (count == Constants.MAX_ADDRESS_ITEMS_COUNT) {
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED,
                    "新增地址失败，每个用户地址条目最多为" + Constants.MAX_ADDRESS_ITEMS_COUNT+ "条！");
        }
        // 封装地址
        Address address = new Address();
        address.setName(addressDTO.getName());
        // 确定地址编号与名称对应
        // 根据地址编号去查询地址信息，判断地址对象中的地址名称跟传递过来的名称已否一致

        District province = districtMapper.getDistrictByCode(addressDTO.getProvinceCode());
        // 省/直辖市编码是否存在
        if (province == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "省/直辖市编码不存在！");
        }
        // 省/直辖市编码所对的省份名是否正确
        if (!province.getName().equals(addressDTO.getProvinceName())) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "省/直辖市编码与名称对应错误！");
        }

        District city = districtMapper.getDistrictByCode(addressDTO.getCityCode());
        // 城市编码是否存在
        if (city == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "城市编码不存在！");
        }
        // 城市编码所对的城市名是否正确
        if (!city.getName().equals(addressDTO.getCityName())) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "城市编码与名称对应错误！");
        }
        // 是否属于该省/直辖市
        if (!city.getParent().equals(province.getCode())) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "城市与省/直辖市对应错误！");
        }

        District area = districtMapper.getDistrictByCode(addressDTO.getAreaCode());
        // 区域编码是否存在
        if (area == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "区域编码不存在！");
        }
        // 区域编码所对的区域名是否正确
        if (!area.getName().equals(addressDTO.getAreaName())) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "区域编码与名称对应错误！");
        }
        // 是否属于该城市
        if (!area.getParent().equals(city.getCode())) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "区域与城市对应错误！");
        }

        address.setProvinceCode(addressDTO.getProvinceCode());
        address.setProvinceName(addressDTO.getProvinceName());
        address.setCityCode(addressDTO.getCityCode());
        address.setCityName(addressDTO.getCityName());
        address.setAreaCode(addressDTO.getAreaCode());
        address.setAreaName(addressDTO.getAreaName());
        address.setAddressDetail(addressDTO.getAddressDetail());
        address.setZip(addressDTO.getZip());
        address.setPhone(addressDTO.getPhone());
        address.setTel(addressDTO.getTel());
        address.setTag(addressDTO.getTag());
        // 添加人名称
        address.setCreatedUser(userLoginVO.getUsername());
        // 地址所属人的编号
        address.setUserId(userLoginVO.getId());
        int result = addressMapper.addAddress(address);
        if (result != 1) {
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED, "新增地址失败！");
        }
    }

    /**
     * 获取收货信息
     */
    @Override
    public AddressUpdateVO getAddressById(Integer id, UserLoginVO userLoginVO) {
        // 根据编号查询地址
        Address address = addressMapper.getAddressById(id);
        // 地址是否为空
        if (address == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "地址不存在！");
        }
        // 地址是否是该用户的
        if (!address.getUserId().equals(userLoginVO.getId())) {
            throw new ServiceException(ServiceCode.ERROR_FORBIDDEN, "无操作权限！");
        }
        // 封装返回
        AddressUpdateVO addressUpdateVO = new AddressUpdateVO();
        addressUpdateVO.setUserId(address.getUserId());
        addressUpdateVO.setId(address.getId());
        addressUpdateVO.setName(address.getName());
        addressUpdateVO.setProvinceCode(address.getProvinceCode());
        addressUpdateVO.setProvinceName(address.getProvinceName());
        addressUpdateVO.setCityCode(address.getCityCode());
        addressUpdateVO.setCityName(address.getCityName());
        addressUpdateVO.setAreaCode(address.getAreaCode());
        addressUpdateVO.setAreaName(address.getAreaName());
        addressUpdateVO.setAddressDetail(address.getAddressDetail());
        addressUpdateVO.setZip(address.getZip());
        addressUpdateVO.setPhone(address.getPhone());
        addressUpdateVO.setTel(address.getTel());
        addressUpdateVO.setTag(address.getTag());
        return addressUpdateVO;
    }

    /**
     * 修改地址信息
     */
    @Override
    public void updateAddress(AddressDTO addressDTO, UserLoginVO userLoginVO) {
        // 根据传来的数据获取地址编号
        Integer id = addressDTO.getId();
        if (id == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "修改的地址编号不能为空！");
        }
        // 根据ID查询地址
        Address address = addressMapper.getAddressById(id);
        if (address == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "地址编号不存在！");
        }
        // 是否属于该用户
        if (!address.getUserId().equals(userLoginVO.getId())) {
            throw new ServiceException(ServiceCode.ERROR_FORBIDDEN, "无操作权限！");
        }
        // 判断地址合法性
        // 判断省/直辖市
        District province = districtMapper.getDistrictByCode(addressDTO.getProvinceCode());
        // 省/直辖市编码是否存在
        if (province == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "省/直辖市编码不存在！");
        }
        // 省/直辖市编码所对的省份名是否正确
        if (!province.getName().equals(addressDTO.getProvinceName())) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "省/直辖市编码与名称对应错误！");
        }
        // 判断城市
        District city = districtMapper.getDistrictByCode(addressDTO.getCityCode());
        // 城市编码是否存在
        if (city == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "城市编码不存在！");
        }
        // 城市编码所对的城市名是否正确
        if (!city.getName().equals(addressDTO.getCityName())) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "城市编码与名称对应错误！");
        }
        // 是否属于该省/直辖市
        if (!city.getParent().equals(province.getCode())) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "城市与省/直辖市对应错误！");
        }
        // 判断区域
        District area = districtMapper.getDistrictByCode(addressDTO.getAreaCode());
        // 区域编码是否存在
        if (area == null) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "区域编码不存在！");
        }
        // 区域编码所对的区域名是否正确
        if (!area.getName().equals(addressDTO.getAreaName())) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "区域编码与名称对应错误！");
        }
        // 是否属于该城市
        if (!area.getParent().equals(city.getCode())) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "区域与城市对应错误！");
        }

        // 重新设置值
        address.setName(addressDTO.getName());
        address.setProvinceCode(addressDTO.getProvinceCode());
        address.setProvinceName(addressDTO.getProvinceName());
        address.setCityCode(addressDTO.getCityCode());
        address.setCityName(addressDTO.getCityName());
        address.setAreaCode(addressDTO.getAreaCode());
        address.setAreaName(addressDTO.getAreaName());
        address.setAddressDetail(addressDTO.getAddressDetail());
        address.setZip(addressDTO.getZip());
        address.setPhone(addressDTO.getPhone());
        address.setTel(addressDTO.getTel());
        address.setTag(addressDTO.getTag());
        address.setModifiedUser(userLoginVO.getUsername());
        int res = addressMapper.updateAddress(address);
        if (res != 1) {
            throw new ServiceException(ServiceCode.ERROR_SAVE_FAILED, "修改地址失败！");
        }
    }

    @Override
    public List<AddressVO> managerGetAddressList(Integer userId, UserLoginVO userLoginVO) {
        if(!userLoginVO.getUsername().equals("admin")){
            throw new ServiceException(ServiceCode.ERROR_BAD_REQUEST,"用户没有管理员全息");
        }
        // 获取列表
        List<Address> addressList = addressMapper.getAddressListByUserId(userId);
        if (addressList.isEmpty()){
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "该用户没有地址！");
        }
        // 将实体类封装的数据转成VO供控制层调用
        List<AddressVO> addressVOList = new ArrayList<>();
        addressList.forEach(address -> {
            AddressVO addressVO = new AddressVO();
            addressVO.setId(address.getId());
            addressVO.setName(address.getName());
            addressVO.setPhone(address.getPhone());
            addressVO.setTag(address.getTag());
            addressVO.setIsDefault(address.getIsDefault());
            // 详细地址：省份 + 城市 + 区县 + 小区编号
            addressVO.setAddressDetail(address.getProvinceName()
                    + address.getCityName()
                    + address.getAreaName()
                    + address.getAddressDetail());
            addressVOList.add(addressVO);
        });
        log.debug("地址信息：{}", addressList);
        return addressVOList;
    }
}
