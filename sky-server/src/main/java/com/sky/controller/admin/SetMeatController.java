package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@SuppressWarnings("all")
@Api(tags = "套餐相关接口")
@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetMeatController {
    @Autowired
    private SetMealService setMealService;

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增套餐")
    public Result save(@RequestBody SetmealDTO setmealDTO) {

        setMealService.saveWithDish(setmealDTO);
        log.info("新增套餐中的事务{}",setmealDTO.getSetmealDishes());
        return Result.success();
    }



}