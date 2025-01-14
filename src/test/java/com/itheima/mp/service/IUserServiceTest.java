package com.itheima.mp.service;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.po.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IUserServiceTest {
    @Autowired
    private IUserService userService;

    @Test
    void testSaveUser() {
        User user = new User();
        //user.setId(5L);
        user.setUsername("LiLei");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        user.setInfo(UserInfo.of(24,"英语老师","female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userService.save(user);
    }

    @Test
    void testQuery(){
        List<User> ids = userService.listByIds(List.of(1L, 2L, 4L));
        ids.forEach(System.out::println);
    }

    private User buildUser(int i) {
        User user = new User();
        user.setUsername("user_"+i);
        user.setPassword("123");
        user.setPhone(""+(18699190000L + i));
        user.setBalance(2000);
        user.setInfo(UserInfo.of(24,"英语老师","female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(user.getCreateTime());
        return user;
    }

    //未加rewriteBatchedStatements=true：29s184ms
    //加入rewriteBatchedStatements=true：13s446ms
    @Test
    void testSaveBatch(){
        //每次批量插入1000条数据，插入100次即10万条数据
        //1.准备一个容量为1000的容器
        ArrayList<User> list = new ArrayList<>(1000);
        long b = System.currentTimeMillis();
        for(int i=1;i<=100000;i++){
            //2.添加一个User
            list.add(buildUser(i));
            //3.每1000条批量插入一次
            if(i % 1000 ==0){
                userService.saveBatch(list);
                //4.清空集合，准备下一批数据
                list.clear();
            }
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时："+(e-b));
    }

    @Test
    void testPageQuery(){
        int pageNo=1,pageSize=2;
        //1.准备分页条件
        //1.1分页条件
        Page<User> page = Page.of(pageNo, pageSize);
        //1.2排序条件
        page.addOrder(new OrderItem("balance", true)); //升序排序
        page.addOrder(new OrderItem("id", true)); //升序排序

        //2.分页查询
        Page<User> p = userService.page(page);

        //3.解析
        long total = p.getTotal();
        System.out.println("total = "+total);
        long pages = p.getPages();
        System.out.println("pages = "+pages);
        List<User> users = p.getRecords();
        users.forEach(System.out::println);
    }
}