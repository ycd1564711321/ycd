package com.ycd.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ycd.bean.User;
import com.ycd.utils.StringUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:applicationContext-redis.xml")
public class UserTest {
  @Resource
  private RedisTemplate<String, Object> redisTemplate;

  @Test
  public void Test() {
    List<User> userlist = new ArrayList<User>();
    for (int i = 0; i < 50000; i++) {
      User user = new User();
      user.setId(i + 1);

      //随机汉字
      String randomChinese = StringUtils.getRandomChinese(3);
      user.setName(randomChinese);
      //随机性别
      Random random = new Random();
      String sex = random.nextBoolean() ? "男" : "女";
      user.setSex(sex);
      //随机手机号
      String randomNumber = "13" + StringUtils.getRandomNumber(9);
      user.setPhone(randomNumber);
      //随机邮箱
      int random2 = (int) (Math.random() * 20);
      int len = random2 < 3 ? random2 + 3 : random2;
      String randomStr = StringUtils.getRandomStr(len);
      String randomEmailSuffex = StringUtils.getRandomEmailSuffex();
      user.setEmail(randomStr + randomEmailSuffex);
      //随机生日
      String randomBirthday = StringUtils.randomBirthday();
      user.setBirthday(randomBirthday);

      userlist.add(user);

    }
    //jdk序列化方式
    System.out.println("jdk的序列化方式");
    long start = System.currentTimeMillis();
    BoundListOperations<String, Object> boundListOps = redisTemplate.boundListOps("jdk");
    long leftPush = 0L;
    for (User user : userlist) {
      leftPush = boundListOps.leftPush(user);
    }
    boundListOps.leftPush(userlist);
    long end = System.currentTimeMillis();
    System.out.println("条数:" + leftPush);
    System.out.println("耗时:" + (end - start) + "毫秒");

    //json序列化方式
    /*System.out.println("json的序列化方式");
    long start = System.currentTimeMillis();
    BoundListOperations<String, Object> boundListOps = redisTemplate.boundListOps("json");
    long leftPush = 0L;
    for (User user : userlist) {
      leftPush = boundListOps.leftPush(user);
    }
    boundListOps.leftPush(userlist);
    long end = System.currentTimeMillis();
    System.out.println("条数:" + leftPush);
    System.out.println("耗时:" + (end - start) + "毫秒");*/

    //hash序列化方式
    /*System.out.println("hash的序列化方式");
    long start = System.currentTimeMillis();
    BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps("hash");
    for (User user : userlist) {
      boundHashOps.put("hash", user);
    }
    boundHashOps.put("hash", userlist);
    long end = System.currentTimeMillis();
    //System.out.println("条数:" + leftPush);
    System.out.println("耗时:" + (end - start) + "毫秒");*/
  }
}
