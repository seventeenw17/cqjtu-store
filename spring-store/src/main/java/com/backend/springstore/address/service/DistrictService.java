package com.backend.springstore.address.service;

import com.backend.springstore.address.pojo.vo.DistrictVO;

import java.util.List;

public interface DistrictService {
    List<DistrictVO> getDistrictList(Integer parent);
}
