package com.num.digital_ticket.sevice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.num.digital_ticket.entity.Activity;
import com.num.digital_ticket.entity.Order;
import com.num.digital_ticket.entity.Order;
import com.num.digital_ticket.entity.Ticket;
import com.num.digital_ticket.mapper.OrderMapper;
import com.num.digital_ticket.sevice.OrderService;
import org.springframework.stereotype.Service;
import sun.print.DialogOnTop;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("orderServiceImpl")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Resource
    private TicketServiceImpl ticketService;
    @Resource
    private ActivityServiceImpl activityService;

    /**
     * 订单创建
     * @param entity
     * @param creator
     * @return
     */
    public boolean save(Order entity, String creator) {
        Integer quantity = entity.getQuantity();
        // 票数不足
        Activity activity = activityService.getById(entity.getActivityId());
        if(activity.getQuantity() < quantity)  throw new RuntimeException("库存不足");

        Date date = new Date();
        // 订单编号
        entity.setOrderNumber("NO"+date.getTime()+""+ (int) (Math.random() * 1000));
        // 待支付
        entity.setStatus(1);
        // 创建/下单时间
        entity.setCreatedTime(new Date());
        // 创建人
        entity.setCreator(creator);
        // 订单总金额
        entity.setTotalAmount(BigDecimal.valueOf((long) activity.getPrice() * quantity));




        return super.save(entity);
    }


    /**
     * 修改订单信息
     * @param entity
     * @return
     */
    public boolean updateById(Order entity, String editor) {
        // 修改时间
        entity.setEditedTime(new Date());
        // 修改人
        entity.setEditor(editor);

        return super.updateById(entity);
    }

    /**
     * 删除订单
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
//        Order order = this.getById(id);
//        if(order != null) {
//            order.setDeleteFlag(Boolean.TRUE);
//            return this.updateById(order);
//        }
        Order order = this.getById(id);
        if(order != null) {
            return super.removeById(order);
        }
        return Boolean.FALSE;
    }
}
