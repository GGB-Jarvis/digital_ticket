package com.num.digital_ticket.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.num.digital_ticket.aspect.ApiOperationLog;
import com.num.digital_ticket.entity.Role;
import com.num.digital_ticket.sevice.impl.RoleServiceImpl;
import com.num.digital_ticket.utils.JwtTokenUtil;
import com.num.digital_ticket.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleServiceImpl roleServiceImpl;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public RoleController(RoleServiceImpl roleServiceImpl, JwtTokenUtil jwtTokenUtil) {
        this.roleServiceImpl = roleServiceImpl;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * 根据id查询角色
     * @param roleId
     * @return
     */
    @GetMapping("/{roleId}")
    @ApiOperationLog(description = "根据id查询角色")
    public Response<Role> getRoleById(@PathVariable Integer roleId) {
        return Response.success(roleServiceImpl.getById(roleId));
    }

    /**
     * 查询全部角色
     * 分页
     * @return
     */
    @GetMapping("/all/{offset}/{size}")
    @ApiOperationLog(description = "查询全部角色")
    public Response<Page> getAllRoles(@PathVariable Integer offset,
                                            @PathVariable Integer size) {
        return Response.success(roleServiceImpl.page(new Page<>(offset,size), null));
    }

    /**
     * 创建角色
     * @param role
     * @return
     */
    @PostMapping
    @ApiOperationLog(description = "创建角色")
    @PreAuthorize("hasAuthority('Admin')")
    public Response<Role> createRole(@RequestBody Role role, HttpServletRequest request) {
        String username = jwtTokenUtil.parseTokenForUsername(request);
        roleServiceImpl.save(role, username);
        return Response.success(role);
    }

    /**
     * 修改角色
     * @param roleId
     * @param role
     * @return
     */
    @PutMapping("/{roleId}")
    @ApiOperationLog(description = "修改角色")
    @PreAuthorize("hasAuthority('Admin')")
    public Response<Role> updateRole(@PathVariable Integer roleId, @RequestBody Role role, HttpServletRequest request) {
        String username = jwtTokenUtil.parseTokenForUsername(request);
        role.setId(roleId);
        roleServiceImpl.updateById(role, username);
        return Response.success(role);
    }

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    @DeleteMapping("/{roleId}")
    @ApiOperationLog(description = "删除角色")
    @PreAuthorize("hasAuthority('Admin')")
    public Response deleteRole(@PathVariable Integer roleId) {
        roleServiceImpl.removeById(roleId);
        return Response.success("Role with ID " + roleId + " deleted successfully.","","");
    }
}

