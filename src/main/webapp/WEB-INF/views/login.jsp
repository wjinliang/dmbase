<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <meta charset="utf-8"/>
    <title>DMCMS | 登录</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta content="" name="description"/>
    <meta content="" name="author"/>
    <!-- BEGIN GLOBAL MANDATORY STYLES -->
    <link
            href="<%=basePath%>assets/global/plugins/font-awesome/css/font-awesome.min.css"
            rel="stylesheet" type="text/css"/>
    <link
            href="<%=basePath%>assets/global/plugins/simple-line-icons/simple-line-icons.min.css"
            rel="stylesheet" type="text/css"/>
    <link
            href="<%=basePath%>assets/global/plugins/bootstrap/css/bootstrap.min.css"
            rel="stylesheet" type="text/css"/>
    <link
            href="<%=basePath%>assets/global/plugins/uniform/css/uniform.default.css"
            rel="stylesheet" type="text/css"/>
    <!-- END GLOBAL MANDATORY STYLES -->
    <!-- BEGIN PAGE LEVEL STYLES -->
    <link href="<%=basePath%>assets/admin/pages/css/login2.css"
          rel="stylesheet" type="text/css"/>
    <!-- END PAGE LEVEL SCRIPTS -->
    <!-- BEGIN THEME STYLES -->
    <link href="<%=basePath%>assets/global/css/components-rounded.css"
          id="style_components" rel="stylesheet" type="text/css"/>
    <link href="<%=basePath%>assets/global/css/plugins.css" rel="stylesheet"
          type="text/css"/>
    <link href="<%=basePath%>assets/admin/layout/css/layout.css"
          rel="stylesheet" type="text/css"/>
    <link href="<%=basePath%>assets/admin/layout/css/themes/default.css"
          rel="stylesheet" type="text/css" id="style_color"/>
    <link href="<%=basePath%>assets/admin/layout/css/custom.css"
          rel="stylesheet" type="text/css"/>
    <!-- END THEME STYLES -->
    <link rel="shortcut icon" type="image/x-icon" href="<%=basePath%>favicon.ico" media="screen"/>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="login">
<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
<div class="menu-toggler sidebar-toggler"></div>
<!-- END SIDEBAR TOGGLER BUTTON -->
<!-- BEGIN LOGO -->
<div class="logo">
    <a href="#"> <img
            src="<%=basePath%>assets/admin/layout3/img/logo-big-white.png"
            style="height: 17px;" alt=""/> </a>
