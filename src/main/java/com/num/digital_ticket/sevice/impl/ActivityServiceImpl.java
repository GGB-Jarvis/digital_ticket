package com.num.digital_ticket.sevice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.num.digital_ticket.entity.Activity;
import com.num.digital_ticket.entity.Activity;
import com.num.digital_ticket.entity.Ticket;
import com.num.digital_ticket.mapper.ActivityMapper;
import com.num.digital_ticket.mapper.ActivityMapper;
import com.num.digital_ticket.sevice.ActivityService;
import com.num.digital_ticket.utils.Blockchain;
import com.num.digital_ticket.utils.HttpRequest;
import com.num.digital_ticket.utils.SnowFlakeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("activityServiceImpl")
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {
    private TicketServiceImpl ticketService;

    @Autowired
    public ActivityServiceImpl(TicketServiceImpl ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * 发布活动
     * 待修改
     * @param entity
     * @return
     */
    @Transactional
    public boolean save(Activity entity, String creator) {
        long id = SnowFlakeUtil.nextId();
        entity.setId(id);
        // 创建时间
        entity.setCreatedTime(new Date());
        // 创建人
        entity.setCreator(creator);
        // 创建门票
        Long quantity = entity.getQuantity();
        List<Ticket> tickets = new ArrayList<>();
        String token = HttpRequest.createNft(Blockchain.address,
                Blockchain.password,
                Blockchain.collectAddress,
                // 活动宣传图
                "https://cdn.10.1pxeye.com/wp-content/uploads/2018/01/javascript-01-1024x576.jpg",
                entity.getActivityName(),
                entity.getIntro(),
                entity.getCreator(),
                "暂无",
                quantity + "");

        for (long i = 0; i < quantity; i++) {
            Ticket ticket = new Ticket();
            ticket.setActivityId(entity.getId());
            ticket.setCreator(creator);
            ticket.setCreatedTime(new Date());
            ticket.setBlockToken(token);
            tickets.add(ticket);
        }
        return super.save(entity) && ticketService.saveBatch(tickets);
    }


    /**
     * 修改活动信息
     * @param entity
     * @return
     */
    public boolean updateById(Activity entity, String editor) {
        // 修改时间
        entity.setEditedTime(new Date());
        // 修改人
        entity.setEditor(editor);
        return super.updateById(entity);
    }

    /**
     * 删除活动
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
//        Activity activity = this.getById(id);
//        if(activity != null) {
//            activity.setDeleteFlag(Boolean.TRUE);
//            return this.updateById(activity);
//        }
        Activity activity = this.getById(id);
        if(activity != null) {
            return super.removeById(activity);
        }
        return Boolean.FALSE;
    }
}
