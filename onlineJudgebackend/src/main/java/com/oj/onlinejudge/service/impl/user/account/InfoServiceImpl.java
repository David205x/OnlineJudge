package com.oj.onlinejudge.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oj.onlinejudge.mapper.UserMapper;
import com.oj.onlinejudge.pojo.User;
import com.oj.onlinejudge.service.impl.utils.UserDetailsImpl;
import com.oj.onlinejudge.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InfoServiceImpl implements InfoService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public Map<String, String> getInfo() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("id", user.getId().toString());
        map.put("username", user.getUsername());
        map.put("photo", user.getAvataruri());
        return map;
    }

    @Override
    public Map<String, String> getOthers(String userKey) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userKey);
        List<User> users = userMapper.selectList(queryWrapper);
        Map<String, String> map = new HashMap<>();
        if(users == null || users.isEmpty()){
            map.put("error_message", "not found");
            return map;
        }
        map.put("error_message", "success");
        map.put("id", users.get(0).getId().toString());
        map.put("username", users.get(0).getUsername());
        map.put("photo", users.get(0).getAvataruri());
        return map;
    }
}
