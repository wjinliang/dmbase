<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8" />
<title>DMCMS | 后台主页</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta content="width=device-width, initial-scale=1" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<%@include file="../includejsps/style.jsp"%>
<%@include file="../includejsps/plugin-style.jsp"%>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<!-- DOC: Apply "page-header-menu-fixed" class to set the mega menu fixed  -->
<!-- DOC: Apply "page-header-top-fixed" class to set the top menu fixed  -->
<body>
	<!-- BEGIN HEADER -->
	<%@include file="../includejsps/head.jsp"%>
	<!-- END HEADER -->
	<!-- BEGIN PAGE CONTAINER -->
	<div class="page-container">
		<!-- BEGIN PAGE HEAD -->
		<div class="page-head">
			<div class="container-fluid">
				<!-- BEGIN PAGE TITLE -->
				<div class="page-title">
					<h1>
						主页面板 <small>统计 & 报告</small><a style="cursor:default;"></a>
					</h1>
				</div>
				<!-- END PAGE TITLE -->
				<!-- BEGIN PAGE TOOLBAR -->
				<%@include file="../includejsps/toolbar.jsp"%>
				<!-- END PAGE TOOLBAR -->
			</div>
		</div>
		<!-- END PAGE HEAD -->
		<!-- BEGIN PAGE CONTENT -->
		<div class="page-content">
			<div class="container-fluid">
				<!-- BEGIN PAGE CONTENT INNER -->
				<div class="row margin-top-10">
					<div class="col-md-12 col-sm-12">
						<!-- BEGIN PORTLET-->
						<div class="portlet light">
							<div class="portlet-title">
								<div class="caption caption-md">
									<i class="icon-bar-chart theme-font hide"></i> <span
										class="caption-subject theme-font bold uppercase">grid插件示例</span>
									<span class="caption-helper"></span>
								</div>
							</div>
							<div class="portlet-body" id="example_grid"></div>
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
	<%@include file="../includejsps/foot.jsp"%>
	<!-- END FOOTER-->
	<!-- BEGIN JAVASCRIPTS-->
	<%@include file="../includejsps/js.jsp"%>
	<%@include file="../includejsps/plugin-js.jsp"%>
	<script type="text/javascript">
		var grid;
		var options = {
			url : "example/ajaxList", // ajax地址
			pageNum : 1,//当前页码
			pageSize : 5,//每页显示条数
			idFiled : "id",//id域指定
			showCheck : true,//是否显示checkbox
			checkType : "radio",
			pageSelect : [ 5, 15, 30, 50 ],
			cloums : [ {
				title : "普通文本",
				field : "name",
				width : "15%",
				sort : true
			}, {
				title : "下拉框",
				field : "selector",
				format : function(index, data) {
					if (data.selector == "1") {
						return "一";
					} else if (data.selector == "2") {
						return "二";
					} else {
						return data.selector;
					}
				},
				sort : true
			} ],
			actionCloumText : "操作",//操作列文本
			actionCloumWidth : "30%",
			actionCloums : [ {
				text : "查看",
				cls : "green",
				handle : function(index, data) {
					popViewItem(data.id);
				}
			}, {
				//text : "编辑",
				textHandle : function(index, data) {
					//index为点击操作的行数
					//data为该行的数据
					//特殊需要时，textHandle能更具需要改变操作项的文本显示
					return "编辑";
				},
				cls : "green",
				handle : function(index, data) {
					//index为点击操作的行数
					//data为该行的数据
					modal = $.dmModal({
						id : "cgj",
						title : "表单",
						distroy : true
					});
					modal.show();
					var form = modal.$body.dmForm(formOpts);
					form.loadLocal(data);
				}
			}, {
				text : "删除",
				cls : "red",
				handle : function(index, data) {
					deleteItem(data.id);
				}
			} ],
			tools : [
			//工具属性
			{
				text : " 添 加",//按钮文本
				cls : "btn green",//按钮样式
				icon : "fa fa-cubes",
				handle : function(grid) {//按钮点击事件
					popWin();
				}
			}, {
				text : " 删 除",
				cls : "btn red ",//按钮样式
				handle : function(grid) {
					deleteItems(grid.getSelectIds());
				}
			} ],
			dropdowns : [
			//工具属性
			{
				text : " 弹窗",//按钮文本
				icon : "fa fa-cubes",
				handle : function(grid) {//按钮点击事件
					var modal = $.dmModal({
						title : "表格"
					});
					modal.$body.dmGrid(grid._options);
					modal.show();
				}
			}, {
				text : " 删 除",
				handle : function() {
					alert("删除");
				}
			} ],
			search : {
				rowEleNum : 4,
				//搜索栏元素
				items : [ {
					type : "select",//元素类型
					label : "下拉框",//元素说明
					name : "selector",//元素name
					items : [ {
						text : "请选择",
						value : ""
					}, {
						text : "一",
						value : "1"
					}, {
						text : "二",
						value : "2"
					} ],
					itemsUrl : "example/exampleItems"
				}, {
					type : "text",
					label : "普通文本",
					name : "name",
					placeholder : "普通文本"
				}, {
					type : 'radioGroup',
					name : 'radio',
					label : '单选框',
					items : [ {
						value : '1',
						text : '单选框1'
					}, {
						value : '2',
						text : '单选框2'
					} ],
					itemsUrl : "example/exampleItems"
				}, {
					type : 'checkboxGroup',
					name : 'checkbox',
					detail : '这是复选框',
					label : '复选框',
					items : [ {
						checked : true,
						value : '1',
						text : '复选框1'
					}, {
						value : '2',
						text : '复选框2'
					//初始默认选中
					} ]
				} ],
				buttons : [ {
					text : "清 空",//按钮文本
					handle : function() {//按钮点击事件
						alert("添加");
					}
				} ]
			}
		};
		var modal;
		//form
		var formOpts = {
			id : "example_form",//表单id
			name : "example_form",//表单名
			method : "post",//表单method
			action : "example/ajaxSave",//表单action
			ajaxSubmit : true,//是否使用ajax提交表单
			labelInline : true,
			rowEleNum : 1,
			beforeSubmit : function() {

			},
			ajaxSuccess : function() {
				modal.hide();
				grid.reload();
			},
			submitText : "保存",//保存按钮的文本
			showReset : true,//是否显示重置按钮
			resetText : "重置",//重置按钮文本
			isValidate : true,//开启验证
			buttons : [ {
				type : 'button',
				text : '关闭',
				handle : function() {
					modal.hide();
				}
			} ],
			buttonsAlign : "center",
			//表单元素
			items : [ {
				type : 'section',
				title : '文本表单'
			}, {
				type : 'hidden',
				name : 'id',
				id : 'id'
			}, {
				type : 'text',//类型
				name : 'name',//name
				id : 'name',//id
				label : '普通文本框',//左边label
				detail : '这是普通文本',//描述
				span : '6',//宽度 1-8
				value : '默认值',//默认显示值
				cls : 'input-medium',
				icon : 'fa fa-info-circle tooltips',
				rule : {//验证规则
					required : true,
					remote : {
						type : "post",
						url : "example/checkName",
						data : {
							name : function() {
								return $("#name").val();
							},
							id : function(){
								return $("#id").val();
							}
						},
						dataType : "json",
						dataFilter : function(data, type) {
							return data;
						}
					}
				},
				message : {//对应验证提示信息
					required : "请输入文本",
					remote : "名字被占用"
				}
			}, {
				type : 'password',
				name : 'psw',
				id : 'psw',
				cls : ' input-circle',
				detail : '这是密码',
				label : '密码'
			}, {
				type : 'textarea',
				name : 'textarea',
				id : 'textarea',
				label : 'textarea',
				rule : {
					required : true
				},
				message : {
					required : "请输入"
				}
			}, {
				type : 'tag',
				name : 'tag',
				id : 'tag',
				label : 'tag',
				color : 'blue',
				rule : {
					required : true
				},
				message : {
					required : "请输入"
				}
			}, {
				type : 'colorpicker',
				name : 'color',
				id : 'color',
				label : 'color',
				span : '5',
				rule : {
					required : true
				},
				message : {
					required : "请输入"
				}
			}, {
				type : 'file',
				id : 'file1',
				name : 'file1',
				label : '文件',
				detail : "这是文件控件",
				isAjaxUpload : true,
				uploadUrl : 'file/uploadImage',//文件单独上传路径
				onSuccess : function(data) {
					if (data.status == "1") {
						$("#file1").attr("value",data.fileUrl);
					} else {
						alert(data.message);
					}

				},
				deleteHandle : function() {
					$("#file_file").attr("value","");
				},
				rule : {
					required : true
				},
				message : {
					required : "请上传文件"
				}
			}, {
				type : 'image',
				id : 'image',
				name : 'image',
				label : '图片',
				detail : "这是图片控件",
				isAjaxUpload : true,
				autoUpload : true,
				uploadUrl : 'file/uploadImage',//文件单独上传路径
				onSuccess : function(data) {
					if (data.status == "1") {
						$("#image").attr("value",data.fileUrl);
					} else {
						alert(data.msg);
					}
				},
				deleteHandle : function() {
					$("#image").attr("value","");
				},
				rule : {
					required : true
				},
				message : {
					required : "请上传图片"
				}
			}, {
				type : 'display',
				name : 'display',
				id : 'display',
				label : '展示内容',
				value : '这是展示的内容'
			}, {
				type : 'checkboxGroup',
				name : 'checkbox',
				id : 'checkbox',
				inline : true,
				detail : '这是复选框',
				label : '复选框',
				items : [ {
					value : '1',
					text : '复选框1'
				}, {
					value : '2',
					text : '复选框2'
				//初始默认选中
				} ],
				itemsUrl : "example/exampleItems",
				rule : {
					required : true
				},
				message : {
					required : "请选择一项"
				}
			}, {
				type : 'radioGroup',
				name : 'radio',
				id : 'radioGroup',
				label : '单选框',
				items : [ {
					value : '1',
					text : '单选框1'
				}, {
					value : '2',
					text : '单选框2'
				} ],
				itemsUrl : "example/exampleItems",
				rule : {
					required : true
				},
				message : {
					required : "请选择"
				}
			}, {
				type : 'select',
				name : 'selector',
				id : 'selector',
				label : '下拉框',
				span : '6',
				readonly : true,
				items : [ {
					value : '',
					text : '请选择'
				}, {
					value : '1',
					text : '下拉框1',
				}, {
					value : '2',
					text : '下拉框2'
				} ],
				itemsUrl : "example/exampleItems",
				rule : {
					required : true
				},
				message : {
					required : "请选择"
				}
			}, {
				type : 'datepicker',
				name : 'datepicker',
				id : 'datepicker',
				label : '时间选择器',
				span : '4',
				isTime : true,//选择时间模式
				format : 'YYYY-MM-DD hh:mm:ss',//日期格式
				rule : {
					required : true
				},
				message : {
					required : "请选择日期"
				}
			}, {
				type : "tree",
				name : "tree",
				id : "tree",
				label : "树",
				detail : "这是树控件",
				url : "./usermenu/load",
				autoParam : [ "id", "name","pId" ],
				chkStyle : "checkbox",
				rule : {
					required : true
				},
				message : {
					required : "请选择一项"
				}
			}, {
				type : 'kindEditor',
				name : 'editor',
				id : 'editor',
				label : 'kindEditor',
				height : "300px",
				width : "500px",
				rule : {
					required : true
				},
				message : {
					required : "请输入"
				}
			} ]
		};

		function popWin() {
			modal = $.dmModal({
				id : "cgj",
				title : "表单",
				distroy : true
			});
			modal.show();
			modal.$body.dmForm(formOpts);
		}

		function deleteItem(id) {
			bootbox.confirm("确定删除吗？", function(result) {
				if (result) {
					$.ajax({
						type : "POST",
						data : "exampleid=" + id,
						dataType : "json",
						url : "example/ajaxDelete",
						success : function(data) {
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
				bootbox.confirm("确定删除吗？", function(result) {
					if (result) {
						$.ajax({
							type : "POST",
							data : "exampleid=" + ids,
							dataType : "json",
							url : "example/ajaxDelete",
							success : function(data) {
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

		jQuery(document).ready(function() {
			grid = $("#example_grid").dmGrid(options);
		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>