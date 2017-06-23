import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qczl.websocket.dao.SensitivewordDao;
import com.qczl.websocket.dto.PageDto;
import com.qczl.websocket.entity.Sensitiveword;
import com.qczl.websocket.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import java.util.List;

@ContextConfiguration(locations = "classpath:spring/spring*.xml")



@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
public class SensitivewordTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SensitivewordDao sensitivewordDao;

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void test(){


        Page<Sensitiveword> page = PageHelper.startPage(2,10);

        List<Sensitiveword> list =  sensitivewordDao.selectAll();

        PageDto<Sensitiveword> pd = new PageDto<>();
        pd.setList(list);
        pd.setTotalCount(page.getTotal());

        logger.info("pageDto is {}",JSON.toJSONString(pd));
    }

    @Test
    public void  sortedSet() {
        System.out.println( "==SoretedSet==" );
        Jedis jedis = redisUtil.getJedis();
        try  {
            jedis.zadd( "hackers" ,  1 ,  "Alan Kay" );
            jedis.zadd( "hackers" ,  4 ,  "Richard Stallman");
            jedis.zadd( "hackers" ,  5 ,  "Yukihiro Matsumoto");
            jedis.zadd( "hackers" ,  6 ,  "Claude Shannon");
            jedis.zadd( "hackers" ,  3 ,  "Linus Torvalds");
            jedis.zadd( "hackers" ,  2 ,  "Alan Turing" );
//            Set<String> setValues = jedis.zrange( "hackers" ,  0, - 1 );
//            System.out.println(setValues);
//            Set<String> setValues2 = jedis.zrevrange( "hackers",  0 , - 1 );
//            System.out.println(setValues2);
            System.out.println(jedis.zrangeByScore("hackers",1,2));
            System.out.println(jedis.zrevrangeByScore("hackers",3,1));


        }  catch  (Exception e) {
            e.printStackTrace();
        }  finally  {
           jedis.close();
        }

        // 清空数据
//        System.out.println(jedis.flushDB());
//        // 添加数据
//        jedis.zadd( "zset" ,  10.1 ,  "hello" );
//        jedis.zadd( "zset" ,  10.0 ,  ":" );
//        jedis.zadd( "zset" ,  9.0 ,  "zset" );
//        jedis.zadd( "zset" ,  11.0 ,  "zset!" );
//        // 元素个数
//        System.out.println(jedis.zcard( "zset" ));
//        // 元素下标
//        System.out.println(jedis.zscore( "zset" ,  "zset" ));
//        // 集合子集
//        System.out.println(jedis.zrange( "zset" ,  0 , - 1 ));
//        // 删除元素
//        System.out.println(jedis.zrem( "zset" ,  "zset!" ));
//
//
//        System.out.println(jedis.zcount( "zset" ,  9.5 ,  10.5 ));
//        // 整个集合值
//        System.out.println(jedis.zrange( "zset" ,  0 , - 1 ));
    }
}
