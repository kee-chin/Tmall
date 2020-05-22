package com.chinkee.tmall.mapper;

import com.chinkee.tmall.pojo.Property;
import com.chinkee.tmall.pojo.PropertyExample;
import java.util.List;

public interface PropertyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table property
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table property
     *
     * @mbg.generated
     */
    int insert(Property record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table property
     *
     * @mbg.generated
     */
    int insertSelective(Property record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table property
     *
     * @mbg.generated
     */
    List<Property> selectByExample(PropertyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table property
     *
     * @mbg.generated
     */
    Property selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table property
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Property record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table property
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Property record);
}