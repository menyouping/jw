<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-CN">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="Jay Zhang">

<title>Jw英语</title>

<!-- Bootstrap Core CSS -->
<link href="${root}/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="${root}/css/sb-admin.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="${root}/font-jw/style.css" rel="stylesheet" type="text/css">
<link href="${root}/css/jw.editor.css" rel="stylesheet" type="text/css">
<style type="text/css">
#main {
    color: #666
}
</style>

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>

    <jsp:include page="./nav.jsp"></jsp:include>
    <div id="wrapper">
        <div id="page-wrapper">

            <div class="container-fluid">
                <!-- /.row -->
                <div class="row" style="padding-top: 10px;">
                    <div class="col-md-offset-2 col-lg-offset-2 col-md-10 col-lg-10"
                        style="margin-left: 15px; margin-right: 15px">
                        <button id="btnGo" class="btn btn-success">美化</button>
                        <button id="btnRaw" class="btn btn-default">一行</button>
                    </div>
                </div>
                <!-- /.row -->
                <div class="row" style="padding-top: 10px;">
                    <div class="col-md-offset-2 col-lg-offset-2 col-md-10 col-lg-10"
                        style="margin-left: 15px; margin-right: 15px">
                        <div class="line">
                            <textarea rows="10" id="txtLine" disabled></textarea>
                        </div>
                        <textarea rows="20" id="txtContent" class="form-control"></textarea>
                    </div>
                </div>

                <div class="row" style="padding-top: 10px;">
                    <div class="col-md-offset-2 col-lg-offset-2 col-md-10 col-lg-10"
                        style="margin-left: 15px; margin-right: 15px">
                        <div id="divMsg" class="alert alert-success"
                            style="display: none;"></div>
                    </div>
                </div>
            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- /#page-wrapper -->

    </div>

    <!-- /#wrapper -->

    <!-- jQuery -->
    <script src="${root}/js/jquery.js"></script>
    <script src="${root}/js/jsl.format.js"></script>
    <script src="${root}/js/jsl.parser.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="${root}/js/bootstrap.min.js"></script>
    <script src="${root}/js/jw.js"></script>
    <script src="${root}/js/jw.editor.js"></script>
    <script type="text/javascript">
        $(function() {
            $("#liJson").addClass("active");
            $('#btnGo').click(
                function(e) {
                    try {
                        var content = $('#txtContent').val();
                        jsl.parser.parse(content);
                        content = jsl.format.formatJson(content);
                        $('#txtContent').val(content);
                        keyUp();
                        $('#divMsg').removeClass('alert-danger').addClass(
                                'alert-success').html('JSON is valid.')
                                .show();
                    } catch (exp) {
                        $('#divMsg').removeClass('alert-success').addClass(
                                'alert-danger').html(exp).show();
                    }
                });
            $('#btnRaw').click(function(e) {
                var json = unformatJson($('#txtContent').val());
                $('#txtContent').val(json);
                hideMsg();
                keyUp();
            });
        });

        
    </script>
</body>

</html>
