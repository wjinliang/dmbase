package com.dm.cms.sqldao;

import com.dm.cms.model.CmsChannel;
import com.dm.platform.dto.TreeNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CmsChannelMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_channel
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_channel
     *
     * @mbggenerated
     */
    int insert(CmsChannel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_channel
     *
     * @mbggenerated
     */
    int insertSelective(CmsChannel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_channel
     *
     * @mbggenerated
     */
    CmsChannel selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_channel
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CmsChannel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cms_channel
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CmsChannel record);

    List<CmsChannel> selectRecordsByArgMap(Map argMap);

    List<TreeNode> selectTreeNodes(Map argMap);

    CmsChannel selectByEnName(String enName);

    CmsChannel selectByDomainAndEnName(@Param(value = "domain") String domain,
        @Param(value = "enName") String enName);
}
