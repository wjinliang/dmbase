package com.dm.cms.sqldao;

import com.dm.cms.model.CmsContent;

import java.util.List;
import java.util.Map;

public interface CmsContentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_content
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_content
     *
     * @mbggenerated
     */
    int insert(CmsContent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_content
     *
     * @mbggenerated
     */
    int insertSelective(CmsContent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_content
     *
     * @mbggenerated
     */
    CmsContent selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_content
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CmsContent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_content
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(CmsContent record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_content
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CmsContent record);

    List<CmsContent> selectRecordByArgMap(Map argMap);
}