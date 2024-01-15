package com.num.digital_ticket.sevice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.num.digital_ticket.entity.Permission;
import com.num.digital_ticket.entity.RedefinedUserDetails;
import com.num.digital_ticket.entity.Role;
import com.num.digital_ticket.entity.User;
import com.num.digital_ticket.mapper.UserMapper;
import com.num.digital_ticket.sevice.UserService;
import com.num.digital_ticket.utils.HttpRequest;
import com.num.digital_ticket.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service("userServiceImpl")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;
    /**
     * 用户登录
     * 根据username查询用户信息以及权限信息封装返回
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
//    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询user表
        User user = userMapper.getUserByUsername(username, false);
        if(user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        // 查询menu表
        List<Permission> permissions = userMapper.selectPermissionByUserId(user.getId());
        // 查询role表
        List<Role> roles = userMapper.selectRoleByUserId(user.getId());

        List<String> list = new ArrayList<>();
        for (Permission m :
                permissions) {
            list.add(m.getPermissionName());
        }
        for (Role r :
                roles) {
            list.add(r.getRoleName());
        }
        RedefinedUserDetails redefinedUserDetails = new RedefinedUserDetails(user, list);
        return redefinedUserDetails;
    }

//    @Override
//    public User getById(Serializable id) {
//        return super.getById(id);
//    }

    /**
     * 用户注册
     * @param entity
     * @return
     */
    @Override
    public boolean save(User entity) {
        // 加密
        entity.setPassword(new BCryptPasswordEncoder().encode(entity.getPassword()));
        // 设置创建时间
        entity.setRegisterTime(new Date());
        // 创建账户
        entity.setBlockPassword(MD5Utils.getMd5(System.currentTimeMillis() + ""));
        String result = HttpRequest.createUser(entity.getBlockPassword());
        entity.setBlockAddress(result);
        String privateKey = HttpRequest.getPrivateKey(entity.getBlockPassword(), entity.getBlockAddress());
        entity.setBlockPrivateKey(privateKey);
        String tCode = "";
        Random random = new Random();
        User j = new User();
        while(true){
            tCode = random.nextInt(1000000)+"";
            if(tCode.length()<6){
                int c = 6 - tCode.length();
                for (int i = 0; i < c; i++) {
                    tCode = "0" + tCode;
                }
            }
            j.setBlockToken(tCode);
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("block_token", tCode);
            List<User> users = userMapper.selectList(queryWrapper);
            if(users.size()==0){
                break;
            }
        }
        entity.setBlockToken(tCode);
        return super.save(entity);
    }


    /**
     * 修改用户信息
     * @param entity
     * @return
     */
    @Override
    public boolean updateById(User entity) {
        // 修改时间
        entity.setEditedTime(new Date());
        // 修改密码
        String password;
        if(!StringUtils.isEmpty(password = entity.getPassword())) {
            entity.setPassword(new BCryptPasswordEncoder().encode(password));
        }
        return super.updateById(entity);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
        User user = this.getById(id);
        if(user != null) {
            user.setDeleteFlag(Boolean.TRUE);
            return this.updateById(user);
        }
        return Boolean.FALSE;

    }
}
