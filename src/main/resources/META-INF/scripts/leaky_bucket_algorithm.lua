-- 限流的资源 key
local resource_key = KEYS[1]
-- 流出的起始时间(上一次漏水时间)
local leaking_time_key = KEYS[2]

-- 漏桶容量
local capacity = tonumber(ARGV[1])
-- 漏水速率
local leaking_rate = tonumber(ARGV[2])
-- 时间戳，由程序传入毫秒数
local now_time = tonumber(ARGV[3])
-- ttl
local leaking_over_time = capacity / leaking_rate
local ttl = math.floor(leaking_over_time * 2)

-- 漏斗剩余空间
local left_quota = tonumber(redis.call("get", resource_key))
if left_quota == nil then
    left_quota = capacity
end
-- 上一次漏水时间
local leaking_time = tonumber(redis.call("get", leaking_time_key))
if leaking_time == nil then
    leaking_time = now_time
end

-- 过去的时间
local delta_time = now_time - leaking_time
-- 腾出的空间
local delta_quota = delta_time * leaking_rate / 1000

if delta_quota < 0 then
    left_quota = capacity
    leaking_time = now_time
end

-- 漏斗剩余空间计算
left_quota = left_quota + delta_quota
leaking_time = now_time

if left_quota > capacity then
    left_quota = capacity
end

local allowed_num = 0
if left_quota >= 1 then
    left_quota = left_quota - 1
    allowed_num = 1
end

redis.call("setex", resource_key, ttl, left_quota)
redis.call("setex", leaking_time_key, ttl, leaking_time)

return allowed_num



