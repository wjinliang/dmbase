<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath =
            request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + path + "/";
%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <meta charset="utf-8"/>
    <title>DMCMS | 频道管理</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta content="width=device-width, initial-scale=1" name="viewport"/>
    <meta content="" name="description"/>
    <meta content="" name="author"/>
    <%@include file="../../includejsps/style.jsp" %>
    <%@include file="../../includejsps/plugin-style.jsp" %>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<!-- DOC: Apply "page-header-menu-fixed" class to set the mega menu fixed  -->
<!-- DOC: Apply "page-header-top-fixed" class to set the top menu fixed  -->
<body>
<!-- BEGIN HEADER -->
<%@include file="../../includejsps/head.jsp" %>
<!-- END HEADER -->
<!-- BEGIN PAGE CONTAINER -->
<div class="page-container">
    <!-- BEGIN PAGE HEAD -->
    <div class="page-head">
        <div class="container">
            <!-- BEGIN PAGE TITLE -->
            <div class="page-title">
                <h1>频道管理</h1>
            </div>
            <!-- END PAGE TITLE -->
            <!-- BEGIN PAGE TOOLBAR -->
            <%@include file="../../includejsps/toolbar.jsp" %>
            <!-- END PAGE TOOLBAR -->
        </div>
    </div>
    <!-- END PAGE HEAD -->
    <!-- BEGIN PAGE CONTENT -->
    <div class="page-content">
        <div class="container">
            <!-- BEGIN PAGE CONTENT INNER -->
            <div class="row margin-top-10">
                <div class="col-md-4 col-sm-12">
                    <div class="row">
                        <div class="col-md-12 col-sm-12">
                            <!-- BEGIN PORTLET-->
                            <div class="portlet light">
                                <div class="portlet-title">
                                    <div class="caption caption-md">
                                        <i class="icon-bar-chart theme-font hide"></i> <span
                                            class="caption-subject theme-font bold uppercase">站点</span>
                                        <span class="caption-helper"></span>
                                    </div>
                                </div>
                                <div class="portlet-body">
                                    <div class="scroller" style="height:200px;" id="site_tree_div"
                                         data-always-visible="1" data-rail-visible="1">
                                        <ul id="site_tree" class="ztree"></ul>
                                    </div>
                                </div>
                            </div>
                            <!-- END PORTLET-->
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12 col-sm-12">
                            <!-- BEGIN PORTLET-->
                            <div class="portlet light">
                                <div class="portlet-title">
                                    <div class="caption caption-md">
                                        <i class="icon-bar-chart theme-font hide"></i> <span
                                            class="caption-subject theme-font bold uppercase">频道树</span>
                                        <span class="caption-helper"></span>
                                    </div>
                                </div>
                                <div class="portlet-body">
                                    <div class="scroller" style="height:200px;"
                                         id="channel_tree_div"
                                         data-always-visible="1" data-rail-visible="1">
                                        <ul id="channel_tree" class="ztree"></ul>
                                    </div>
                                </div>
                            </div>
                            <!-- END PORTLET-->
                        </div>
                    </div>
                </div>

                <div class="col-md-8 col-sm-12">
                    <!-- BEGIN PORTLET-->
                    <div class="portlet light">
                        <div class="portlet-title">
                            <div class="caption caption-md">
                                <i class="icon-bar-chart theme-font hide"></i> <span
                                    class="caption-subject theme-font bold uppercase">频道列表</span>
                                <span class="caption-helper"></span>
                            </div>
                        </div>
                        <div class="portlet-body" id="channel_grid"></div>
                    </div>
                    <!-- END PORTLET-->
                </div>
            </div>
            <!-- END PAGE CONTENT INNER -->
        </div>
    </div>
    <!-- END PAGE CONTENT -->
</div>
<!-- END PAGE CONTAINER -->
<!-- BEGIN FOOTER -->
<%@include file="../../includejsps/foot.jsp" %>
<!-- END FOOTER-->
<!-- BEGIN JAVASCRIPTS-->
<%@include file="../../includejsps/js.jsp" %>
<%@include file="../../includejsps/plugin-js.jsp" %>
<script type="text/javascript">
    var siteZTree;
    var channelTree;
    var currentSiteId;
    var siteSetting = {
        view: {
            showIcon: false,
            selectedMulti: false
        },
        edit: {
            enable: false,
            showRemoveBtn: false,
            showRenameBtn: false
        },
        check: {
            enable: false
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        async: {
            enable: true,
            dataType: "json",
            url: "../site/tree",
            autoParam: ["id", "name", "pId"]
        },
        callback: {
            onAsyncSuccess: function (event, treeId, treeNode, msg) {
                if (msg.length == 0) {
                    Metronic.alert({
                        message: "<a href='../site/page'>请先新建站点！</a>",
                        type: "warning",
                        container: "#site_tree_div",
                        place: "prepend",
                        close: false,
                        closeInSeconds : 5
                    });
                }
            },
            onClick: function (event, treeId, treeNode) {
                if (typeof(channelTree) == "undefined") {
                    $.fn.zTree.init($("#channel_tree"), channelSetting, "");
                    channelTree = $.fn.zTree.getZTreeObj("channel_tree");
                }
                currentSiteId = treeNode.id;
                channelTree.setting.async.url = "./tree?siteId=" + currentSiteId;
                channelTree.reAsyncChildNodes(null, "refresh");
            }
        }
    };
    var channelSetting = {
        view: {
            showIcon: false,
            selectedMulti: false
        },
        edit: {
            enable: false,
            showRemoveBtn: false,
            showRenameBtn: false
        },
        check: {
            enable: false
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        async: {
            enable: true,
            dataType: "json",
            url: "",
            autoParam: ["id", "name", "pId"]
        },
        callback: {
            beforeAsync: function (treeId, treeNode) {
                var nodes = siteZTree.getSelectedNodes();
                if (nodes[0] == null) {
                    return false;
                }
                return true;
            },
            onAsyncSuccess: function (event, treeId, treeNode, msg) {
                if (msg.length == 0) {
                    Metronic.alert({
                        message: "该站点下暂无频道！",
                        type: "warning",
                        container: "#channel_tree_div",
                        place: "prepend",
                        close: false,
                        closeInSeconds : 5
                    });
                }
            },
            onClick: function (event, treeId, treeNode) {
                alert(treeNode.id);
            }
        }
    };
    jQuery(document).ready(function () {
        $.fn.zTree.init($("#site_tree"), siteSetting, "");
        siteZTree = $.fn.zTree.getZTreeObj("site_tree");
    });
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>
