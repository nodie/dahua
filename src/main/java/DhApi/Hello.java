package DhApi;

import redis.clients.jedis.Jedis;

public class Hello {
    private Jedis jedis = new RedisTools().getJedis();
    public static void main(String[] argv) {
        Hello hello = new Hello();
        System.out.println(hello.jedis.hmget("DH0-0011000000", "sim_id"));
        System.out.println(hello.jedis.hmget("DH0-0011000000", "car_no"));
    }
}
