package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;
import lombok.Data;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);


    @AutoFill(value = OperationType.INSERT)
    void insert(Setmeal setmeal);


    void insertWithDish(List<SetmealDish> setmealDishes);

    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    @Update("update setmeal set status = #{status} where id = #{id}")
    void startOrStop(Integer status, Long id);

    void deleteBatchSetmeal(List<Long> ids);

    @Select("select status from setmeal where id = #{id}")
    Setmeal getStatusById(Long id);

    @Select("select * from setmeal where  id = #{id}")
    SetmealVO getById(Long id);

    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getByIdWithDishes(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);

    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void deleteBatchWithDish(Long id);

}
