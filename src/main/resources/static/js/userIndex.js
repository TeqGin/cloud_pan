/*$("a").click(function () {
    var r =confirm("确认下载文件吗？")
    id = $(this).attr('id');
    if (r == true){
        var form =$("<form>")
        form.attr("style", "display:none")
        form.attr("target", "")
        form.attr("method", "post")
        form.attr("action", "/file/download?id=" + id)

        $('body').append(form)
        form.submit();
    }
});*/

function downF(id) {
    var r =confirm("确认下载文件吗？")
    if (r == true){
        var form =$("<form>")
        form.attr("style", "display:none")
        form.attr("target", "")
        form.attr("method", "post")
        form.attr("action", "/file/download?id=" + id)

        $('body').append(form)
        form.submit();
    }
}