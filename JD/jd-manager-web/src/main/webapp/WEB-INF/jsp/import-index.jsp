<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div>
	<a class="easyui-linkbutton" onclick="importIndex()">一键导入商品数据到索引库</a>
</div>
<script type="text/javascript">
function importIndex() {
	$.ajax({
		url : "/index/importall",
		type: 'POST',
		//data : dates,
		beforeSend : function() {
			var h = document.body.clientHeight;
			$("<div class=\"datagrid-mask\"></div>").css({
				display : "block",
				width : "100%",
				height : h
			}).appendTo("body");
			$("<div class=\"datagrid-mask-msg\"></div>")
				.html("数据正在同步中......请耐心等待")
				.appendTo("body")
				.css(
					{display : "block",
					 left : ($(document.body).outerWidth(true) - 190) / 2,
					 top : (h - 45) / 2}
				);
		},
		complete : function(data) {
			$('.datagrid-mask-msg').remove();
			$('.datagrid-mask').remove();
		},
		success : function(data) {
			if (data.status == 200) {
				$.messager.alert('提示', '导入索引库成功！！！！');
			} else{
				$.messager.alert('提示', '导入索引库失败~~~~~');
			}
		},
		error : function(data) {
			$.messager.alert('提示', '导入索引库失败~~~~~');
		}
	}); 
}
</script>

</head>
