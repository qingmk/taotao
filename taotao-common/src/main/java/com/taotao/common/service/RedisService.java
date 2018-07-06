package com.taotao.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class RedisService {
	@Autowired(required=false)
	private ShardedJedisPool shardedJedisPool;
	/**
	 * 公用代码
	 * @param fun
	 * @return
	 */
	public <T> T execute(Function<T,ShardedJedis> fun) {
		
		ShardedJedis shardedJedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis = shardedJedisPool.getResource();

            return fun.callback(shardedJedis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != shardedJedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }
		
		return null;
		
	}
	
	
	/**
	 * redis设置值
	 * @param key
	 * @param value
	 * @return
	 */
	/*public String set(String key,String value) {
		ShardedJedis shardedJedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis = shardedJedisPool.getResource();

            return shardedJedis.set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != shardedJedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }
        return null;
	}*/
	public String set(String key, String value) {
		return this.execute(new Function<String, ShardedJedis>() {

			@Override
			public String callback(ShardedJedis e) {
			
				return e.set(key, value);
			}
		});
		
		
	}
	/**
	 * redis set 方法
	 * @param key
	 * @return
	 */
	/*public String get(String key) {
		ShardedJedis shardedJedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            shardedJedis = shardedJedisPool.getResource();

            return shardedJedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != shardedJedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                shardedJedis.close();
            }
        }
        return null;
	}*/
	public String get( String key) {
		return this.execute(new Function<String, ShardedJedis>() {

			@Override
			public String callback(ShardedJedis e) {
			
				return e.get(key);
			}
		});
		
	}
	/**
	 * redis删除值
	 * @param key
	 * @return
	 */
	public Long del(String key) {
		return this.execute(new Function<Long, ShardedJedis>() {

			@Override
			public Long callback(ShardedJedis e) {
				// TODO Auto-generated method stub
				return e.del(key);
			}
		});
	}
	/**
	 * 设置生存时间
	 * @param key
	 * @param seconds
	 * @return
	 */
	public Long expire(String key,Integer seconds) {
		return this.execute(new Function<Long, ShardedJedis>() {

			@Override
			public Long callback(ShardedJedis e) {
				// TODO Auto-generated method stub
				return e.expire(key, seconds);
			}
		});
	}
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String set(String key, String value,Integer seconds) {
		return this.execute(new Function<String, ShardedJedis>() {

			@Override
			public String callback(ShardedJedis e) {
			
				String result= e.set(key, value);
				e.expire(key, seconds);
				return result;
			}
		});
		
		
	}
	
}
