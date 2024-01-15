package com.num.digital_ticket.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.num.digital_ticket.aspect.ApiOperationLog;
import com.num.digital_ticket.entity.Activity;
import com.num.digital_ticket.sevice.ActivityService;
import com.num.digital_ticket.sevice.impl.ActivityServiceImpl;
import com.num.digital_ticket.utils.JwtTokenUtil;
import com.num.digital_ticket.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/activities")
public class ActivityController {
    private final ActivityServiceImpl activityService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public ActivityController(ActivityServiceImpl activityService, JwtTokenUtil jwtTokenUtil) {
        this.activityService = activityService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * 根据id查询活动
     * @param activityId
     * @return
     */
    @GetMapping("/{activityId}")
    @ApiOperationLog(description = "根据id查询活动")
    public Response<Activity> getActivityById(@PathVariable Long activityId) {
        return Response.success(activityService.getById(activityId));
    }

    /**
     * 查询全部活动
     * 分页
     * @return
     */
    @GetMapping("/all/{offset}/{size}")
    @ApiOperationLog(description = "查询全部活动")
    public Response<Page> getAllActivitys(@PathVariable Integer offset,
                                                    @PathVariable Integer size) {
//        return Response.success(activityService.page(new Page<>(offset,size), null).getRecords());
        return Response.success(activityService.page(new Page<>(offset,size), null));
    }

    /**
     * 发布活动
     * @param activity
     * @return
     */
    @PostMapping
    @ApiOperationLog(description = "创建活动")
    @PreAuthorize("hasAuthority('Admin')")
    public Response<Activity> createActivity(@RequestBody Activity activity,
                                             HttpServletRequest request) {
        String username = jwtTokenUtil.parseTokenForUsername(request);
        activityService.save(activity, username);
        return Response.success(activity);
    }

    /**
     * 修改活动
     * @param activityId
     * @param activity
     * @return
     */
    @PutMapping("/{activityId}")
    @ApiOperationLog(description = "修改活动")
    @PreAuthorize("hasAuthority('Admin')")
    public Response<Activity> updateActivity(@PathVariable Long activityId,
                                             @RequestBody Activity activity,
                                             HttpServletRequest request) {
        String username = jwtTokenUtil.parseTokenForUsername(request);
        activity.setId(activityId);
        activityService.updateById(activity, username);
        return Response.success(activity);
    }

    /**
     * 删除活动
     * @param activityId
     * @return
     */
    @DeleteMapping("/{activityId}")
    @ApiOperationLog(description = "删除活动")
    @PreAuthorize("hasAuthority('Admin')")
    public Response deleteActivity(@PathVariable Long activityId) {
        activityService.removeById(activityId);
        return Response.success("Activity with ID " + activityId + " deleted successfully.","","");
    }
}
