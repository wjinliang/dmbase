package com.dm.cms.service.impl;

import com.dm.cms.model.CmsChannel;
import com.dm.cms.service.CmsChannelService;
import com.dm.cms.sqldao.CmsChannelMapper;
import com.dm.cms.sqldao.CmsSiteMapper;
import com.dm.platform.dto.TreeNode;
import com.dm.platform.util.UserAccountUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cgj on 2015/11/24.
 */
@Service public class CmsChannelServiceImpl implements CmsChannelService {
    @Autowired CmsChannelMapper cmsChannelMapper;
    @Autowired CmsSiteMapper cmsSiteMapper;

    @Override public void insertCmsChannel(CmsChannel cmsChannel) {
        cmsChannelMapper.insertSelective(cmsChannel);
        cmsChannel.setSeq(cmsChannel.getId());
        updateCmsChannel(cmsChannel);
    }

    @Override public void updateCmsChannel(CmsChannel cmsChannel) {
        cmsChannelMapper.updateByPrimaryKeySelective(cmsChannel);
    }

    @Override public void deleteCmsChannel(int cmsChannelId) {
        cmsChannelMapper.deleteByPrimaryKey(cmsChannelId);
    }

    @Override public CmsChannel findOneById(int cmsChannelId) {
        return cmsChannelMapper.selectByPrimaryKey(cmsChannelId);
    }

    @Override public CmsChannel findOneByEnName(String enName) {
        return cmsChannelMapper.selectByEnName(enName);
    }

    @Override public CmsChannel findOneByPortal(String domain, String enName) {
        return cmsChannelMapper.selectByDomainAndEnName(domain, enName);
    }

    @Override public PageInfo<CmsChannel> findCmsChannelByPage(Integer pageNum, Integer pageSize,
        CmsChannel cmsChannel) {
        Map map = new HashMap();
        map.put("model", cmsChannel);
        PageHelper.startPage(pageNum, pageSize);
        List<CmsChannel> list = cmsChannelMapper.selectRecordsByArgMap(map);
        PageInfo<CmsChannel> page = new PageInfo<CmsChannel>(list);
        return page;
    }

    @Override public List<TreeNode> findCmsChannelTreeNodeBySiteId(int siteId) {
        Map map = new HashMap();
        CmsChannel model = new CmsChannel();
        model.setSiteId(siteId);
        map.put("model", model);
        return cmsChannelMapper.selectTreeNodes(map);
    }

    @Override public List<TreeNode> findCmsChannelTreeNodeWithSiteNode() {
        Map channelArgMap = new HashMap();
        List<TreeNode> channelNodes = cmsChannelMapper.selectTreeNodes(channelArgMap);
        for (TreeNode treeNode : channelNodes) {
            if (treeNode.getpId() == 0) {
                treeNode.setpId(-treeNode.getI());
            }
            ;
        }
        Map siteArgMap = new HashMap();
        List<TreeNode> siteNodes = cmsSiteMapper.selectTreeNodesByArgMap(siteArgMap);
        for (TreeNode siteNode : siteNodes) {
            siteNode.setpId(0);
            siteNode.setId(-siteNode.getId());
            Collections.addAll(channelNodes, siteNode);
        }
        return channelNodes;
    }

    @Override public List<TreeNode> findCmsChannelTreeNodeWithSiteNodeFilterByUser() {
        String currentUserName = UserAccountUtil.getInstance().getCurrentUser();

        return null;
    }

    @Override public void updateSortOfCmsChannel(int fromCmsChannelId, int toCmsChannelId) {

    }
}
