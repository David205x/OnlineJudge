package com.oj.onlinejudge.service.impl.user;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oj.onlinejudge.mapper.SubmissionMapper;
import com.oj.onlinejudge.mapper.UserMapper;
import com.oj.onlinejudge.pojo.Submission;
import com.oj.onlinejudge.pojo.User;
import com.oj.onlinejudge.service.impl.GenericOjFilter;
import com.oj.onlinejudge.service.user.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RankServiceImpl extends GenericOjFilter implements RankService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SubmissionMapper submissionMapper;

    private class Pair{
        public Integer id;
        public String name;
        public Integer count;
        public Pair(Integer id, String name, Integer count){
            this.id = id;
            this.name = name;
            this.count = count;
        }
        @Override
        public String toString() {
            return id.toString() + name + "," + count.toString();
        }
    }

    @Override
    public JSONObject getRanking(Integer count) {
        List<User> userList = userMapper.selectList(null);
        ArrayList<Pair> pairList = new ArrayList<>();
        ArrayList<Pair> subList = new ArrayList<>();
        JSONObject ret = new JSONObject();


        ArrayList<JSONObject> userJsonList = new ArrayList<>();

        for(User u : userList) {
            QueryWrapper<Submission> submissionWrapper = new QueryWrapper<>();
            submissionWrapper.eq("userKey", u.getId());
            pairList.add(new Pair(u.getId(), u.getUsername(), Math.toIntExact(submissionMapper.selectCount(submissionWrapper))));
        }
        pairList.sort((o1, o2) -> o2.count - o1.count);

        for(int i = 0; i < count; i++){
            if(pairList.size() <= i) subList.add(new Pair(0, "", 0));
            else subList.add(pairList.get(i));
        }
        ret.put("userList", subList);
        return ret;
    }
}
