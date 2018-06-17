<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="../layui/css/layui.css" media="all">
<title>Insert title here</title>
</head>

<body>
	<div style="width: 500px; height: 500px; margin: 10% auto;">
	
	<fieldset class="layui-elem-field layui-field-title"
			style="margin-top: 20px; text-align: center;">
			<legend>长链接 转 短链接</legend>
		</fieldset>

		<div class="layui-tab layui-tab-brief" lay-filter="docDemoTabBrief">
			<ul class="layui-tab-title">
				<li class="layui-this">缩短网址</li>
				<li>网址还原</li>
			</ul>
			<div class="layui-tab-content" style="height: 100px;">
				<div class="layui-tab-item layui-show">
					<div class="layui-form-item">
						<label class="layui-form-label">输入长链接</label>
						<div class="layui-input-block">
							<input type="hidden" name="urlid">
							<input id="longurl" type="text" lay-verify="title"
								autocomplete="off" placeholder="输入长链接" class="layui-input">
						</div>

						<!-- <form class="layui-form"> -->
							<label class="layui-form-label">长度</label>
							<div class="layui-input-block">
								<input style="size: 50px 50px" type="radio" name="urlLength" value="6" title="6"
									checked="checked">6
								<input type="radio" name="urlLength"
									value="8" title="8">8
							</div>
						<!-- </form> -->

						<div class="layui-input-block">
							<button id="change1" style="margin-left: 280px;"
								class="layui-btn">立即转换</button>
						</div>
						<label class="layui-form-label">自定义</label>
						<div class="layui-input-block">
							<label id="base" class="layui-form-label">localhost:8080/</label> 
							<input id="zidingyi" type="text" lay-verify="title"
								onkeyup="value=value.replace(/[\W]/g,'')"
								onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
								style="width: 120px; display: inline;" autocomplete="off"
								placeholder="输入数字、字母" class="layui-input">
							<span style="color: #ccc;">数字、字母</span>
						</div>
					</div>
					<div class="layui-form">
						<table class="layui-table">
							<colgroup>
								<col width="50">
								<col width="200">
							</colgroup>
							<tbody id="tbody1">
							</tbody>
						</table>
					</div>
				</div>
				
				<div class="layui-tab-item">
					<div class="layui-tab-item layui-show">
						<div class="layui-form-item">
							<label class="layui-form-label">输入短链接</label>
							<div class="layui-input-block">
								<input id="shorturl" type="text" lay-verify="title"
									autocomplete="off" placeholder="输入短链接" class="layui-input">
							</div>
							<div class="layui-input-block">
								<button id="change2" style="margin-left: 280px;"
									class="layui-btn">立即转换</button>
							</div>
						</div>
					</div>
					<div class="layui-form">
						<table class="layui-table">
							<colgroup>
								<col width="50">
								<col width="200">
							</colgroup>
							<tbody id="tbody2">
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>

	</div>
