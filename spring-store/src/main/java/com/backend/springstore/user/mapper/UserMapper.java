package com.backend.springstore.user.mapper;

import com.backend.springstore.user.pojo.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int insert(User user);
    User getUserByName(String username);
    int updatePassword(User updateUser);
    int updateInfo(User updateUser);
    int saveAvatar(User savcUser);
    int login(User user);
    Integer countUser();

    int deleteUser(User user);

    List<User> getList();

    int insertUser(User user);

    int updateUser(User user);

    List<User> getUser(String username);
}
