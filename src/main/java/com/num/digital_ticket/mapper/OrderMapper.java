package com.num.digital_ticket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.num.digital_ticket.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
