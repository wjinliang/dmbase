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
    <title>DMCMS | 内容信息管理和发布</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta content="width=device-width, initial-scale=1" name="viewport"/>
    <meta content="" name="description"/>
    <meta content="" name="author"/>
    <link rel="stylesheet" type="text/css"
          href="<%=basePath%>assets/global/plugins/bootstrap-select/bootstrap-select.min.css"/>
    <link rel="stylesheet" type="text/css"
          href="<%=basePath%>assets/global/plugins/select2/select2.css"/>
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
                <h1>内容信息管理</h1>
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
                                            class="caption-subject theme-font bold uppercase">选择站点</span>
                                        <span class="caption-helper"></span>
                                    </div>
                                </div>
                                <div class="portlet-body form">
                                    <form action="javascript:;"
                                          class="form-horizontal form-row-sepe">
                                        <div class="form-body">
                                            <div class="form-group">
                                                <div class="col-md-12">
                                                    <div class="input-group">
                                                        <select name="select2_site"
                                                                id="select2_site"
                                                                class="form-control input-medium select2me"
                                                                data-placeholder="请选择站点...">
                                                        </select>
                                                        <span class="input-group-addon"
                                                              onclick="refreshSite()"
                                                              style="cursor:pointer">
                                                            <i class="fa fa-refresh"></i>刷新
                                                        </span>
                                                    </div>
                                                </div>

                                            </div>
                                        </div>
                                    </form>
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
                                    class="caption-subject theme-font bold uppercase">内容信息管理</span>
                                <span class="caption-helper"></span>
                            </div>
                        </div>
                        <div class="portlet-body" id="content_grid"></div>
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
<script type="text/javascript"
        src="<%=basePath%>assets/global/plugins/bootstrap-select/bootstrap-select.min.js"></script>
<script type="text/javascript"
        src="<%=basePath%>assets/global/plugins/select2/select2.min.js"></script>
<script type="text/javascript">
    var root = "<%=basePath%>"
    var grid;

    var channelTree;
    var currentSiteId;
    var currentChannelId;

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
                if (typeof (currentSiteId) == "undefined") {
                    return false;
                } else {
                    return true;
                }
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
                channelTree.expandAll(true);
            },
            onClick: function (event, treeId, treeNode) {
                currentChannelId = treeNode.id;
                grid.reload({
                    url: "./list?channelId=" + currentChannelId
                });
            }
        }
    };
    function initSelect2Site() {
        $.ajax({
            type: "POST",
            dataType: "json",
            url: "../site/selectOptions",
            success: function (data) {
                if (data.length == 0) {
                    Metronic.alert({
                        message: "<a href='../site/page'>请先新建频道！</a>",
                        type: "warning",
                        container: "#channel_tree_div",
                        place: "prepend",
                        close: true
                    });
                }
                $.each(data, function (i, o) {
                    var option = $("<option></option>");
                    option.text(o.text);
                    option.attr("value", o.value);
                    $("#select2_site").append(option);
                });
                if (data.length > 0) {
                    refreshSite();
                }
                $("#select2_site").change(function () {
                    refreshSite();
                });
                $("#select2_site").select2({
                    allowClear: true
                });
            }
        });
    }
    function refreshSite() {
        if (typeof(channelTree) == "undefined") {
            $.fn.zTree.init($("#channel_tree"), channelSetting, "");
            channelTree = $.fn.zTree.getZTreeObj("channel_tree");
        }
        currentSiteId = $("#select2_site").val();
        channelTree.setting.async.url = "../channel/tree?siteId=" + currentSiteId;
        channelTree.reAsyncChildNodes(null, "refresh");
    }
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
            title: "标题",
            field: "title",
            sort: true
        }],
        actionCloumText: "操作",//操作列文本
        actionCloumWidth: "30%",
        actionCloums: [{
            text: "预览",
            cls: "green",
            icon: "fa fa-search",
            handle: function (index, data) {
                window.open(root + "portal/" + data.siteDomain + "/" + data.channelEnName + "/" + data.id);
            }
        }, {
            text: "编辑",
            cls: "green",
            handle: function (index, data) {
                //index为点击操作的行数
                //data为该行的数据
                modal = $.dmModal({
                    id: "siteForm",
                    title: "编辑内容信息-" + data.title,
                    distroy: true
                });
                modal.show();
                var form = modal.$body.dmForm(getForm());
                form.loadRemote("./load?contentId=" + data.id);
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
                        title: "添加内容信息",
                        distroy: true
                    });
                    modal.show();
                    var form = modal.$body.dmForm(getForm());
                    form.setValue("channelId", currentChannelId);
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
                label: "标题",
                name: "title",
                placeholder: "输入要搜索的内容信息标题"
            }]
        }
    };
    var modal;
    //form
    function getForm() {
        var formOpts = {
            id: "site_form",//表单id
            name: "site_form",//表单名
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
                type: "tree",
                name: "channelId",
                id: "channelId",
                label: "所属频道",
                url: "../channel/tree?siteId=" + currentSiteId,
                autoParam: ["id", "name", "pId"],
                expandAll: true,
                chkStyle: "radio"
            }, {
                type: 'text',//类型
                name: 'title',//name
                id: 'title',//id
                label: '内容信息标题',//左边label
                cls: 'input-large',
                rule: {
                    required: true,
                    maxlength: 64
                },
                message: {
                    required: "请输入内容信息标题",
                    maxlength: "最多输入64字节"
                }
            }, {
                type: 'textarea',//类型
                row: 3,
                name: 'brief',//name
                id: 'brief',//id
                label: '内容信息摘要',//左边label
                cls: 'input-large',
                rule: {
                    required: true,
                    maxlength: 256
                },
                message: {
                    required: "请输入内容信息摘要",
                    maxlength: "最多输入256字节"
                }
            }, {
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
            }, {
                type: 'kindEditor',
                name: 'contentText',
                id: 'contentText',
                label: '内容文本',
                height: "300px",
                width: "500px",
                rule: {
                    required: true
                },
                message: {
                    required: "请输入内容文本"
                }
            }]
        };
        return formOpts;
    }


    function deleteItem(id) {
        bootbox.confirm("确定删除吗？", function (result) {
            if (result) {
                $.ajax({
                    type: "POST",
                    data: "contentId=" + id,
                    dataType: "json",
                    url: "./delete",
                    success: function (data) {
                        if (data.status == 1) {
                            grid.reload();
                        }
                    }
                });
            }
        });
    }
    function deleteItems(ids) {
        if (ids.length > 0) {
            bootbox.confirm("确定删除吗？", function (result) {
                if (result) {
                    $.ajax({
                        type: "POST",
                        data: "contentId=" + ids,
                        dataType: "json",
                        url: "./delete",
                        success: function (data) {
                            if (data.status == 1) {
                                grid.reload();
                            }
                        }
                    });
                }
            });
        } else {
            bootbox.alert("请选择要删除的选项！");
        }
    }

    jQuery(document).ready(function () {
        initSelect2Site();
        grid = $("#content_grid").dmGrid(options);
    });
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>
