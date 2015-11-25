package com.dm.cms.sqldao;

import com.dm.cms.model.CmsSite;
import com.dm.platform.dto.TreeNode;

import java.util.List;
import java.util.Map;

public interface CmsSiteMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_site
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_site
     *
     * @mbggenerated
     */
    int insert(CmsSite record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_site
     *
     * @mbggenerated
     */
    int insertSelective(CmsSite record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_site
     *
     * @mbggenerated
     */
    CmsSite selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_site
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CmsSite record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_site
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CmsSite record);

    List<CmsSite> selectRecordsByArgMap(Map argMap);

    CmsSite selectByDomain(String domain);

    List<TreeNode> selectTreeNodesByArgMap(Map argMap);
}
