package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("all")


@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;

    public static final String key = "SHOP_STAUS";

    /**
     *设置店铺营业状态
     * @param status
     * @return
     */
    @GetMapping
    @ApiOperation("获取店铺营业状态")
    public Result getStatus(){
        Integer status =(Integer) redisTemplate.opsForValue().get(key);
        log.info("获取店铺的营业状态{}",status == 1 ? "营业中":"已打烊");
        return Result.success(status);
    }
}