package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.enums.UserStatus;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    /*扣减用户余额*/
    @Override
    @Transactional
    public void deductBalance(Long id, Integer money) {
        //1.查询用户
        User user = getById(id);
        //2.校验用户状态
        if(user == null || user.getStatus() == UserStatus.FROZEN){
            throw new RuntimeException("用户状态异常！");
        }
        //3.校验余额是否充足
        if(money > user.getBalance()){
            throw new RuntimeException("用户余额不足！");
        }
        //4.扣减余额 update tb_user set balance = balance - ?
        /*baseMapper.deductBalance(id,money);*/
        int remainBalance = user.getBalance() - money;
        lambdaUpdate()
                .set(User::getBalance,remainBalance)
                .set(remainBalance==0,User::getStatus,UserStatus.FROZEN)
                .eq(User::getId,id)
                .eq(User::getBalance,user.getBalance()) //乐观锁
                .update();
    }

    /*根据复杂条件查询用户*/
    @Override
    public List<User> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance) {
        return lambdaQuery()
                .like(name!=null,User::getUsername,name)
                .eq(status!=null,User::getStatus,status)
                .ge(minBalance!=null,User::getBalance,minBalance)
                .le(maxBalance!=null,User::getBalance,maxBalance)
                .list();
    }

    @Override
    public UserVO queryUserAndAddressById(Long id) {
        //1.查询用户
        User user = getById(id);
        if(user==null || user.getStatus()==UserStatus.FROZEN){
            throw new RuntimeException("用户状态异常！");
        }
        //2.查询地址
        List<Address> addresses = Db.lambdaQuery(Address.class).eq(Address::getUserId, id).list();
        //3.封装VO
        //3.1转User的PO为VO
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        //3.2转地址VO
        if(CollUtil.isNotEmpty(addresses)){
            userVO.setAddresses(BeanUtil.copyToList(addresses, AddressVO.class));
        }
        return userVO;
    }

    @Override
    public List<UserVO> queryUserAndAddressByIds(List<Long> ids) {
        //1.查询用户
        List<User> users = listByIds(ids);
        if(CollUtil.isNotEmpty(users)){
            return Collections.emptyList();
        }
        //2.查询地址
        //2.1获取用户id集合
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        //2.2根据用户id查询地址
        List<Address> addresses = Db.lambdaQuery(Address.class).in(Address::getUserId, userIds).list();
        //2.3转换地址VO
        List<AddressVO> addressVOList = BeanUtil.copyToList(addresses, AddressVO.class);
        //2.4用户地址集合分组处理，相同用户的放入一个集合（组）中
        Map<Long,List<AddressVO>> addressMap = new HashMap<>(0);
        if(CollUtil.isNotEmpty(addresses)){
            addressMap = addressVOList.stream().collect(Collectors.groupingBy(AddressVO::getUserId));
        }

        //3.转换VO返回
        List<UserVO> list = new ArrayList<>(users.size());
        for(User user : users){
            //3.1转换User的PO为VO
            UserVO vo = BeanUtil.copyProperties(user, UserVO.class);
            //3.2转换地址VO
            vo.setAddresses(addressMap.get(user.getId()));
        }

        return list;
    }
}
