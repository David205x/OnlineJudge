package com.oj.onlinejudge.service.impl.user.account;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oj.onlinejudge.mapper.SubmissionMapper;
import com.oj.onlinejudge.pojo.Submission;
import com.oj.onlinejudge.service.user.account.UserHeatmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserHeatmapServiceImpl implements UserHeatmapService {

    @Autowired
    private SubmissionMapper submissionMapper;

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private String timestampConversion(Timestamp timestamp) {
        return format.format(new Date(timestamp.getTime()));
    }

    @Override
    public JSONObject getHeatmapInfo(String userKey) {

        QueryWrapper<Submission> subQueryWrapper = new QueryWrapper<>();
        subQueryWrapper.orderByAsc("time");
        subQueryWrapper.eq("userkey", Integer.parseInt(userKey));
        List<Submission> submissions = submissionMapper.selectList(subQueryWrapper);

        JSONObject ret = new JSONObject();
        ArrayList<JSONObject> submissionList = new ArrayList<>();

        ArrayList<String> dateInfo = new ArrayList<>();

        for (Submission s : submissions) {
            String cur = timestampConversion(s.getTime());
            if (!dateInfo.contains(cur)) {
                dateInfo.add(cur);
            }
        }

        int[] countInfo = new int[dateInfo.size()];

        for (Submission s : submissions) {
            String cur = timestampConversion(s.getTime());
            countInfo[dateInfo.indexOf(cur)]++;
        }

        for (int i = 0; i < dateInfo.size(); i++) {
            JSONObject heatmapNode = new JSONObject();
            heatmapNode.put("date", dateInfo.get(i));
            heatmapNode.put("count", countInfo[i]);


            submissionList.add(heatmapNode);
        }

        ret.put("submissionList", submissionList);
        return ret;
    }
}
