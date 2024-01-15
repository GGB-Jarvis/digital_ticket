package com.num.digital_ticket.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.num.digital_ticket.aspect.ApiOperationLog;
import com.num.digital_ticket.config.properties.WxPayProperties;
import com.num.digital_ticket.entity.Activity;
import com.num.digital_ticket.entity.Order;
import com.num.digital_ticket.entity.Ticket;
import com.num.digital_ticket.entity.User;
import com.num.digital_ticket.sevice.impl.ActivityServiceImpl;
import com.num.digital_ticket.sevice.impl.OrderServiceImpl;
import com.num.digital_ticket.sevice.impl.TicketServiceImpl;
import com.num.digital_ticket.sevice.impl.UserServiceImpl;
import com.num.digital_ticket.utils.*;
import com.num.digital_ticket.vo.Response;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderServiceImpl orderService;
    private final UserServiceImpl userService;

    private final ActivityServiceImpl activityService;
    private final TicketServiceImpl ticketService;

    private final JwtTokenUtil jwtTokenUtil;

//    private final Config wxPayConfig;

    private final WxPayProperties wxPayProperties;

    @Autowired
    public OrderController(OrderServiceImpl orderService, UserServiceImpl userService, ActivityServiceImpl activityService, TicketServiceImpl ticketService, JwtTokenUtil jwtTokenUtil,
//                           Config wxPayConfig,
                           WxPayProperties wxPayProperties) {
        this.orderService = orderService;
        this.userService = userService;
        this.activityService = activityService;
        this.ticketService = ticketService;
        this.jwtTokenUtil = jwtTokenUtil;
//        this.wxPayConfig = wxPayConfig;
        this.wxPayProperties = wxPayProperties;
    }

    /**
     * 根据id查询订单
     * @param orderId
     * @return
     */
    @GetMapping("/{orderId}")
    @ApiOperationLog(description = "根据id查询订单")
    public Response<Order> getOrderById(@PathVariable Long orderId) {
        return Response.success(orderService.getById(orderId));
    }

    /**
     * 查询全部订单
     * 分页
     * @return
     */
    @GetMapping("/all/{offset}/{size}")
    @ApiOperationLog(description = "查询全部订单")
    public Response<Page> getAllOrders(@PathVariable Integer offset,
                                            @PathVariable Integer size) {
        return Response.success(orderService.page(new Page<>(offset,size), null));
    }

    /**
     * 创建订单
     * @param order
     * @return
     */
    @PostMapping
    @ApiOperationLog(description = "创建订单")
