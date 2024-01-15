package com.num.digital_ticket.sevice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.num.digital_ticket.entity.Permission;
import com.num.digital_ticket.entity.Permission;
import com.num.digital_ticket.mapper.PermissionMapper;
import com.num.digital_ticket.mapper.PermissionMapper;
import com.num.digital_ticket.sevice.PermissionService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;

@Service("permissionServiceImpl")
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    /**
     * 权限注册
     * @param entity
     * @return
     */
    public boolean save(Permission entity, String creator) {
        // 设置创建时间
        entity.setCreatedTime(new Date());
        // 设置创建人
        entity.setCreator(creator);
        return super.save(entity);
    }


    /**
     * 修改权限信息
     * @param entity
     * @return
     */
    public boolean updateById(Permission entity, String editor) {
        // 修改时间
        entity.setEditedTime(new Date());
        // 修改人
        entity.setEditor(editor);
        return super.updateById(entity);
    }

    /**
     * 删除权限
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
//        Permission permission = this.getById(id);
//        if(permission != null) {
//            permission.setDeleteFlag(Boolean.TRUE);
//            return this.updateById(permission);
//        }
//        return Boolean.FALSE;
        Permission permission = this.getById(id);
        if(permission != null) {
            return super.removeById(permission);
        }
        return Boolean.FALSE;
    }
    
}
