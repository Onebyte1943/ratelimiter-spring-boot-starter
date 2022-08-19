-- 限流资源 key
local resource_key = KEYS[1]
-- 限流数目
local count = tonumber(ARGV[1])
-- 限流周期
local period = tonumber(ARGV[2])
-- 查询当前统计的数目，若不存在，赋默认值 0
local current = tonumber(redis.call('get', resource_key) or "0")

-- 如果超出限流大小
current = current + 1
if current > count then
    return 0
else
    redis.call("incr", resource_key)
    if current == 1 then
        redis.call("expire", resource_key, period)
    end
    return 1
end