//    @PreAuthorize("hasAuthority('管理员')")
    public Response<Order> createOrder(@RequestBody Order order,
                                       HttpServletRequest request) {
        String username = jwtTokenUtil.parseTokenForUsername(request);
        orderService.save(order, username);
        return Response.success(order);
    }

    /**
     * 修改订单
     * @param orderId
     * @param order
     * @return
     */
    @PutMapping("/{orderId}")
    @ApiOperationLog(description = "修改订单")
    @PreAuthorize("hasAuthority('Admin')")
    public Response<Order> updateOrder(@PathVariable Long orderId,
                                       @RequestBody Order order,
                                       HttpServletRequest request) {
        String username = jwtTokenUtil.parseTokenForUsername(request);
        order.setId(orderId);
        orderService.updateById(order, username);
        return Response.success(order);
    }

    /**
     * 删除订单
     * @param orderId
     * @return
     */
    @DeleteMapping("/{orderId}")
    @ApiOperationLog(description = "删除订单")
    @PreAuthorize("hasAuthority('Admin')")
    public Response deleteOrder(@PathVariable Long orderId) {
        orderService.removeById(orderId);
        return Response.success("Order with ID " + orderId + " deleted successfully.","200","");
    }

    public static JsapiService service;
    @GetMapping("/pay")
    public Map<Object, Object> pay(Integer orderId) throws Exception {
        Config config = new RSAAutoCertificateConfig.Builder()
                .merchantId(wxPayProperties.getMchid())
                // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
//                        .privateKeyFromPath(privateKeyPath)
//                        .merchantSerialNumber(merchantSerialNumber)
                .apiV3Key(wxPayProperties.getMchkey())
                .build();
        service = new JsapiService.Builder().config(config).build();
        Order order = orderService.getById(orderId);
        User user = userService.getById(order.getUserId());

        String openid = user.getOpenid();
        Payer payer = new Payer();
        payer.setOpenid(openid);

        String noncestr = Sha1Util.getNonceStr();
        String timestamp = Sha1Util.getTimeStamp();
        String out_trade_no = timestamp + order.getId();
        order.setOutTradeNo(out_trade_no);

        Amount amount = new Amount();
        amount.setTotal(Integer.parseInt(order.getTotalAmount().toString()));
        amount.setCurrency("CNY");

        SceneInfo sceneInfo = new SceneInfo();
        sceneInfo.setDeviceId("WEB");
//        sceneInfo.setPayerClientIp(request.getRemoteAddr());

        PrepayRequest prepayRequest = new PrepayRequest();
        prepayRequest.setAppid(wxPayProperties.getAppid());
        prepayRequest.setMchid(wxPayProperties.getMchid());
        prepayRequest.setAmount(amount);
        prepayRequest.setDescription("门票");
        prepayRequest.setOutTradeNo(out_trade_no);
        prepayRequest.setNotifyUrl(wxPayProperties.getNotifyUrl());
        prepayRequest.setPayer(payer);
        prepayRequest.setSceneInfo(sceneInfo);

        PrepayResponse prepay = service.prepay(prepayRequest);
        String prepay_id = prepay.getPrepayId();

        // 生成支付签名
        SortedMap<Object,Object> signMap = new TreeMap<Object,Object>();
        signMap.put("appId", wxPayProperties.getAppid());
        signMap.put("timeStamp", timestamp);
        signMap.put("nonceStr", noncestr);
        signMap.put("package", "prepay_id=" + prepay_id);
        signMap.put("signType", "MD5");
        // 微信支付签名
        String paySign = MD5Util.createSign(signMap, wxPayProperties.getMchkey());
        signMap.put("paySign", paySign);

        orderService.updateById(order);
        return signMap;
    }
    @GetMapping("/notify")
    @Transactional
    public String notify(HttpServletRequest request) {
        try {
            InputStream inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            String resultStr = new String(outSteam.toByteArray(), "UTF-8");
            Map<String, String> resultMap = XMLUtil.doXMLParse(resultStr);

            System.out.println("微信回调结果:" + resultMap.toString());

            String out_trade_no = resultMap.get("out_trade_no");
            String return_code = resultMap.get("return_code");
            // 签名验证
            //GenericValue userLogin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "admin"), false);
            if (return_code.equals("SUCCESS")) {
                Order orders;
                QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("out_trade_no", out_trade_no);
                List<Order> ordersList = orderService.list(queryWrapper);
                if(ordersList.size()>0){
                    // 更新订单表
                    orders = ordersList.get(0);
                    orders.setPaymentMethod("微信支付");
                    orders.setStatus(2);
                    orders.setPaymentTime(new Date());
                    orderService.updateById(orders);
                    // 购买数量
                    Integer quantity = orders.getQuantity();
                    // 更新活动表
                    Activity activity = activityService.getById(orders.getActivityId());
                    activity.setQuantity(activity.getQuantity() - quantity);
                    activityService.updateById(activity);
                    // 查询未被购买的票
                    QueryWrapper<Ticket> queryWrapper1 = new QueryWrapper<>();
                    Map<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id", activity.getId());
                    hashMap.put("user_id","null");
                    queryWrapper.allEq(hashMap);
                    List<Ticket> tickets = ticketService.list(queryWrapper1);
                    User user = userService.getById(orders.getUserId());
                    // 更新门票
                    for (int i = 0; i < quantity; i++) {
                        Ticket ticket = tickets.get(i);
                        ticket.setUserId(orders.getUserId());
                        ticketService.updateById(ticket);
                        // 门票转移
                        HttpRequest.transfer(Blockchain.address,
                                Blockchain.password,
                                Blockchain.collectAddress,
                                ticket.getBlockToken(),
                                "1",
                                user.getBlockAddress(),
                                orders.getOrderNumber());
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";

    }

}
