package com.num.digital_ticket.controller.admin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.num.digital_ticket.aspect.ApiOperationLog;
import com.num.digital_ticket.entity.User;
import com.num.digital_ticket.sevice.UserService;
import com.num.digital_ticket.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
//    @Resource
    private final UserService userServiceImpl;

    @Autowired
    public UserController(UserService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    /**
     * 根据id查询用户
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    @ApiOperationLog(description = "根据id查询用户")
    @PreAuthorize("hasAuthority('Admin')")
    public Response<User> getUserById(@PathVariable Long userId) {
        return Response.success("success","200",userServiceImpl.getById(userId));
    }

    /**
     * 查询全部用户
     * 分页
     * @return
     */
    @GetMapping("/all/{offset}/{size}")
    @ApiOperationLog(description = "查询全部用户")
    @PreAuthorize("hasAuthority('Admin')")
    public Response<Page> getAllUsers(@PathVariable Integer offset,
                                            @PathVariable Integer size) {
//        return Response.success(userServiceImpl.page(new Page<>(offset,size), null).getRecords());
        return Response.success(userServiceImpl.page(new Page<>(offset,size), null));
    }

    /**
     * 创建用户
     * @param user
     * @return
     */
    @PostMapping("/register")
    @ApiOperationLog(description = "创建用户")
//    @PreAuthorize("hasAuthority('管理员')")
    public Response<User> createUser(@RequestBody User user) {
        userServiceImpl.save(user);
        return Response.success(user);
    }

    /**
     * 修改用户
     * @param userId
     * @param user
     * @return
     */
    @PutMapping("/{userId}")
    @ApiOperationLog(description = "修改用户")
    @PreAuthorize("hasAuthority('Admin')")
    public Response<User> updateUser(@PathVariable Long userId, @RequestBody User user) {
        user.setId(userId);
        userServiceImpl.updateById(user);
        return Response.success(user);
    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @DeleteMapping("/{userId}")
    @ApiOperationLog(description = "删除用户")
    @PreAuthorize("hasAuthority('Admin')")
    public Response deleteUser(@PathVariable Long userId) {
        userServiceImpl.removeById(userId);
        return Response.success("User with ID " + userId + " deleted successfully.","200","");
    }
}
