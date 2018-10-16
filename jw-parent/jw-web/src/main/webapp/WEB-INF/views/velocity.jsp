<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-CN">

<head>

<jsp:include page="./header.jsp"></jsp:include>

<title>Jw Labs-Velocity</title>

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
    height: 500px;
    border-top-right-radius: 0px;
    border-bottom-right-radius: 0px;
}
iframe {
    width: 49%;
    float: left;
    height: 500px;
    border: 1px solid #ccc;
    border-left: 0px;
    border-top-right-radius: 4px;
    border-bottom-right-radius: 4px;
    box-shadow: inset 0 1px 1px rgba(0,0,0,.075);
}
@media screen and (max-height:1080px) {
    .CodeMirror {
        height: 835px;
    }
    iframe {
        height: 835px;
    }
}

@media screen and (max-height:800px) {
        .CodeMirror {
            height: 500px;
        }
        iframe {
            height: 500px;
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
                    <div class="col-md-12 col-lg-12" style="margin-left: 15px; margin-right: 15px">
                            <label style="width: 50%;">
                                源码  
                            </label>
                            <input type="radio" name="gpMode" id="rdoMode0" value="param" > 参数  
                            <input type="radio" name="gpMode" id="rdoMode1" value="result" > 结果  
                            <button id="btnPreview" class="btn btn-primary" disabled>渲染</button>
                            <button id="btnGo" class="btn btn-success">美化</button>
                    </div>
                </div>
                <div class="row" style="padding-top: 10px;">
                    <div class="col-md-12 col-lg-12" style="margin-left: 15px; margin-right: 15px">
                         <textarea id="code" name="code" style="display:none"></textarea>
                         <textarea id="param" name="param" style="display:none"></textarea>
                         <textarea id="preview" name="preview" style="display:none" ></textarea>
                    </div>
                </div>
                <div class="row" style="padding-top: 10px;">
                    <div class="col-md-6 col-lg-6 col-md-offset-6 col-lg-offset-6">
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
    <script src="${root}/plugin/codemirror/mode/javascript/javascript.min.js"></script>
    
    <script src="${root}/plugin/codemirror/addon/edit/closebrackets.min.js"></script>
    <script src="${root}/plugin/codemirror/addon/edit/matchbrackets.min.js"></script>
    
    <script src="${root}/plugin/codemirror/addon/fold/xml-fold.min.js"></script>
    <script src="${root}/plugin/codemirror/addon/edit/closetag.min.js"></script>
    <script src="${root}/plugin/codemirror/addon/edit/matchtags.min.js"></script>
    <script src="${root}/plugin/codemirror/mode/xml/xml.min.js"></script>
    <script src="${root}/plugin/codemirror/addon/selection/active-line.min.js"></script>
    <script src="${root}/js/jquery.format.min.js"></script>
    
    <!-- Bootstrap Core JavaScript -->
    <script src="${root}/js/bootstrap.min.js"></script>
    <script src="${root}/js/jw.js"></script>
    <script type="text/javascript">
        var storageKey_tpl = 'velocity_template';
        var storageKey_param = 'velocity_param';
        
        var editor;
        var txtParam;
        var targetMode;
        var preview;
        $(function() {
            $("#menuVelocity").addClass("active");
            
            editor = CodeMirror.fromTextArea(document.getElementById("code"), {
                mode: "application/xml",
                matchTags: true,
                autoCloseTags:true,
                lineNumbers: true,
                styleActiveLine: true,
                indentUnit:4
              });
            editor.setValue($jw.readStorage(storageKey_tpl) || '');
            
           $('input[type=radio]').click(function(e){
               var $that = $(this);
               targetMode = $that.val();
               changeMode();
           });
           
           $('#rdoMode0').attr("checked","checked").click();
           
           $('#btnPreview').click(function(){
               updatePreview();
           });
           
           $('#btnGo').click(
                function(e) {
                	if(targetMode=='param') {
	                   beautifulParam();
                	} else {
                		var content = preview.getValue().trim();
                        var rs = validateXML(content);
                        if(rs.error_code != 1) {
                            content = $.format(content,{method:'xml'});
                            preview.setValue(content);
                        }
                	}
             });
           
       });
       
       function beautifulParam() {
           try {
               var content = txtParam.getValue();
               if(content && content.length > 1 && content.indexOf("\"") == 0 && content.lastIndexOf("\"") == content.length - 1) {
                       content = content.substring(1, content.length - 1);
               }
               jsl.parser.parse(content);
               content = jsl.format.formatJson(content);
               txtParam.setValue(content);
               $('#divMsg').removeClass('alert-danger')
                   .addClass('alert-success')
                   .html('JSON is valid.')
                   .show();
               $jw.saveStorage(storageKey_param, content);
           } catch (exp) {
               var msg = exp.toString().replace(/\n/g, "<br>");
               $('#divMsg').removeClass('alert-success')
                   .addClass('alert-danger')
                   .html(msg)
                   .show();
           }
       }
        
       function changeMode() {
           if(targetMode == "param") {
               $('#preview').next().hide();
               if(!txtParam) {
                   txtParam = CodeMirror.fromTextArea(document.getElementById("param"), {
                       mode: "application/json",
                       matchBrackets: true,
                       autoCloseBrackets: true,
                       lineNumbers: true,
                       styleActiveLine: true,
                       indentUnit:4
                     });
               } else {
                   $('#param').next().show();
               }
                txtParam.setValue($jw.readStorage(storageKey_param) || '');
                $('#btnPreview').attr('disabled',true);
           } else {
               if(txtParam) {
                    $jw.saveStorage(storageKey_param, txtParam.getValue().trim());
               }
               $('#param').next().hide();
               $('#preview').next().show();
               $('#btnPreview').removeAttr('disabled');
               updatePreview();
           }
       }
        
        function updatePreview() {
            if(!preview) {
                preview = CodeMirror.fromTextArea(document.getElementById("preview"), {
                    mode: "application/xml",
                    matchBrackets: true,
                    autoCloseBrackets: true,
                    lineNumbers: true,
                    styleActiveLine: true,
                    indentUnit:4
                  });
            }
            
          if(!editor.getValue()) {
              preview.setValue("");
          } else {
              var params = {'params':txtParam.getValue().trim(),'payload':editor.getValue().trim()};
              $jw.saveStorage(storageKey_tpl, params.params);
              $jw.saveStorage(storageKey_tpl, params.payload);
              var param = JSON.stringify(params);  
              $.ajax({  
                  url : "${root}/velocity/render",  
                  type : 'POST',  
                  data : param,  
                  contentType : 'application/json;charset=utf-8',  
                  success : function(data, status, xhr) {  
                      if(data && data.status == 200) {
                          preview.setValue(data.data);
                      } else {
                          alert(data.message);
                      }
                  },  
                  error : function(xhr, error, exception) {  
                      alert(exception.toString());  
                  }  
              });
          }
        }
        
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
    </script>
    <jsp:include page="./footer.jsp"></jsp:include>
</body>

</html>
