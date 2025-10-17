package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@SuppressWarnings("all")
@Mapper
public interface SetMealDishMapper {
    /**
     * 根据菜品id查询对应的套餐id
     * @param dishIds
     * @return
     */
    List<Long> getSetMealIdsByDishIds(List<Long> dishIds);


    void deleteBatchWithDish(List<Long> ids);
}
