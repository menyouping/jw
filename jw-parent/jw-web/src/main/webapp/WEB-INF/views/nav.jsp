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
                        <li id="menuDict">
                            <a href="${root}/">字典</a>
                        </li>
                        <li id="menuJson">
                            <a href="${root}/json">JSON</a>
                        </li>
                        <li id="menuXml">
                            <a href="${root}/xml">XML</a>
                        </li>
                        <li id="menuSql">
                            <a href="${root}/sql">SQL</a>
                        </li>
                        <li id="menuSet">
                            <a href="${root}/set">集合</a>
                        </li>
                        <li id="menuSet">
                            <a href="${root}/compare">文本比较</a>
                        </li>
                        <li id="menuO3">
                            <a href="${root}/o3">O3</a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
