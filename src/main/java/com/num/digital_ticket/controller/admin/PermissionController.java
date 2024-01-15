package com.num.digital_ticket.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.num.digital_ticket.aspect.ApiOperationLog;
import com.num.digital_ticket.entity.Permission;
import com.num.digital_ticket.sevice.impl.PermissionServiceImpl;
import com.num.digital_ticket.utils.JwtTokenUtil;
import com.num.digital_ticket.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/permissions")
public class PermissionController {
    private final PermissionServiceImpl permissionService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public PermissionController(PermissionServiceImpl permissionService, JwtTokenUtil jwtTokenUtil) {
        this.permissionService = permissionService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * 根据id查询权限
     * @param permissionId
     * @return
     */
    @GetMapping("/{permissionId}")
    @ApiOperationLog(description = "根据id查询权限")
    public Response<Permission> getPermissionById(@PathVariable Integer permissionId) {
        return Response.success(permissionService.getById(permissionId));
    }

    /**
     * 查询全部权限
     * 分页
     * @return
     */
    @GetMapping("/all/{offset}/{size}")
    @ApiOperationLog(description = "查询全部权限")
    public Response<Page> getAllPermissions(@PathVariable Integer offset,
                                                        @PathVariable Integer size) {
        return Response.success(permissionService.page(new Page<>(offset,size), null));
    }

    /**
     * 创建权限
     * @param permission
     * @return
     */
    @PostMapping
    @ApiOperationLog(description = "创建权限")
    @PreAuthorize("hasAuthority('Admin')")
    public Response<Permission> createPermission(@RequestBody Permission permission, HttpServletRequest request) {
        String username = jwtTokenUtil.parseTokenForUsername(request);
        permissionService.save(permission, username);
        return Response.success(permission);
    }

    /**
     * 修改权限
     * @param permissionId
     * @param permission
     * @return
     */
    @PutMapping("/{permissionId}")
    @ApiOperationLog(description = "修改权限")
    @PreAuthorize("hasAuthority('Admin')")
    public Response<Permission> updatePermission(@PathVariable Integer permissionId, @RequestBody Permission permission, HttpServletRequest request) {
        String username = jwtTokenUtil.parseTokenForUsername(request);
        permission.setId(permissionId);
        permissionService.updateById(permission, username);
        return Response.success(permission);
    }

    /**
     * 删除权限
     * @param permissionId
     * @return
     */
    @DeleteMapping("/{permissionId}")
    @ApiOperationLog(description = "删除权限")
    @PreAuthorize("hasAuthority('Admin')")
    public Response deletePermission(@PathVariable Integer permissionId) {
        permissionService.removeById(permissionId);
        return Response.success("Permission with ID " + permissionId + " deleted successfully.","","");
    }
}
