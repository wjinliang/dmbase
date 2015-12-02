package com.dm.cms.service;

import com.dm.cms.model.CmsChannel;
import com.dm.platform.dto.TreeNode;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by cgj on 2015/11/23.
 */
public interface CmsChannelService {
    void insertCmsChannel(CmsChannel cmsChannel);

    void updateCmsChannel(CmsChannel cmsChannel);

    void deleteCmsChannel(int cmsChannelId);

    CmsChannel findOneById(int cmsChannelId);

    /**
     * 根据唯一英文简写查找频道
     *
     * @param enName
     * @return
     */
    CmsChannel findOneByEnName(String enName);

    CmsChannel findOneByPortal(String domain, String enName);

    PageInfo<CmsChannel> findCmsChannelByPage(Integer pageNum, Integer pageSize,
        CmsChannel cmsChannel);

    /**
     * 根据站点id获取频道树list
     *
     * @param siteId
     * @return
     */
    List<TreeNode> findCmsChannelTreeNodeBySiteId(int siteId);

    /**
     * 包含站点节点的频道树
     *
     * @return
     */
    List<TreeNode> findCmsChannelTreeNodeWithSiteNode();

    /**
     * 用户权限下包含站点节点的频道树
     *
     * @return
     */
    List<TreeNode> findCmsChannelTreeNodeWithSiteNodeFilterByUser();

    void updateSortOfCmsChannel(int fromCmsChannelId, int toCmsChannelId);

}
