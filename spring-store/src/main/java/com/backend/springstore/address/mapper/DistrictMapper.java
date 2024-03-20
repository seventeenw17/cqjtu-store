package com.backend.springstore.address.mapper;

import com.backend.springstore.address.pojo.entity.District;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictMapper {
    List<District> getDistrictList(Integer parent);
    District getDistrictByCode(Integer code);
    List<District> getDistrictByName(String name);
}
