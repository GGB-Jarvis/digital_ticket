package com.num.digital_ticket;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.num.digital_ticket.entity.*;
import com.num.digital_ticket.mapper.*;
import com.num.digital_ticket.sevice.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class DigitalTicketApplicationTests {

//	@Resource
//	private UserMapper userMapper;
//	@Resource
//	private PermissionMapper permissionMapper;
//	@Resource
//	private RoleMapper roleMapper;
//	@Resource
//	private TicketMapper ticketMapper;
//	@Resource
//	private ActivityMapper activityMapper;
	@Test
	void contextLoads() {
//		QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
//		QueryWrapper<Role> queryWrapper2 = new QueryWrapper<>();
//		QueryWrapper<Permission> queryWrapper3 = new QueryWrapper<>();
//		QueryWrapper<Activity> queryWrapper4 = new QueryWrapper<>();
//		QueryWrapper<Ticket> queryWrapper5 = new QueryWrapper<>();
//
//		// 查询全部数据
//		List<User> users = userMapper.selectList(new QueryWrapper<>());
//		List<Role> roles = roleMapper.selectList(new QueryWrapper<>());
//		List<Permission> permissions = permissionMapper.selectList(new QueryWrapper<>());
//		List<Activity> activities = activityMapper.selectList(new QueryWrapper<>());
//		List<Ticket> tickets = ticketMapper.selectList(new QueryWrapper<>());
//
//		// 查询用户权限
//		List<Role> roles1 = userMapper.selectRoleByUserId(8L);
//		List<Permission> permissions1 = userMapper.selectPermissionByUserId(8L);
	}
	@Resource
	private UserService userService;
	@Resource
	private PermissionService permissionService;
	@Resource
	private RoleService roleService;
	@Resource
	private TicketService ticketService;
	@Resource
	private ActivityService activityService;
	@Test
	void testService() {
		// select
//		User user = userService.getById(8);
//		Page<User> page = userService.page(new Page<User>(3, 2), null);

//		User user = new User();
//		user.setUsername("user4");
//		user.setPhone("13408840748");
//		boolean save = userService.save(user);

//		User user = new User();
//		user.setId(11L);
//		user.setAge(18);
//		boolean update = userService.updateById(user);

//		userService.removeById(11);
	}

}
