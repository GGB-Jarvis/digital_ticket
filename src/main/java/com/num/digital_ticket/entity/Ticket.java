package com.num.digital_ticket.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("ticket")
public class Ticket implements Serializable {
    private static final long serialVersionUID = 987654321012345L;
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long activityId;

    private String seatInfo;
    private Long ticketCollectorId;
    private Date ticketCollectorTime;
    private Date createdTime;
    private String creator;
    private Date editedTime;
    private String editor;
    private Boolean deleteFlag;

    private String blockToken;
}

