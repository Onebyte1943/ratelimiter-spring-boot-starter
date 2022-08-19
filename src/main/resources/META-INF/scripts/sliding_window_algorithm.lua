-- 限流的资源 key
local resource_key = KEYS[1]
-- 上一次滑动时间
local sliding_time_key = KEYS[2]

-- 限流数目
local count = tonumber(ARGV[1])
-- 限流时间窗
local period = tonumber(ARGV[2])
-- 划分子窗口数目
local child_window_num = tonumber(ARGV[3])
-- 时间戳，由程序传入毫秒数
local current_time = tonumber(ARGV[4])

-- 存入 zset
redis.call('zadd', resource_key, current_time, tostring(current_time))

-- 计算窗口的开始时刻和结束时刻，滑动
-- 子窗口大小
local child_window_size = math.ceil(period * 1000 / child_window_num)

-- 上一次滑动时间
local sliding_time = tonumber(redis.call("get", sliding_time_key))
if sliding_time == nil then
    sliding_time = current_time - period * 1000
end

local delta = math.floor((current_time - period * 1000 - sliding_time) / child_window_size)
if delta < 0 then
    delta = 0
end
-- 窗口的开始和结束
local window_start = sliding_time + delta * child_window_size
local window_end = window_start + period * 1000

-- 移除掉所统计时间窗之前的记录，节约空间和时间
--redis.call('zremrangebyscore', resource_key, 0, window_start)
-- 统计时间窗内的记录数目
local current_count = tonumber(redis.call('zcount', resource_key, window_start, window_end))

-- 设置过期时间
local ttl = period + 1
-- 重置滑动时间
sliding_time = window_start
redis.call('expire', resource_key, ttl)
redis.call("setex", sliding_time_key, ttl, sliding_time)

-- 是否超过限制
if current_count > count then
    return 0
else
    return current_count
end