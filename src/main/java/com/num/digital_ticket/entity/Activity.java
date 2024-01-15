package com.num.digital_ticket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Builder
@TableName("activity")
public class Activity implements Serializable {
    private static final long serialVersionUID = 893257123456789L;

    @TableId(type = IdType.NONE)
    private Long id;
    private String activityName;
    private String intro;
    private String promotionalImage;
    private String cast;
    private String ticketCutoffTime;
    private Date performanceTime;
    private String performanceLocation;
    private Long quantity;
    private Integer price;
    private Date createdTime;
    private String creator;
    private Date editedTime;
    private String editor;
    private Boolean deleteFlag;
}

