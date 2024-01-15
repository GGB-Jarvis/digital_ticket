package com.num.digital_ticket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("`order`")
public class Order implements Serializable {

    private static final long serialVersionUID = 123456781176543L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNumber;
    @NonNull
    private Long userId;
    @NonNull
    private Long activityId;
    @NonNull
    private Integer quantity;
    @NonNull
    private String paymentMethod;
    private Date paymentTime;
    private BigDecimal totalAmount;
    private Integer status;
    @TableField(exist = false)
    private String seatInfo;
    private Date createdTime;
    private String creator;
    private Date editedTime;
    private String editor;
    private Boolean deleteFlag;

    private String outTradeNo;

}
