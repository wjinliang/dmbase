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
                                            class="caption-subject theme-font bold uppercase">频道</span>
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
                        message: "<a href='../site/page'>请先新建频道！</a>",
                        type: "warning",
                        container: "#site_tree_div",
                        place: "prepend",
                        close: false,
                        closeInSeconds: 5
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
                grid.reload({
                    url: "./list?siteId=" + currentSiteId
                });
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
                        message: "该频道下暂无频道！",
                        type: "warning",
                        container: "#channel_tree_div",
                        place: "prepend",
                        close: false,
                        closeInSeconds: 5
                    });
                }
            },
            onClick: function (event, treeId, treeNode) {
                alert(treeNode.id);
            }
        }
    };
    var grid;
    var model;
    var options = {
        url: "./list", // ajax地址
        pageNum: 1,//当前页码
        pageSize: 5,//每页显示条数
        idFiled: "id",//id域指定
        showCheckbox: true,//是否显示checkbox
        checkboxWidth: "3%",
        showIndexNum: true,
        indexNumWidth: "5%",
        pageSelect: [2, 15, 30, 50],
        cloums: [{
            title: "名称",
            field: "displayName",
            sort: true
        }, {
            title: "英文标识",
            field: "enName",
            sort: true
        }, {
            title: "修改时间",
            field: "updateTime",
            format: function (i, data) {
                return new Date(parseInt(data.updateTime)).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
            }
        }
        ],
        actionCloumText: "操作",//操作列文本
        actionCloumWidth: "30%",
        actionCloums: [{
            text: "编辑",
            cls: "green",
            handle: function (index, data) {
                modal = $.dmModal({
                    id: "siteForm",
                    title: "表单",
                    distroy: true
                });
                modal.show();
                var form = modal.$body.dmForm(formOpts);
                form.loadRemote("./load?channelId=" + data.id);
            }
        },
            {
                text: "删除",
                cls: "red",
                handle: function (index, data) {
                    deleteItem(data.id);
                }
            }],
        tools: [
            //工具属性
            {
                text: " 添 加",//按钮文本
                cls: "btn green",//按钮样式
                icon: "fa fa-cubes",
                handle: function (grid) {//按钮点击事件
                    modal = $.dmModal({
                        id: "siteForm",
                        title: "添加",
                        distroy: true
                    });
                    modal.show();
                    var form = modal.$body.dmForm(formOpts);
                }
            }, {
                text: " 删 除",
                cls: "btn red ",//按钮样式
                handle: function (grid) {
                    deleteItems(grid.getSelectIds());
                }
            }],
        search: {
            rowEleNum: 2,
            //搜索栏元素
            items: [{
                type: "text",
                label: "名称",
                name: "name",
                placeholder: "输入要搜索的频道名称"
            }]
        }
    };
    var formOpts = {
        id: "channel_form",//表单id
        name: "channel_form",//表单名
        method: "post",//表单method
        action: "./insertOrUpdate",//表单action
        ajaxSubmit: true,//是否使用ajax提交表单
        labelInline: true,
        rowEleNum: 1,
        beforeSubmit: function () {

        },
        ajaxSuccess: function () {
            modal.hide();
            grid.reload();
        },
        submitText: "保存",//保存按钮的文本
        showReset: true,//是否显示重置按钮
        resetText: "重置",//重置按钮文本
        isValidate: true,//开启验证
        buttons: [{
            type: 'button',
            text: '关闭',
            handle: function () {
                modal.hide();
            }
        }],
        buttonsAlign: "center",
        //表单元素
        items: [{
            type: 'hidden',
            name: 'id',
            id: 'id'
        }, {
            type: 'hidden',
            name: 'pid',
            id: 'pid',
            value : 0
        }, {
            type: 'hidden',
            name: 'siteId',
            id: 'siteId',
            value : 1
        }, {
            type: 'text',//类型
            name: 'displayName',//name
            id: 'displayName',//id
            label: '频道名称',//左边label
            cls: 'input-large',
            rule: {
                required: true
            },
            message: {
                required: "频道名称"
            }
        }, {
            type: 'text',//类型
            name: 'enName',//name
            id: 'enName',//id
            label: '英文标示',//左边label
            cls: 'input-large',
            rule: {
                required: true
            },
            message: {
                required: "请输入英文标示"
            }
        },{
            type: 'select',
            name: 'templateId',
            id: 'templateId',
            label: '页面模板',
            cls: 'input-large',
            items: [{
                value: '',
                text: '请选择'
            }],
            itemsUrl: "../template/selects",
            rule: {
                required: true
            },
            message: {
                required: "请选择页面模板"
            }
        }]
    };
    jQuery(document).ready(function () {
        $.fn.zTree.init($("#site_tree"), siteSetting, "");
        siteZTree = $.fn.zTree.getZTreeObj("site_tree");
        grid = $("#channel_grid").dmGrid(options);
    });
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>
