package com.num.digital_ticket.sevice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.num.digital_ticket.entity.Permission;
import com.num.digital_ticket.entity.Role;
import com.num.digital_ticket.entity.Role;
import com.num.digital_ticket.mapper.RoleMapper;
import com.num.digital_ticket.sevice.RoleService;
import com.num.digital_ticket.utils.HttpRequest;
import com.num.digital_ticket.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service("roleServiceImpl")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    /**
     * 角色注册
     * @param entity
     * @return
     */
    public boolean save(Role entity, String creator) {
        // 设置创建时间
        entity.setCreatedTime(new Date());
        // 设置创建人
        entity.setCreator(creator);
        return super.save(entity);
    }


    /**
     * 修改角色信息
     * @param entity
     * @return
     */
    public boolean updateById(Role entity, String editor) {
        // 修改时间
        entity.setEditedTime(new Date());
        // 修改人
        entity.setEditor(editor);
        return super.updateById(entity);
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
//        Role role = this.getById(id);
//        if(role != null) {
//            role.setDeleteFlag(Boolean.TRUE);
//            return this.updateById(role);
//        }
        Role role = this.getById(id);
        if(role != null) {
            return super.removeById(role);
        }
        return Boolean.FALSE;
    }
}
