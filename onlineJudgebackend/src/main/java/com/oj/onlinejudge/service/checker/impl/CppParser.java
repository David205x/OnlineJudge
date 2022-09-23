package com.oj.onlinejudge.service.checker.impl;

import com.oj.onlinejudge.service.checker.CodeParser;

import java.util.HashMap;
import java.util.Map;

public class CppParser implements CodeParser {
    @Override
    public String[] ParseCode(String srcCode, String reg) {
        try{
            String[] injector = srcCode.split(reg);
            if(injector.length <= 1) return null;
            return injector;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String, String> Response(String srcCode, String reg, String[] insertCode) {
        String[] interval = ParseCode(srcCode, reg);
        Map<String, String> res = new HashMap<>();
        if(interval == null){
            res.put("error_message", "CompileError");
            res.put("ParsedCodeString", "CompileError");
            return res;
        }
        StringBuilder ParsedCodeBuilder = new StringBuilder();
        for (int i = 0; i < insertCode.length; i++) {
            ParsedCodeBuilder.append(insertCode[i]);
            if(i < interval.length) ParsedCodeBuilder.append(interval[i]);
        }
        if(insertCode.length < interval.length){
            for (int i = insertCode.length; i < interval.length; i++) {
                ParsedCodeBuilder.append(interval[i]);
            }
        }
        String ParsedCode = ParsedCodeBuilder.toString();

        res.put("ParsedCodeString", ParsedCode);
        res.put("error_message", "success");
        return res;
    }
}
