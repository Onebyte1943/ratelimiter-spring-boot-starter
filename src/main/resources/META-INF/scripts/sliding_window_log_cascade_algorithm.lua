-- 限流的资源 key
local resource_key = KEYS[1]
-- 一级限流数目
local first_count = tonumber(ARGV[1])
-- 一级限流时间窗
local first_period = tonumber(ARGV[2])
-- 二级限流数目
local second_count = tonumber(ARGV[3])
-- 二级限流时间窗
local second_period = tonumber(ARGV[4])
-- 时间戳，由程序传入毫秒数
local current_time = tonumber(ARGV[5])

-- 存入 zset
redis.call('zadd', resource_key, current_time, tostring(current_time))

local first_current_count = redis.call('zcount', resource_key, current_time - first_period * 1000, current_time)
local second_current_count = redis.call('zcount', resource_key, current_time - second_period * 1000, current_time)

-- 设置过期时间
redis.call('expire', resource_key, second_period + 1)

-- 一级流控判断
if first_current_count > first_count then
    return 0
else
    return 1
end

-- 二级流控判断
if second_current_count > second_count then
    return 0
else
    return 2
end
