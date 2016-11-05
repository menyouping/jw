<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-CN">

<head>

<jsp:include page="./header.jsp"></jsp:include>

<title>Jw Labs-JSON</title>

<!-- Bootstrap Core CSS -->
<link href="${root}/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="${root}/css/sb-admin.min.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="${root}/font-jw/style.css" rel="stylesheet" type="text/css">
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
    <script src="${root}/js/jsl.format.min.js"></script>
    <script src="${root}/js/jsl.parser.min.js"></script>
    <script src="${root}/plugin/codemirror/lib/codemirror.min.js"></script>
    <script src="${root}/plugin/codemirror/addon/edit/closebrackets.min.js"></script>
    <script src="${root}/plugin/codemirror/addon/edit/matchbrackets.min.js"></script>
    <script src="${root}/plugin/codemirror/mode/javascript/javascript.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="${root}/js/bootstrap.min.js"></script>
    <script src="${root}/js/jw.js"></script>
    <script type="text/javascript">
        var storageKey = 'json';
        $(function() {
            $("#menuJson").addClass("active");
            var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
                mode: "application/json",
                matchBrackets: true,
                autoCloseBrackets: true,
                lineNumbers: true,
                indentUnit:4
              });
            editor.setValue($jw.readStorage(storageKey) || '');
            editor.on('change', function(instance, changeObj) {
                hideMsg();
            });
            $('#btnGo').click(
                function(e) {
                    try {
                        var content = editor.getValue();
                        jsl.parser.parse(content);
                        content = jsl.format.formatJson(content);
                        editor.setValue(content);
                        $('#divMsg').removeClass('alert-danger')
                            .addClass('alert-success')
                            .html('JSON is valid.')
                            .show();
                        $jw.saveStorage(storageKey, content);
                    } catch (exp) {
                        var msg = exp.toString().replace(/\n/g, "<br>");
                        $('#divMsg').removeClass('alert-success')
                            .addClass('alert-danger')
                            .html(msg)
                            .show();
                    }
                });
            $('#btnRaw').click(function(e) {
                try {
                    var content = editor.getValue();
                    jsl.parser.parse(content);
                    content = unformatJson(content);
                    editor.setValue(content);
                    $jw.saveStorage(storageKey, content);
                    hideMsg();
                } catch (exp) {
                    var msg = exp.toString().replace(/\n/g, "<br>");
                    $('#divMsg').removeClass('alert-success')
                        .addClass('alert-danger')
                        .html(msg)
                        .show();
                }
            });
        });

        function unformatJson(json) {
            if (typeof json === 'string') {
                json = JSON.parse(json);
            }
            json = JSON.stringify(json);
            return json.trim();
        }

        function hideMsg() {
            $('#divMsg').removeClass('alert-danger').removeClass('alert-success').html(
                    '').hide();
        }
    </script>
    <jsp:include page="./footer.jsp"></jsp:include>
</body>

</html>
