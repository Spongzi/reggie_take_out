package com.itheima.test;

import com.itheima.reggie.ReggieApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = ReggieApplication.class)
public class TestRedis {

    /**
     * 会有序列化问题的出现
     */
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void TestString() {
        redisTemplate.opsForValue().set("city", "bj");
    }

    /**
     * 测试String
     */
    @Test
    public void TestStringRedisTemplate() {
        stringRedisTemplate.opsForValue().set("city", "bj");

        System.out.println(stringRedisTemplate.opsForValue().get("city"));

        stringRedisTemplate.opsForValue().set("key1", "value1", 10L, TimeUnit.SECONDS);
    }

    /**
     * 测试hash
     */
    @Test
    public void TestHash() {
        HashOperations<String, Object, Object> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.put("002", "name", "张三");
        hashOperations.put("002", "age", "20");
        hashOperations.put("002", "addr", "北京");

        System.out.println(hashOperations.get("002", "name"));

        hashOperations.keys("002").forEach(System.out::println);

        // 获得所有值
        hashOperations.values("002").forEach(System.out::println);
    }

    /**
     * 操作list数据
     */
    @Test
    public void testList() {
        ListOperations<String, String> listOperations = stringRedisTemplate.opsForList();

        // 存值
        listOperations.leftPush("myList", "a");
        listOperations.leftPushAll("myList", "a", "b", "c");

        // 取值
        Objects.requireNonNull(listOperations.range("myList", 0, -1)).forEach(System.out::println);

        // 获得列表长度len
        Long size = listOperations.size("myList");
        int lSize = size.intValue();

        for (int i = lSize; i > 0; i--) {
            // 出队列
            String element = listOperations.rightPop("myList");
            System.out.println(element);
        }

    }
}
