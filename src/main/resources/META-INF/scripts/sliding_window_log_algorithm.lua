-- 限流的资源 key
local resource_key = KEYS[1]
-- 限流数目
local count = tonumber(ARGV[1])
-- 限流时间窗
local period = tonumber(ARGV[2])
-- 时间戳，由程序传入毫秒数
local current_time = tonumber(ARGV[3])

-- 存入 zset
redis.call('zadd', resource_key, current_time, tostring(current_time))
-- 移除掉所统计时间窗之前的记录
redis.call('zremrangebyscore', resource_key, 0, current_time - period * 1000)
-- 统计时间窗内的记录数目
local current_count = tonumber(redis.call('zcard', resource_key))
-- 设置过期时间
redis.call('expire', resource_key, period + 1)
-- 是否超过限制
if current_count > count then
    return 0
else
    return current_count
end