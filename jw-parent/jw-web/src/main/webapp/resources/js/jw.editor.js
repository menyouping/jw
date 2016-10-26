$(function() {
    $('#txtContent').keyup(function(e) {
        keyUp();
    });
    $('#txtContent').on('cut', function(e) {
        hideMsg();
        keyUp();
    });
    $('#txtContent').on('paste', function(e) {
        hideMsg();
        keyUp();
    });
    $('#txtContent').on('change', function(e) {
        hideMsg();
        keyUp();
    });
    setLineNumber(1);
    if (!document.all) {
        window.addEventListener("load", autoScroll, false);
    }
});

function hideMsg() {
    $('#divMsg').removeClass('alert-danger').removeClass('alert-success').html(
            '').hide();
}

function keyUp() {
    var $txtContent = $("#txtContent");
    var content = $txtContent.val();
    content = content.replace(/\r/gi, "");
    var lines = content.split("\n");
    var n = lines.length;
    setLineNumber(n);
}

function setLineNumber(n) {
    var $line = $("#txtLine");
    var nums = [];
    for (var i = 1; i <= n; i++) {
        nums.push(i);
    }
    if (document.all) {
        $line.val(nums.join("\r\n"));
    } else {
        $line.val(nums.join("\n"));
    }
}

function autoScroll() {
    var nV = 0;
    if (!document.all) {
        nV = $("#txtContent").scrollTop();
        $("#txtLine").scrollTop(nV);
        setTimeout("autoScroll()", 20);
    }
}