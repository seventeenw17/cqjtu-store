package com.backend.springstore.user.service;

import com.backend.springstore.user.pojo.dto.*;
import com.backend.springstore.user.pojo.entity.User;
import com.backend.springstore.user.pojo.vo.UserLoginVO;
import com.backend.springstore.user.pojo.vo.UserInfoVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    void register(UserRegisterDTO userRegisterDTO);
    UserLoginVO login(UserLoginDTO userLoginDTO);
    void updatePassword(UserPasswordUpdateDTO userPasswordUpdateDTO, UserLoginVO userLoginVO);
    UserInfoVO getUserInfo(UserLoginVO userLoginVO);
    void updateInfo(UserInfoUpdateDTO userInfoUpdateDTO, UserLoginVO userLoginVO);
    void uploadAvatar(MultipartFile avatar, UserLoginVO userLoginVO);
    Integer countUser();
    void deleteuser(UserDeleteDTO userDeleteDTO, UserLoginVO userLoginVO);

    List<User> getUserList(UserLoginVO userLoginVO);
    List<User> selectUser(UserDeleteDTO userDeleteDTO,UserLoginVO userLoginVO);

    void updateUser(UserUpdateDTO userUpdateDTO, UserLoginVO userLoginVO);
    void InsertUser(UserInsertDTO userInsertDTO, UserLoginVO userLoginVO);
}
