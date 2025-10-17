package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetMealDishMapper;
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
    @Autowired
    private SetMealDishMapper setMealDishMapper;

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

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);


        return new PageResult(page.getTotal(),page.getResult());
    }


    /**
     * 批量起售停售
     * @param status
     * @param ids
     */
    public void startOrStop(Integer status, Long id) {
        setmealMapper.startOrStop(status,id);
    }

    /**
     * 批量删除
     * @param ids
     */
    public void deleteBatch(List<Long> ids) {
        if (ids == null || ids.size() == 0) {
            throw new DeletionNotAllowedException("请选择你要删除的套餐");
        }

        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getStatusById(id);
            if (setmeal.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        setmealMapper.deleteBatchSetmeal(ids);
        setMealDishMapper.deleteBatchWithDish(ids);
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    public SetmealVO getById(Long id) {
        SetmealVO setmealVO = setmealMapper.getById(id);
        List<SetmealDish> dishes = setmealMapper.getByIdWithDishes(id);
        log.info("taocan{}",dishes);
        setmealVO.setSetmealDishes(dishes);
        return setmealVO;
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
       setmealMapper.update(setmeal);
       setmealMapper.deleteBatchWithDish(setmealDTO.getId());
       setmealMapper.insertWithDish(setmealDTO.getSetmealDishes());
    }
}
