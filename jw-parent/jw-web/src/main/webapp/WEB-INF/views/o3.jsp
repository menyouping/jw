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

<title>Jw Labs-O3</title>

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
.CodeMirror {
    height: 355px;
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
                        <button id="btnSign" class="btn btn-success">算签</button>
                        &nbsp;&nbsp;<a href="http://docs.o3cloud.cn/explain.html#" target="new">说明</a>
                    </div>
                </div>
                <div class="row" style="padding-top: 10px;">
                    <div class="col-md-12 col-lg-12"
                        style="margin-left: 15px; margin-right: 15px">
                        <label><span>私钥</span></label>
                        <input id="txtSecretKey" type="text" class="form-control" ></input>
                    </div>
                </div>
                <!-- /.row -->
                <div class="row" style="padding-top: 10px;">
                    <div class="col-md-12 col-lg-12"
                        style="margin-left: 15px; margin-right: 15px">
                        <label><span>Body</span></label>
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
    <script src="${root}/plugin/codemirror/addon/selection/active-line.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="${root}/js/bootstrap.min.js"></script>
    <script src="${root}/js/jw.js"></script>
    <script type="text/javascript" >
       var o3SecretKey = 'o3.secretKey';
       var o3Body = 'o3.body';
        $(function() {
            $("#menuO3").addClass("active");
            var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
                mode: "application/json",
                matchBrackets: true,
                autoCloseBrackets: true,
                lineNumbers: true,
                styleActiveLine: true,
                indentUnit:4
              });
            editor.setValue($jw.readStorage(o3Body) || '');
            $('#txtSecretKey').val($jw.readStorage(o3SecretKey) || '');
            editor.on('change', function(instance, changeObj) {
                hideMsg();
            });
            $('#btnSign').click(function(e) {
            	try {
                    var content = editor.getValue();
                    jsl.parser.parse(content);
                    hideMsg();
                    var param = JSON.stringify({'secretKey':$('#txtSecretKey').val(),'body':editor.getValue().trim()});  
                    $.ajax({  
                        url : "${root}/o3/sign",  
                        type : 'POST',  
                        data : param,  
                        contentType : 'application/json;charset=utf-8',  
                        success : function(data, status, xhr) {  
                            if(data && data.status == 200) {
                                editor.setValue(jsl.format.formatJson(data.body));
                                $jw.saveStorage(o3Body, editor.getValue().trim());
                                $jw.saveStorage(o3SecretKey, $('#txtSecretKey').val());
                            }
                        },  
                        error : function(xhr, error, exception) {  
                            alert(exception.toString());  
                        }  
                    });
                } catch (exp) {
                    var msg = exp.toString().replace(/\n/g, "<br>");
                    $('#divMsg').removeClass('alert-success')
                        .addClass('alert-danger')
                        .html(msg)
                        .show();
                }
            });
        });
        
        function hideMsg() {
            $('#divMsg').removeClass('alert-danger')
                .removeClass('alert-success')
                .html('')
                .hide();
        }
    </script>
    <jsp:include page="./footer.jsp"></jsp:include>
</body>

</html>
