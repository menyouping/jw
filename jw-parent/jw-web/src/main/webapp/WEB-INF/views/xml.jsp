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

<title>Jw Labs-XML</title>

<!-- Bootstrap Core CSS -->
<link href="${root}/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="${root}/css/sb-admin.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="${root}/font-jw/style.css" rel="stylesheet" type="text/css">
<%-- <link href="${root}/css/jw.editor.css" rel="stylesheet" type="text/css"> --%>
<link rel="stylesheet" href="${root}/plugin/codemirror/lib/codemirror.css">
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
                        <textarea id="code" name="code" style="display: none;"></textarea>
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
    <%-- <script src="${root}/js/jw.editor.js"></script> --%>
    <script src="${root}/js/jquery.format.js"></script>
    <script src="${root}/plugin/codemirror/lib/codemirror.js"></script>
    <script src="${root}/plugin/codemirror/addon/fold/xml-fold.js"></script>
    <script src="${root}/plugin/codemirror/addon/edit/closetag.js"></script>
    <script src="${root}/plugin/codemirror/addon/edit/matchtags.js"></script>
    <script src="${root}/plugin/codemirror/mode/xml/xml.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="${root}/js/bootstrap.min.js"></script>
    <script src="${root}/js/jw.js"></script>
    <script type="text/javascript" >
       var storageKey = 'xml';
        $(function() {
            $("#menuXml").addClass("active");
            var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
                mode: "application/xml",
                matchTags: true,
                autoCloseTags:true,
                lineNumbers: true,
                indentUnit:4
              });
            editor.setValue($jw.readStorage(storageKey) || '');
            editor.on('change', function(instance, changeObj) {
                hideMsg();
            });
            $('#btnGo').click(function(e) {
                var content = editor.getValue().trim();
                var rs = validateXML(content);
                if(rs.error_code == 1) {
                    $('#divMsg').removeClass('alert-success')
                        .addClass('alert-danger')
                        .html(rs.msg)
                        .show();
                } else {
                    content = $.format(content,{method:'xml'});
                    editor.setValue(content);
                    $('#divMsg').removeClass('alert-danger')
                    .addClass('alert-success')
                    .html('XML is valid.')
                    .show();
                    $jw.saveStorage(storageKey, content);
                }
            });
            $('#btnRaw').click(function(e) {
                var content = editor.getValue().trim();
                var rs = validateXML(content);
                if(rs.error_code == 1) {
                    $('#divMsg').removeClass('alert-success')
                        .addClass('alert-danger')
                        .html(rs.msg)
                        .show();
                } else {
                    content = content.replace(/(\s*)\n(\s*)/g,'');
                    editor.setValue(content);
                    $('#divMsg').removeClass('alert-danger')
                        .addClass('alert-success')
                        .html('XML is valid.')
                    .show();
                    $jw.saveStorage(storageKey, content);
                }
            });
        });

        function validateXML(xmlContent) { 
            //errorCode 0是xml正确，1是xml错误，2是无法验证 
            var xmlDoc,msg,errorCode = 0; 
            // code for IE 
            if (window.ActiveXObject) { 
                xmlDoc  = new ActiveXObject("Microsoft.XMLDOM"); 
                xmlDoc.async="false"; 
                xmlDoc.loadXML(xmlContent); 
         
                if(xmlDoc.parseError.errorCode!=0) 
                { 
                    msg ="line " + xmlDoc.parseError.line + " exists error, the detail is " + xmlDoc.parseError.reason; 
                    errorCode = 1; 
                } else { 
                    msg = "XML is valid"; 
                } 
            } 
            // code for Mozilla, Firefox, Opera, chrome, safari,etc. 
            else if (document.implementation.createDocument) { 
                var parser=new DOMParser(); 
                xmlDoc = parser.parseFromString(xmlContent,"text/xml"); 
                var error = xmlDoc.getElementsByTagName("parsererror"); 
                if (error.length > 0) { 
                    if(xmlDoc.documentElement.nodeName=="parsererror"){ 
                        errorCode = 1; 
                        msg = xmlDoc.documentElement.childNodes[0].nodeValue; 
                    } else { 
                        errorCode = 1; 
                        msg = xmlDoc.getElementsByTagName("parsererror")[0].innerHTML; 
                    } 
                } else { 
                    msg = "XML is valid"; 
                } 
            } else  { 
                errorCode = 2; 
                msg = "The web browser is not support to validate the xml"; 
            } 
            return { 
                "msg":msg,  
                "error_code":errorCode 
            }; 
        }
        
        function hideMsg() {
            $('#divMsg').removeClass('alert-danger').removeClass('alert-success').html(
                    '').hide();
        }
    </script>
    <jsp:include page="./footer.jsp"></jsp:include>
</body>

</html>
