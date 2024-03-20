package com.backend.springstore.favorite.pojo.dto;

import com.backend.springstore.page.PageDTO;
import lombok.Data;

@Data
public class FavoritePageDTO extends PageDTO {
    // 用户ID
    private Integer userId;
}
