package com.num.digital_ticket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.num.digital_ticket.entity.Permission;
import com.num.digital_ticket.entity.Role;
import com.num.digital_ticket.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;
import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据用户 Id 查询用户角色
     * @param userId
     * @return
     */
    @Select("SELECT r.*\n" +
            "FROM user_role ur\n" +
            "JOIN role r ON ur.role_id = r.id\n" +
            "WHERE ur.user_id = #{userId};\n")
    List<Role> selectRoleByUserId(Long userId);
    /**
     * 根据用户 Id 查询菜单
     * @param userId
     * @return
     */
    @Select("SELECT p.*\n" +
            "FROM user_role ur\n" +
            "JOIN role_permission rp ON ur.role_id = rp.role_id\n" +
            "JOIN permission p ON rp.permission_id = p.id\n" +
            "WHERE ur.user_id = #{userId};\n")
    List<Permission> selectPermissionByUserId(Long userId);

    /**
     *
     * @param username
     * @param deleteFlag
     * @return
     */
    @Select("SELECT * FROM user WHERE  username = #{username} and delete_flag = #{deleteFlag};")
    User getUserByUsername(String username, Boolean deleteFlag);
}
