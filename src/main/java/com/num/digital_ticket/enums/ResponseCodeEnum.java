package com.num.digital_ticket.enums;

import com.num.digital_ticket.exception.BaseExceptionInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: 犬小哈
 * @url: www.quanxiaoha.com
 * @date: 2023-04-18 8:14
 * @description: 响应枚举
 **/
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {


    // ----------- 通用异常状态码 -----------
    SYSTEM_ERROR("10000", "出错啦，后台小哥正在努力修复中"),
    PARAM_ERROR("10001", "参数错误"),

    // ----------- 业务异常状态码 -----------
    UNAUTHORIZED("10002", "无访问权限，请先登录！"),
    FORBIDDEN("10003", "演示账号仅支持查询操作！"),
    NO_TOKEN_OR_TOKEN_INVALID("10004", "Token 丢失或 Token 不可用！"),
    LOGIN_FAIL("10005", "登录失败"),
    USERNAME_OR_PWD_ERROR("10006", "用户名或密码错误"),
    UPLOAD_FILE_ERROR("10007", "文件上传失败"),
    DUPLICATE_TAG_ERROR("10008", "提交的部分标签已被创建"),
    DUPLICATE_CATEGORY_ERROR("10009", "该分类已被创建"),
    TOKEN_EXPIRED("10010", "Token 已过期"),

    ACCOUNT_LOCKED("10011", "账户被锁定，请联系管理员"),
    ACCOUNT_EXPIRED("10012", "账户已过期"),
    ACCOUNT_DISABLED("10013", "账户被禁用，请联系管理员"),
    CSRF_EXCEPTION("10014", "非法访问跨域请求异常"),
    AUTHORIZATION_SERVICE_EXCEPTION("10015", "认证服务异常请重试"),
    REGISTER_FAIL("10016", "注册失败")
    ;

    private String errorCode;
    private String errorMessage;

}
