package com.oj.onlinejudge.service.checker;

import java.util.Map;
/**
 * @author luo'xing'yue
 * @CreateTime 2022-09-23
 * @Response 函数是拼接代码的方法 返回值包括 “error_message", "ParsedCodeString"
 * @insertCode 是插入的代码段
 * @ParseCode 作为返回值”间隔"与 @insertCode 进行拼接
 * 例如 insertCode[0] ParseCode[0](间隔1) insertCode[1] ParseCode[1](间隔2) insertCode[2] ParseCode[2](间隔3) insertCode[3]
 */
public interface CodeParser {
    String[] ParseCode(String srcCode, String reg);                                     // 根据输入规则解析字符串
    Map<String, String> Response(String srcCode, String reg, String[] insertCode);      // 根据输入规则以及需要插入的代码段返回最终结果

}
