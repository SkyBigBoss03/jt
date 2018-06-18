package com.jt.test.redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class TestRedis {
	
	/**
	 * host主机:ip地址
	 * 端口号: 6379
	 */
	//@Test
	public void test01(){
		Jedis jedis = new Jedis("192.168.126.151", 6379);
		jedis.set("1712","tomcat猫");	
		System.out.println("从redis中获取数据:"+jedis.get("1712"));	
	}
	
	//redis测试分片
	//@Test
	public void test02(){
		//2.创建分片的连接池
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(500);
		poolConfig.setMaxIdle(20);
		//3.准备redis的分片
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("192.168.126.151", 6379));
		shards.add(new JedisShardInfo("192.168.126.151", 6380));
		shards.add(new JedisShardInfo("192.168.126.151", 6381));
		//1.创建分片的对象
		ShardedJedisPool jedisPool = 
				new ShardedJedisPool(poolConfig, shards);
		//获取jedis对象
		ShardedJedis shardedJedis = jedisPool.getResource();
		//5.redis的存取值操作
		shardedJedis.set("fgffdgf","我是分片操作");
		System.out.println("获取分片的数据:"+shardedJedis.get("1712"));
	}
	
	
	//测试哨兵
	@Test
	public void test03(){
		//准备哨兵的Set集合   ip:端口
		Set<String> sets = new HashSet<String>();
		sets.add("192.168.126.151:26379");
		sets.add("192.168.126.151:26380");
		sets.add("192.168.126.151:26381");
		JedisSentinelPool pool = 
				new JedisSentinelPool("mymaster", sets);
		//获取jedis对象
		Jedis jedis = pool.getResource();
		jedis.set("1712","1712今天天气不错");
		System.out.println("获取redis数据:"+jedis.get("1712"));	
	}
	
	//集群测试类
	@Test
	public void testCluster(){
	/*	HostAndPort nodes1 = new HostAndPort("ip", 端口);
		HostAndPort nodes1 = new HostAndPort("ip", 端口);
		HostAndPort nodes1 = new HostAndPort("ip", 端口);
		HostAndPort nodes1 = new HostAndPort("ip", 端口);
		HostAndPort nodes1 = new HostAndPort("ip", 端口);
		HostAndPort nodes1 = new HostAndPort("ip", 端口);
		HostAndPort nodes1 = new HostAndPort("ip", 端口);
		HostAndPort nodes1 = new HostAndPort("ip", 端口);
		HostAndPort nodes1 = new HostAndPort("ip", 端口);
		HostAndPort nodes1 = new HostAndPort("ip", 端口);
		HostAndPort nodes1 = new HostAndPort("ip", 端口);
		//ip,端口
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(nodes1);
		nodes.add(nodes1);
		nodes.add(nodes1);
		nodes.add(nodes1);
		nodes.add(nodes1);
		nodes.add(nodes1);
		nodes.add(nodes1);
		nodes.add(nodes1);
		nodes.add(nodes1);
		nodes.add(nodes1);
		
		JedisCluster jedisCluster = 
				new JedisCluster(nodes, poolConfig);
		
		jedisCluster.get(key)
		jedisCluster.set(key, value);*/
	}
	
	
	
	
	
	
}
