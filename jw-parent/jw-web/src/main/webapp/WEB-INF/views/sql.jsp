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

<title>Jw Labs-SQL</title>

<!-- Bootstrap Core CSS -->
<link href="${root}/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="${root}/css/sb-admin.min.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="${root}/font-jw/style.css" rel="stylesheet" type="text/css">
<%-- <link href="${root}/css/jw.editor.css" rel="stylesheet" type="text/css"> --%>
<link rel="stylesheet" href="${root}/plugin/codemirror/lib/codemirror.min.css">
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
                    <div class="col-md-12 col-lg-12"
                        style="margin-left: 15px; margin-right: 15px">
                        <button id="btnGo" class="btn btn-success">美化</button>
                        <button id="btnRaw" class="btn btn-default">一行</button>
                    </div>
                </div>
                <!-- /.row -->
                <div class="row" style="padding-top: 10px;">
                    <div class="col-md-12 col-lg-12"
                        style="margin-left: 15px; margin-right: 15px">
                        <textarea id="code" name="code" style="display: none;"></textarea>
                    </div>
                </div>

                <div class="row" style="padding-top: 10px;">
                    <div class="col-md-12 col-lg-12"
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
    <script src="${root}/js/jquery.min.js"></script>
    <script src="${root}/js/jquery.format.min.js"></script>
    <script src="${root}/plugin/codemirror/lib/codemirror.min.js"></script>
    <script src="${root}/plugin/codemirror/mode/sql/sql.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="${root}/js/bootstrap.min.js"></script>
    <script src="${root}/js/jw.js"></script>
    <script type="text/javascript" >
       var storageKey = 'sql';
        $(function() {
            $("#menuSql").addClass("active");
            var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
                mode: "text/x-sql",
                lineNumbers: true,
                indentUnit:4
              });
            editor.setValue($jw.readStorage(storageKey) || '');
            editor.on('change', function(instance, changeObj) {
                hideMsg();
            });
            $('#btnGo').click(function(e) {
                var content = editor.getValue().trim();
                content = $.format(content,{method:'sql'});
                editor.setValue(content);
                $jw.saveStorage(storageKey, content);
            });
            $('#btnRaw').click(function(e) {
                var content = editor.getValue().trim();
                content = content.replace(/(\s*)\n(\s*)/g,' ');
                editor.setValue(content);
                $jw.saveStorage(storageKey, content);
            });
        });
        
        function hideMsg() {
            $('#divMsg').removeClass('alert-danger').removeClass('alert-success').html(
                    '').hide();
        }
    </script>
    <jsp:include page="./footer.jsp"></jsp:include>
</body>

</html>
