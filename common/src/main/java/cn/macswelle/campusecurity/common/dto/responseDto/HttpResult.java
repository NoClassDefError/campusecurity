package cn.macswelle.campusecurity.common.dto.responseDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * 用于后端向前端发送结果信息，转成json字符串
 */
public class HttpResult {
    public HashMap<String, String> result = new HashMap<>();

    /**
     * 单个字符串中包含一个键值对，之间用冒号隔开，例如：</br>
     * ResultFactory("result:success","this:error")
     *
     * @param strings 多个键值对
     */
    public HttpResult(String... strings) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        for (String s : strings) {
            String[] kv = s.split(":");
            result.put(kv[0], kv[1]);
        }
        logger.info(result.toString());
    }

    public HttpResult(String string){
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info(string);
        String[] kv1 = string.split("---");
        for(String s:kv1){
            String[] kv = s.split(":");
            result.put(kv[0], kv[1]);
        }
    }
}
