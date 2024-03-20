package com.backend.springstore.address.controller;

import com.backend.springstore.address.pojo.vo.DistrictVO;
import com.backend.springstore.address.service.DistrictService;
import com.backend.springstore.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("district")
@Slf4j
@Api(tags = "区域模块")
public class DistrictController {
    @Autowired
    DistrictService districtService;

    @GetMapping("list")
    @ApiOperation("获取行政列表地址接口")
    public Result<List<DistrictVO>> getDistrictList(Integer parent) {
        List<DistrictVO> districtVOList = districtService.getDistrictList(parent);
        return Result.ok(districtVOList);
    }
}
