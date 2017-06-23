package com.qczl.websocket.filter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qczl.websocket.util.RedisUtil;
import com.qczl.websocket.constans.RedisConstans;
import com.qczl.websocket.dao.SensitivewordDao;
import com.qczl.websocket.entity.Sensitiveword;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class SensitivewordFilter {

    @Autowired
    private SensitivewordDao sensitivewordDao;

    @Autowired
    private RedisUtil redisUtil;

    @PostConstruct
    private List<Sensitiveword> init(){
        List<Sensitiveword> list = sensitivewordDao.selectAll();

        if(CollectionUtils.isEmpty(list)) return null;

        //缓存1天
        redisUtil.del(RedisConstans.SENSITIVEWORD_LIST);
        redisUtil.set(RedisConstans.SENSITIVEWORD_LIST, JSON.toJSONString(list),RedisConstans.ONE_DAYS);
        return list;
    }

    public String doFilter(String context){

        //取redis
        String str = redisUtil.get(RedisConstans.SENSITIVEWORD_LIST);
        List<Sensitiveword> list ;
        if (StringUtils.isBlank(str)){
            //没取到 重新去db拉取 填充redis
            list = init();
        }else{
            list = JSONObject.parseArray(str,Sensitiveword.class);
        }

        //未从db或redis获取到关键词数据即不过滤
        if (CollectionUtils.isEmpty(list)) return context;


        for (Sensitiveword sensitiveword: list) {
            if (context.contains(sensitiveword.getKeenness())){
                context = StringUtils.replace(context,sensitiveword.getKeenness(),sensitiveword.getSubstitution());
            }
        }

        return context;
    }
}
