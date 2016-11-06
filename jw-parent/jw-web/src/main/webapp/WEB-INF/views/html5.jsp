<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-CN">

<head>

<jsp:include page="./header.jsp"></jsp:include>

<title>Jw Labs-HTML5</title>

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
	float: left;
	width: 50%;
	border: 1px solid black;
	border-radius:0px;
	height: 500px;
}
iframe {
	width: 49%;
	float: left;
	height: 500px;
	border: 1px solid black;
	border-left: 0px;
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
                            <label style="width: 50%;">源码</label>
                            <label>预览</label>
                    </div>
                </div>
                <div class="row" style="padding-top: 10px;">
                    <div class="col-md-12 col-lg-12"
                        style="margin-left: 15px; margin-right: 15px">
                             <textarea id="code" name="code"></textarea>
		                    <iframe id=preview></iframe>
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
    <script src="${root}/plugin/codemirror/addon/edit/closetag.min.js"></script>
    <script src="${root}/plugin/codemirror/addon/edit/matchtags.min.js"></script>
    <script src="${root}/plugin/codemirror/addon/fold/xml-fold.min.js"></script>
    <script src="${root}/plugin/codemirror/mode/javascript/javascript.min.js"></script>
    
    <script src="${root}/plugin/codemirror/mode/xml/xml.min.js"></script>
	<script src="${root}/plugin/codemirror/mode/css/css.js"></script>
	<script src="${root}/plugin/codemirror/mode/htmlmixed/htmlmixed.min.js"></script>
    <script src="${root}/plugin/codemirror/addon/selection/active-line.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="${root}/js/bootstrap.min.js"></script>
    <script src="${root}/js/jw.js"></script>
    <script type="text/javascript">
        var storageKey = 'html5';
        var editor;
        $(function() {
            $("#menuHtml5").addClass("active");
            var delay;
            // Initialize CodeMirror editor with a nice html5 canvas demo.
            editor = CodeMirror.fromTextArea(document.getElementById('code'), {
              mode: 'text/html',
              matchTags: true,
              autoCloseTags:true,
              lineNumbers: true,
              styleActiveLine: true,
            });
            editor.setValue($jw.readStorage(storageKey) || '');
            editor.on("change", function() {
              clearTimeout(delay);
              delay = setTimeout(updatePreview, 300);
              $jw.saveStorage(storageKey, editor.getValue());
            });
        });
        
        function updatePreview() {
          var previewFrame = document.getElementById('preview');
          var preview =  previewFrame.contentDocument ||  previewFrame.contentWindow.document;
          preview.open();
          preview.write(editor.getValue());
          preview.close();
        }
        setTimeout(updatePreview, 300);
    </script>
    <jsp:include page="./footer.jsp"></jsp:include>
</body>

</html>
