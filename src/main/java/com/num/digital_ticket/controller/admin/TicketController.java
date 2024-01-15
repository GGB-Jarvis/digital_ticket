package com.num.digital_ticket.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.num.digital_ticket.aspect.ApiOperationLog;
import com.num.digital_ticket.entity.Ticket;
import com.num.digital_ticket.sevice.impl.TicketServiceImpl;
import com.num.digital_ticket.utils.JwtTokenUtil;
import com.num.digital_ticket.vo.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketServiceImpl ticketService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public TicketController(TicketServiceImpl ticketService, JwtTokenUtil jwtTokenUtil) {
        this.ticketService = ticketService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * 根据id查询门票信息
     * @param ticketId
     * @return
     */
    @GetMapping("/{ticketId}")
    @ApiOperationLog(description = "根据id查询门票信息")
    public Response<Ticket> getTicketById(@PathVariable Long ticketId) {
        return Response.success(ticketService.getById(ticketId));
    }

    /**
     * 二维码/base64编码
     * @param userId
     * @return
     */
    @GetMapping("/qr/{userId}")
    @ApiOperationLog(description = "根据id查询门票信息, 返回二维码")
    public Response<String> ticketQR(@PathVariable Long userId) {
//        QueryWrapper<Ticket> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("user_id", userId);
//        List<Ticket> tickets = ticketService.list(queryWrapper);
        return Response.success();
    }

    /**
     * 查询全部门票信息
     * 分页
     * @return
     */
    @GetMapping("/all/{offset}/{size}")
    @ApiOperationLog(description = "查询全部门票信息")
    public Response<Page> getAllTickets(@PathVariable Integer offset,
                                                @PathVariable Integer size) {
        return Response.success(ticketService.page(new Page<>(offset,size), null));
    }

    /**
     * 创建门票信息
     * @param ticket
     * @return
     */
    @PostMapping
    @ApiOperationLog(description = "创建门票信息")
//    @PreAuthorize("hasAuthority('管理员')")
    public Response<Ticket> createTicket(@RequestBody Ticket ticket,
                                         HttpServletRequest request) {
        String username = jwtTokenUtil.parseTokenForUsername(request);
        ticketService.save(ticket, username);
        return Response.success(ticket);
    }

    /**
     * 检票接口
     * @param ticketId
     * @return
     */
    @PutMapping("/check/{ticketId}")
    @ApiOperationLog(description = "检票")
    @PreAuthorize("hasAuthority('Admin')")
    public Response<Ticket> updateTicket(@PathVariable Long ticketId,
//                                         @RequestBody Ticket ticket,
                                         HttpServletRequest request) {
        String username = jwtTokenUtil.parseTokenForUsername(request);
        Ticket ticket = ticketService.getById(ticketId);
        if(ticket == null) {
            return Response.fail("验票失败");
        }
        if(ticket.getTicketCollectorId() != null) {
            return Response.fail("无效的票据");
        }
        ticket.setId(ticketId);
        ticketService.ticketCheck(ticket, username);
        return Response.success(ticket);
    }

    /**
     * 删除门票信息
     * @param ticketId
     * @return
     */
    @DeleteMapping("/{ticketId}")
    @ApiOperationLog(description = "删除门票信息")
    @PreAuthorize("hasAuthority('Admin')")
    public Response deleteTicket(@PathVariable Long ticketId) {
        ticketService.removeById(ticketId);
        return Response.success("Ticket with ID " + ticketId + " deleted successfully.","","");
    }
}
