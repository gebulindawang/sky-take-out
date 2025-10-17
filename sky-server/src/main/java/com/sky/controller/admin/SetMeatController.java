package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询{}",setmealPageQueryDTO);
        PageResult pageResult = setMealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 起售停售
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("起售或停售")
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("起售停售{},{}",id,status);
        setMealService.startOrStop(status,id);
        return  Result.success();
    }

    @DeleteMapping
    @ApiOperation("批量删除套餐")
    public Result delete(@RequestParam List<Long> ids){
        setMealService.deleteBatch(ids);

        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id){
        SetmealVO setmealVO = setMealService.getById(id);

        return Result.success(setmealVO);
    }

    @PutMapping
    @ApiOperation("修改菜单")
    public Result update(@RequestBody SetmealDTO setmealDTO){
        setMealService.update(setmealDTO);
        return Result.success();
    }

}