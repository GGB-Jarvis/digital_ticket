package com.num.digital_ticket.sevice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.num.digital_ticket.entity.Ticket;
import com.num.digital_ticket.entity.Role;
import com.num.digital_ticket.entity.Ticket;
import com.num.digital_ticket.mapper.RoleMapper;
import com.num.digital_ticket.mapper.TicketMapper;
import com.num.digital_ticket.mapper.UserMapper;
import com.num.digital_ticket.sevice.TicketService;
import com.num.digital_ticket.utils.Blockchain;
import com.num.digital_ticket.utils.HttpRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;

@Service("ticketServiceImpl")
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket> implements TicketService {
    @Resource
    private UserMapper userMapper;
    /**
     * 门票注册
     * @param entity
     * @return
     */
    public boolean save(Ticket entity, String creator) {
        // 设置创建时间
        entity.setCreatedTime(new Date());
        // 设置创建人
        entity.setCreator(creator);
//        String block_token = HttpRequest.createNft(Blockchain.address,
//                Blockchain.password,
//                Blockchain.collectAddress,
//                "https://cdn.10.1pxeye.com/wp-content/uploads/2018/01/javascript-01-1024x576.jpg",
//                entity);

//        entity.setBlockToken(block_token);
        return super.save(entity);
    }


    /**
     * 修改门票信息
     * @param entity
     * @return
     */
    public boolean ticketCheck(Ticket entity, String editor) {
        // 修改时间
        entity.setEditedTime(new Date());
        // 修改人
        entity.setEditor(editor);

        entity.setTicketCollectorId(userMapper.getUserByUsername(editor,false).getId());
        entity.setTicketCollectorTime(new Date());
        return super.updateById(entity);
    }

    /**
     * 删除门票
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
//        Ticket ticket = this.getById(id);
//        if(ticket != null) {
//            ticket.setDeleteFlag(Boolean.TRUE);
//            return this.updateById(ticket);
//        }
        Ticket ticket = this.getById(id);
        if(ticket != null) {
            return super.removeById(ticket);
        }
        return Boolean.FALSE;
    }
}