</div>
<!-- END LOGO -->
<!-- BEGIN LOGIN -->
<div class="content">
    <!-- BEGIN LOGIN FORM -->
    <form class="login-form" action="<%=basePath%>j_spring_security_check"
          method="post">
        <div class="form-title">
            <span class="form-title">欢迎.</span> <span class="form-subtitle">请登录.</span>
        </div>
        <div
                class="alert alert-danger
		<c:if test="${param.error!=true}">display-hide</c:if>
		<c:if test="${param.error==true}">display-show</c:if> ">
            <button class="close" data-close="alert"></button>
            <span>${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message}</span>
        </div>
        <div class="form-group">
            <!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
            <label class="control-label visible-ie8 visible-ie9">用户名</label> <input
                class="form-control form-control-solid placeholder-no-fix"
                type="text" autocomplete="off" placeholder="请输入用户名"
                name="j_username"/>
        </div>
        <div class="form-group">
            <label class="control-label visible-ie8 visible-ie9">验证码</label> <input
                class="form-control form-control-solid placeholder-no-fix"
                type="password" autocomplete="off" placeholder="请输入密码"
                name="j_password"/>
        </div>
        <c:if test="${param.error==true}">
            <div class="form-group">
                <a id="flashImage" title="看不清,换一张" href="javascript:void(0)"> <img
                        id="imageF" src="<%=basePath%>randomImage"
                        style="width:100%;height:34px;border: 1px solid #e5e5e5;"
                        class="m-wrap form-control-solid placeholder-no-fix"/>
                </a>
            </div>
            <div class="form-group">
                <label class="control-label visible-ie8 visible-ie9">验证码</label>
                <input
                        class="form-control form-control-solid placeholder-no-fix"
                        type="text" autocomplete="off" placeholder="请输入验证码"
                        name="j_captcha"/>
            </div>
        </c:if>
        <div class="form-actions">
            <button type="submit" class="btn btn-primary btn-block uppercase">登录</button>
        </div>
        <div class="form-actions">
            <div class="pull-left">
                <label class="rememberme check"> <input type="checkbox"
                                                        name="_spring_security_remember_me" value="true"/>免登录</label>
            </div>
            <div class="pull-right forget-password-block">
                <a href="javascript:;" id="forget-password" class="forget-password">忘记密码?</a>
            </div>
        </div>
        <div class="login-options">
            <h4 class="pull-left">或者使用第三方授权登录</h4>
            <ul class="social-icons pull-right">
                <li><a class="social-icon-color facebook"
                       data-original-title="facebook" href="#"></a></li>
                <li><a class="social-icon-color twitter"
                       data-original-title="Twitter" href="#"></a></li>
                <li><a class="social-icon-color googleplus"
                       data-original-title="Goole Plus" href="#"></a></li>
                <li><a class="social-icon-color linkedin"
                       data-original-title="Linkedin" href="#"></a></li>
            </ul>
        </div>
        <div class="create-account">
            <p>
                <a href="javascript:;" id="register-btn">注册账号</a>
            </p>
        </div>
    </form>
    <!-- END LOGIN FORM -->
    <!-- BEGIN FORGOT PASSWORD FORM -->
    <form class="forget-form" action="<%=basePath%>/resetPassword" method="post">
        <div class="form-title">
            <span class="form-title">忘记密码 ?</span> <span
                class="form-subtitle">输入你的电子邮箱重置密码.</span>
        </div>
        <div class="form-group">
            <input class="form-control placeholder-no-fix" type="text"
                   autocomplete="off" placeholder="Email" name="email"/>
        </div>
        <div class="form-actions">
            <button type="button" id="back-btn" class="btn btn-default">返回</button>
            <button type="submit" class="btn btn-primary uppercase pull-right">提交</button>
        </div>
    </form>
    <!-- END FORGOT PASSWORD FORM -->
    <!-- BEGIN REGISTRATION FORM -->
    <form class="register-form" action="<%=basePath%>saveRegiest" method="post">
        <div class="form-title">
            <span class="form-title">注 册</span>
        </div>
        <p class="hint">请输入个人详细信息:</p>

        <div class="form-group">
            <label class="control-label visible-ie8 visible-ie9">姓名全称</label>
            <input class="form-control placeholder-no-fix" type="text"
                   placeholder="姓名全称" name="fullname"/>
        </div>
        <div class="form-group">
            <label class="control-label visible-ie8 visible-ie9">电子邮箱</label> <input
                class="form-control placeholder-no-fix" type="text"
                placeholder="电子邮箱" name="email"/>
        </div>
        <div class="form-group">
            <label class="control-label visible-ie8 visible-ie9">手机</label> <input
                class="form-control placeholder-no-fix" type="text"
                placeholder="手机" name="mobile"/>
        </div>

        <p class="hint">请填写账号信息:</p>

        <div class="form-group">
            <label class="control-label visible-ie8 visible-ie9">用户名</label>
            <input class="form-control placeholder-no-fix" type="text"
                   autocomplete="off" placeholder="用户名" id="username" name="username"/>
        </div>
        <div class="form-group">
            <label class="control-label visible-ie8 visible-ie9">密码</label>
            <input class="form-control placeholder-no-fix" type="password"
                   autocomplete="off" id="register_password" placeholder="密码"
                   name="password"/>
        </div>
        <div class="form-group">
            <label class="control-label visible-ie8 visible-ie9">再次输入密码
                Your Password</label> <input class="form-control placeholder-no-fix"
                                             type="password" autocomplete="off"
                                             placeholder="再次输入密码" name="rpassword"/>
        </div>
        <div class="form-group margin-top-20 margin-bottom-20">
            <label class="check"> <input type="checkbox" name="tnc"/> <span
                    class="loginblue-font">我同意 </span> <a href="#"
                                                          class="loginblue-link">服务协议</a> <span
                    class="loginblue-font">和</span> <a href="#"
                                                       class="loginblue-link">隐私政策 </a> </label>

            <div id="register_tnc_error"></div>
        </div>
        <div class="form-actions">
            <button type="button" id="register-back-btn" class="btn btn-default">返回</button>
            <button type="submit" id="register-submit-btn"
                    class="btn btn-primary uppercase pull-right">完成注册
            </button>
        </div>
    </form>
    <!-- END REGISTRATION FORM -->
</div>
<div class="copyright hide">2015 © DMCMS. orange.
</div>
<!-- END LOGIN -->
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->
<!--[if lt IE 9]>
<script src="<%=basePath%>assets/global/plugins/respond.min.js"></script>
<script src="<%=basePath%>assets/global/plugins/excanvas.min.js"></script>
<![endif]-->
<script>
    var dm_root = "<%=basePath%>";
</script>
<script src="<%=basePath%>assets/global/plugins/jquery.min.js"
        type="text/javascript"></script>
<script src="<%=basePath%>assets/global/plugins/jquery-migrate.min.js"
        type="text/javascript"></script>
<script
        src="<%=basePath%>assets/global/plugins/bootstrap/js/bootstrap.min.js"
        type="text/javascript"></script>
<script src="<%=basePath%>assets/global/plugins/jquery.blockui.min.js"
        type="text/javascript"></script>
<script
        src="<%=basePath%>assets/global/plugins/uniform/jquery.uniform.min.js"
        type="text/javascript"></script>
<script src="<%=basePath%>assets/global/plugins/jquery.cokie.min.js"
        type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script
        src="<%=basePath%>assets/global/plugins/jquery-validation/js/jquery.validate.min.js"
        type="text/javascript"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="<%=basePath%>assets/global/scripts/metronic.js"
        type="text/javascript"></script>
<script src="<%=basePath%>assets/admin/pages/scripts/login.js"
        type="text/javascript"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<script>
    jQuery(document).ready(function () {
        Metronic.init(); // init metronic core components
        Login.init();
    });
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>
