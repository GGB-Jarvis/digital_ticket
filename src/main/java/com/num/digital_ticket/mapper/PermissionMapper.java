package com.num.digital_ticket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.num.digital_ticket.entity.Permission;
import com.num.digital_ticket.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
}
