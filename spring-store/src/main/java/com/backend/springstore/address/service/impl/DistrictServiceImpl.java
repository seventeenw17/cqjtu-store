package com.backend.springstore.address.service.impl;

import com.backend.springstore.address.mapper.DistrictMapper;
import com.backend.springstore.address.pojo.entity.District;
import com.backend.springstore.address.pojo.vo.DistrictVO;
import com.backend.springstore.address.service.DistrictService;
import com.backend.springstore.common.ServiceCode;
import com.backend.springstore.common.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class DistrictServiceImpl implements DistrictService {
    @Autowired
    private DistrictMapper districtMapper;

    /**
     * 获取区域列表
     */
    @Override
    public List<DistrictVO> getDistrictList(Integer parent) {
        if (parent == null) {
            parent = 86;
        }
        List<District> districtList = districtMapper.getDistrictList(parent);
        if (districtList.isEmpty()) {
            throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, "没有查到相关数据！");
        }
        List<DistrictVO> districtVOList = new ArrayList<>();
        districtList.forEach(district -> {
            DistrictVO districtVO = new DistrictVO();
            districtVO.setCode(district.getCode());
            districtVO.setName(district.getName());
            districtVOList.add(districtVO);
        });
        return districtVOList;
    }
}
