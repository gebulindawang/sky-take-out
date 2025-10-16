package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@SuppressWarnings("all")

@Slf4j
@Service
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        //增加套餐表
        setmealMapper.insert(setmeal);
        Long id = setmeal.getId();
        List<SetmealDish> setMealDishes = setmealDTO.getSetmealDishes();
        if (setMealDishes == null || setMealDishes.size() == 0) {
            throw new RuntimeException("新增的菜品没有数据");
        }
            for (SetmealDish setmealDish : setMealDishes) {
                setmealDish.setSetmealId(id);

            }
            setmealMapper.insertWithDish(setMealDishes);
    }
}
