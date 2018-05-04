<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-CN">

<head>

<jsp:include page="./header.jsp"></jsp:include>

<title>Jw Labs-Git Lab日志</title>

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
@media screen and (max-height:1080px) {
    .CodeMirror {
        height: 745px;
    }
}

@media screen and (max-height:800px) {
        .CodeMirror {
            height: 450px;
        }
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
                        <button id="btnGo" title="提取Git Lab提交日志，生成发布公告" class="btn btn-success">美化</button>
                    </div>
                </div>
                <!-- /.row -->
                <div class="row" style="padding-top: 10px;">
                    <div class="col-md-12 col-lg-12"
                        style="margin-left: 15px; margin-right: 15px">
                             <textarea id="code" name="code" style="display: none;"></textarea>
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
    <script src="${root}/plugin/codemirror/lib/codemirror.min.js"></script>
    <script src="${root}/plugin/codemirror/addon/selection/active-line.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="${root}/js/bootstrap.min.js"></script>
    <script src="${root}/js/jw.js"></script>
    <script type="text/javascript">
        var storageKey = 'gitLog';
        $(function() {
            $("#menuGitLog").addClass("active");
            var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
                mode: "text-plain",
                matchBrackets: true,
                autoCloseBrackets: true,
                lineNumbers: true,
                styleActiveLine: true,
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
                        
                        var result = handle(content);
                        editor.setValue(result);
                        
                        $jw.saveStorage(storageKey, content);
                    } catch (exp) {
                    }
                });
        });
        
        function handle(content) {
            if(!content) {
                return false;
            }
            
            var result = [];
            var row = 1;
            
            var map = {};
            
            var lines = content.split('\n');
            var len = lines.length;
            for (var n in lines) {
                var line = lines[n];
                if (!line) {
                    continue;
                }
                if (line.indexOf("Merge branch") > -1) {
                    continue;
                }
                if (line.indexOf("bug fix") > -1) {
                    continue;
                }
                if (line.indexOf("fix bug") > -1) {
                    continue;
                }
                if (line.indexOf("solve conflict") > -1) {
                    continue;
                }
                if (line.indexOf("conflict resolve") > -1) {
                    continue;
                }
                if (line.match(/(.*)\s{2}\w{8}/)) {
                    line = line.substring(0, line.lastIndexOf("  "));
                }
                if(line == '提交') {
                    continue;
                }
                if(line == '冲突') {
                    continue;
                }
                if(line == '代码优化') {
                    continue;
                }
                if(line == 'merge') {
                    continue;
                }
                if(line == 'optmize') {
                    continue;
                }
                if (line.match(/(.*)authored(.*)/)) {
                    continue;
                }
                if (line.match(/\s*\d+\s*commit/)) {
                    continue;
                }
                if (line.match(/\s*\d+\s*\w{3},\s*\d{4}/)) {
                    continue;
                }
                for (var i = 1; i < len + 10; i++) {
                    if (line.startsWith(i + ".")) {
                        line = line.replace(i + ".", "");
                        break;
                    }
                }
                if(map[line]) {
                    continue;
                }
                line = line.replace("<br>","");
                map[line] = true;
                result.push((row++) + "." + line.trim());
            }
            return result.join('<br>\n');
        }
    </script>
    <jsp:include page="./footer.jsp"></jsp:include>
</body>

</html>
