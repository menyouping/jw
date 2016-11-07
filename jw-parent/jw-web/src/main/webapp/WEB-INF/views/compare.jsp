<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-CN">

<head>

<jsp:include page="./header.jsp"></jsp:include>

<title>Jw Labs-文本比较</title>

<!-- Bootstrap Core CSS -->
<link href="${root}/css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="${root}/css/sb-admin.min.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="${root}/font-jw/style.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="${root}/plugin/codemirror/lib/codemirror.min.css">
<link rel="stylesheet" href="${root}/plugin/codemirror/addon/merge/merge.min.css">
<style type="text/css">
#main {
    color: #666
}
.CodeMirror-merge-2pane > .CodeMirror-merge-pane:first-child > .CodeMirror {
    border-top-right-radius: 0px;
    border-bottom-right-radius: 0px;
}

.CodeMirror-merge-2pane > .CodeMirror-merge-pane:nth-child(3) > .CodeMirror {
    border-top-left-radius: 0px;
    border-bottom-left-radius: 0px;
}
.CodeMirror { 
    line-height: 1.2;
}
.CodeMirror-merge {
    border:0px !important;
}
.CodeMirror-merge-gap {
    border-top:1px solid #ccc;
    border-bottom:1px solid #ccc;
    border-left: 0px;
    border-right: 0px;
    background-color:white;
}
@media screen and (min-width: 1300px) {
  article { max-width: 1000px; }
  #nav { border-right: 499px solid transparent; }
}
span.clicky {
    cursor: pointer;
    background: #d70;
    color: white;
    padding: 0 3px;
    border-radius: 3px;
}

 @media screen and (max-height:1080px) {
    .CodeMirror {
        height: 845px;
    }
    .CodeMirror-merge, .CodeMirror-merge .CodeMirror {
        height:845px;
    }
}
@media screen and (max-height:800px) {
    .CodeMirror {
        height: 500px;
    }
    .CodeMirror-merge, .CodeMirror-merge .CodeMirror {
        height:500px;
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
                            <label>
                                <input type="radio" name="gpMode" id="rdoMode0" value="text/plain">  纯文本  
                                <input type="radio" name="gpMode" id="rdoMode1"  value="text/html">  HTML  
                                <input type="radio" name="gpMode" id="rdoMode2"  value="javascript">  JS /JSON 
                                <input type="radio" name="gpMode" id="rdoMode3" value="application/xml">  XML  
                                <input type="radio" name="gpMode" id="rdoMode4" value="text/x-sql">  SQL
                             </label>
                             <div id=view></div>
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
    
    <script src="${root}/plugin/codemirror/mode/xml/xml.min.js"></script>
    <script src="${root}/plugin/codemirror/mode/css/css.min.js"></script>
    <script src="${root}/plugin/codemirror/mode/javascript/javascript.min.js"></script>
    <script src="${root}/plugin/codemirror/mode/sql/sql.min.js"></script>
    <script src="${root}/plugin/codemirror/mode/xml/xml.min.js"></script>
    <script src="${root}/plugin/codemirror/mode/htmlmixed/htmlmixed.js"></script>
<!--     <script src="https://cdnjs.cloudflare.com/ajax/libs/diff_match_patch/20121119/diff_match_patch.js"></script> -->
    <script src="${root}/js/diff_match_patch.js"></script>
    <script src="${root}/plugin/codemirror/addon/merge/merge.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="${root}/js/bootstrap.min.js"></script>
    <script src="${root}/js/jw.js"></script>
    <script type="text/javascript">
    
    var storageKey = 'compare.mode';
    
    var value="", orig1 = "", orig2 = "", dv, panes = 2, highlight = true, connect = null, collapse = false;
    var targetMode = "text/plain";
    function initUI() {
      if (value == null) return;
      var target = document.getElementById("view");
      target.innerHTML = "";
      dv = CodeMirror.MergeView(target, {
        value: value,
        origLeft: panes == 3 ? orig1 : null,
        orig: orig2,
        lineNumbers: true,
        mode: targetMode,
        highlightDifferences: highlight,
        connect: connect,
        collapseIdentical: collapse,
        allowEditingOriginals:true
      });
    }

    function toggleDifferences() {
      dv.setShowDifferences(highlight = !highlight);
    }

    function mergeViewHeight(mergeView) {
      function editorHeight(editor) {
        if (!editor) return 0;
        return editor.getScrollInfo().height;
      }
      return Math.max(editorHeight(mergeView.leftOriginal()),
                      editorHeight(mergeView.editor()),
                      editorHeight(mergeView.rightOriginal()));
    }

    function resize(mergeView) {
      var height = mergeViewHeight(mergeView);
      for(;;) {
        if (mergeView.leftOriginal())
          mergeView.leftOriginal().setSize(null, height);
        mergeView.editor().setSize(null, height);
        if (mergeView.rightOriginal())
          mergeView.rightOriginal().setSize(null, height);

        var newHeight = mergeViewHeight(mergeView);
        if (newHeight >= height) break;
        else height = newHeight;
      }
      mergeView.wrap.style.height = height + "px";
    }
    
    $(function(){
    	 $("#menuCompare").addClass("active");
        var selected = $jw.readStorage(storageKey);
        if(selected) {
            $('#' + selected).attr("checked","checked");
            targetMode = $('#' + selected).val();
            changeMode();
        } else {
            $('#rdoMode0').attr("checked","checked");
            initUI();
        }
        $('input[type=radio]').click(function(e){
            var $that = $(this);
            targetMode = $that.val();
            changeMode();
            var selectedId = $that.attr("id");
            $jw.saveStorage(storageKey, selectedId);
        });
    });
    
    function changeMode() {
        if(dv && dv.right && dv.right.diff) {
            var diff = dv.right.diff;
            var leftArr = [], rightArr = [];
            var len = diff.length;
            var line, type, content;
            for(var i=0;i<len;i++) {
                line = diff[i];
                type = line[0];
                content = line[1];
                if(type == 0) {//EQUAL
                    leftArr.push(content);
                    rightArr.push(content);
                } else if(type == -1) {//DELETE
                    rightArr.push(content);
                } else if(type == 1) {//INSERT
                    leftArr.push(content);
                }
            }
            value = leftArr.join('');
            orig2 = rightArr.join('');
        }
        initUI();
    }
    </script>
    <jsp:include page="./footer.jsp"></jsp:include>
</body>

</html>
