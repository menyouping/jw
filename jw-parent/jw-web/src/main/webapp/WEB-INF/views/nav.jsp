<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
        <!-- Navigation -->
        <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
            <div class="container-fluid">
                <!-- Brand and toggle get grouped for better mobile display -->
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="${root}/">Jw Labs</a>
                </div>
                <div>
                    <ul class="nav navbar-nav">
                        <li id="menuDict" title="英汉，汉英字典">
                            <a href="${root}/">字典</a>
                        </li>
                        <li id="menuJson" title="JSON格式化工具">
                            <a href="${root}/json">JSON</a>
                        </li>
                        <li id="menuXml" title="XML格式化工具">
                            <a href="${root}/xml">XML</a>
                        </li>
                        <li id="menuSql" title="SQL格式化工具">
                            <a href="${root}/sql">SQL</a>
                        </li>
                        <li id="menuVelocity" title="Velocity编辑工具">
                            <a href="${root}/velocity">Velocity</a>
                        </li>
                        <li id="menuSet" title="集合处理工具">
                            <a href="${root}/set">集合</a>
                        </li>
                        <li id="menuTemplate" title="多行文本处理工具">
                            <a href="${root}/template">模板</a>
                        </li>
                        <li id="menuHttp" title="万众期待的HTTP工具">
                            <a href="${root}/http">HTTP</a>
                        </li>
                        <li id="menuCompare" title="文本比较工具">
                            <a href="${root}/compare">文本比较</a>
                        </li>
                         <li id="menuHtml5" title="HTML5草图大师">
                            <a href="${root}/html5">HTML5</a>
                        </li>
                        <li id="menuGitLog" title="Git Lab提交记录提取工具">
                            <a href="${root}/gitLog">Git Lab日志</a>
                        </li>
                        <li id="menuO3" title="MSS O3签名速算">
                            <a href="${root}/o3">MSS O3</a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