</body>
<script src="../layui/layui.js" charset="utf-8"></script>
<script>
	layui.use(['layer','element','form'], function() {
		var $ = layui.jquery, 
			layer = layui.layer,  //独立版的layer无需执行这一句
			element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
		
		$('#change1').on('click', function() {
			var long_url = $('#longurl').val();
			var tbody = $('#tbody1').empty();
			var zidingyi = $('#zidingyi').val();
			var base = $('#base').text();
			var urlLength = $("input[name='urlLength']:checked").val();
			
			if(long_url == ""){
				alert("请输入长链接！");
			}else{
				if(zidingyi == ""){
					var urlid = $("input[name='urlid']").val();
					$.ajax({
						url : 'findAllByLongAction',
						data: {
							"long_url":long_url,
							"urlLength":urlLength,
							"urlid":urlid
						},
						type : 'post',
						dataType : 'json',
						success : function(data) {
							if(tbody.innerTHML == ""){
								alert("已经有了，不要再点我了。。。");
							}else{
								var html = "";
								html += "<tr><td style='border-style: none;'>短链接：</td><td>"
										+ data.result + "</td></tr>";
								html += "<tr><td style='border-style: none;'>原链接：</td><td style='word-break:break-all;'>"
										+ long_url + "</td></tr>";
								tbody.append(html);
								$.ajax({
									url : 'findIdByLongAction?long_url='+long_url,
									type : 'post',
									dataType : 'json',	
									success: function(data){
										//alert("urlid:"+data.urlid);
										$("input[name='urlid']").val(data.urlid);
									},
									error: function(){
										alert("请求失败。。。");
									}
								});
								$.ajax({
									url : 'findIdByLongAction?long_url='+long_url,
									type : 'post',
									dataType : 'json',	
									success: function(data){
										//alert("urlid:"+data.urlid);
										$("input[name='urlid']").val(data.urlid);
									},
									error: function(){
										alert("请求失败。。。");
									}
								});
							}
						},
						error: function(){
							alert("请求失败。。。");
						}
					});
				}else{
					if (zidingyi.length > 8 || zidingyi.length == 0) {
						alert("自定义的长度不超过8位。。。");
					} else if(zidingyi.length > 0 && zidingyi.length <= 8){
						var urlid = $("input[name='urlid']").val();
						$.ajax({
							url: 'findByShortAction',
							data: {
								"zidingyi":zidingyi,
							},
							type : 'post',
							dataType : 'json',
							success: function(data){
								if (data.result == "false") {
									$.ajax({
										url : 'findAllByLongAction',
										data: {
											"long_url":long_url,
											"zidingyi":zidingyi,
											"urlLength":urlLength,
											"urlid": urlid 
										},
										type : 'post',
										dataType : 'json',
										success : function(data) {
											if(tbody.innerTHML == ""){
												alert("已经有了，不要再点我了。。。");
											}else{
												var html = "";
												html += "<tr><td style='border-style: none;'>短链接：</td><td>"
														+ base + zidingyi + "</td></tr>";
												html += "<tr><td style='border-style: none;'>原链接：</td><td style='word-break:break-all;'>"
														+ long_url + "</td></tr>";
												tbody.append(html);
											}
										},
										error: function(){
											alert("请求失败。。。");
										}
									});
								} else if(data.result == "true"){
									alert("自定义已经存在，请重新输入。。。");
								}
							},
							error: function(){
								alert("请求失败。。。");
							}
						});
					}
				}
			}
		});
		
		$('#longurl').bind('input propertychange', function() {
			var long_url = $(this).val();
			$.ajax({
				url : 'findIdByLongAction?long_url='+long_url,
				type : 'post',
				dataType : 'json',	
				success: function(data){
					//alert("urlid:"+data.urlid);
					$("input[name='urlid']").val(data.urlid);
				},
				error: function(){
					alert("请求失败。。。");
				}
			});
		});
		
		//还原短链接
		$('#change2').on('click', function() {
			var tbody = $('#tbody2').empty();
			var short_url = $('#shorturl').val();
			if(short_url == ""){
				alert("请输入短链接！");
			}else{
				$.ajax({
					url : 'reStoreShortAction?short_url='+short_url,
					type : 'post',
					dataType : 'json',
					success : function(data) {
						if(tbody.innerTHML == ""){
							alert("已经有了，不要再点我了。。。");
						}else{
							var html = "";
							html += "<tr><td style='border-style: none;'>原链接：</td><td style='word-break:break-all;'>"
									+ data.result + "</td></tr>";
							tbody.append(html);
						}
					},
					error: function(){
						alert("请求失败。。。");
					}
				});
			}
		});
			
		 //触发事件
		  var active = {
		    tabChange: function(){
		      //切换到指定Tab项
		      element.tabChange('demo', '22');
		    }
		  };
		  
		  $('.site-demo-active').on('click', function(){
		    var othis = $(this), type = othis.data('type');
		    active[type] ? active[type].call(this, othis) : '';
		  });
	});
</script>
</html>