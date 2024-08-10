package com.louis.springbootinit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.louis.springbootinit.common.ResultUtils;
import com.louis.springbootinit.model.entity.User;
import com.louis.springbootinit.model.vo.PageUserVO;
import com.louis.springbootinit.service.SysService;
import com.louis.springbootinit.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author louis
 * @version 1.0
 * @date 2024/7/13 0:56
 */
@SpringBootTest
public class PermissionTest {
    @Resource
    private SysService sysService;
    @Resource
    private UserService userService;
    @Test
    void save(){
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        User user = userService.getOne(new QueryWrapper<User>().eq("id", "1793825863632478210"));
        user.setAuthModelVos(list);
        userService.saveOrUpdate(user);
    }
    @Test
    void get(){
        User user = userService.getOne(new QueryWrapper<User>().eq("id", "1793825863632478210"));
        System.out.println(user.getAuthModelVos());
    }
}
