<#macro picker_lov id callback groupId button="选择" defaultSelect='[]' leafFlag='Y'>
	<button class="btn btn-info" id='${id}' type="button" > <i class="icon-undo bigger-110"></i> ${button} </button>
	<script type="text/javascript">
		var selectedDatas_${id} = ${defaultSelect};
		$('#${id}').click(function(){
			parent.dialog2("选择","${ctx}/common/selectLovList.html?leafFlag=${leafFlag}&pickerId=${id}&groupId=${groupId}",selectedDatas_${id},window);
		})
		function ${id}_callback(datas){
			var callback = ${callback};
			callback(datas);
		}
	</script>
</#macro>

<#macro picker_participant id callback button="选择" defaultSelect='[]'>
	<button class="btn btn-info" id='${id}' type="button" > <i class="icon-undo bigger-110"></i> ${button} </button>
	<script type="text/javascript">
		var selectedDatas_${id} = ${defaultSelect};
		$('#${id}').click(function(){
			parent.dialog2("选择","${ctx}/common/select.html?pickerId=${id}",selectedDatas_${id},window);
		})
		function ${id}_callback(datas){
			var callback = ${callback};
			callback(datas);
		}
	</script>
</#macro>

<#macro picker_lov_tree id callback groupId rootId='' opType='' button="选择" defaultSelect='[]' leafFlag='' >
	<button class="btn btn-info" id='${id}' type="button" > <i class="icon-undo bigger-110"></i> ${button} </button>
	<script type="text/javascript">
		var selectedDatas_${id} = ${defaultSelect};
		$('#${id}').click(function(){
			parent.dialog2("选择","${ctx}/common/selectLovTree.html?leafFlag=${leafFlag!}&pickerId=${id}&groupId=${groupId}&rootId=${rootId!}&opType=${opType}",selectedDatas_${id},window);
		})
		function ${id}_callback(datas){
			var callback = ${callback};
			callback(datas);
		}
	</script>
</#macro>


<!-- 产品选择组件-->
<#macro picker_product id  callback url ="" urlFunction="''" button="选择" defaultSelect='[]' >
	<button class="btn btn-info" id='${id}' type="button" > <i class="icon-undo bigger-110"></i> ${button} </button>
	<script type="text/javascript">
		
		var selectedDatas_${id} = ${defaultSelect};
		
		$('#${id}').click(function(){
			
			if(typeof(${urlFunction}) == 'function' ){
				var urlFunction = ${urlFunction};
				parent.dialog2("${button}",urlFunction()+'&pickerId=${id}',selectedDatas_${id},window);
			}else{
				parent.dialog2("${button}","${url}?pickerId=${id}",selectedDatas_${id},window);
			}
			
		});
		
		function ${id}_callback(data){
			var callback = ${callback};
			callback(data);
		}
	</script>
</#macro>


<#macro process>
<input type="hidden" id="submitType" name="submitType" value="1" />
			<input type="hidden" id="appointUserId" name="appointUserId" value="" />
			<div class="clearfix form-actions">
				<div class="col-md-offset-2 col-md-10">
					 <#if task?? && (task.runtimeAssigneFlag)?? && task.runtimeAssigneFlag == 'Y'>
					 	指派给：<input type="text" id="appointUserName" name="appointUserName" readonly="readonly" value="" />
					 	&nbsp; &nbsp; &nbsp;
						<@biz.picker_product id="appointUser" url="${ctx}/common/user/selectList.html" 
							button="指派"
							callback="function(data){
								if(data){
									$('#appointUserId').val(data.id);
									$('#appointUserName').val(data.name);
								 }
							}"
						/>
					</#if>
					
					<#if task?? && (task.toIds)?? && task.isNormalTask == 'Y'>
						<input type="hidden" name="toActivityId" id="toActivityId"/>
						<#list task.toIds?split(";") as toId>
							<#if toId != "">
								&nbsp; &nbsp; &nbsp;
								<button class="btn btn-info submit" type="button" onclick="$('#toActivityId').val('${toId}');s('1');">
									<i class="icon-ok bigger-110"></i>
									${toId}
								</button>
							</#if>
						</#list>
					<#else>
						&nbsp; &nbsp; &nbsp;
						<button class="btn btn-info submit" type="button" onclick="s('1')">
							<i class="icon-ok bigger-110"></i>
							同意
						</button>
					</#if>
					
					<#if task?? && task.isNormalTask == 'Y' && task.activityType == 'task' >
						<#if rejectPathList?? && rejectPathList?size gt 0 && (task.rejectFlag)?? && task.rejectFlag == 'Y'>
							&nbsp; &nbsp; &nbsp;
							<button class="btn btn-info submit" type="button" onclick="s('0')">
								<i class="icon-ok bigger-110"></i>
								驳回
							</button>
							
							<select name="activityId" id="rejectActivityId">
								<option></option>
								<#list rejectPathList as path>
									<option value="${path.activityId}">${path.activityName}</option>
								</#list>
							</select>
						</#if>

						<#if (task.closeFlag)?? && task.closeFlag == 'Y'>
							&nbsp; &nbsp; &nbsp;
							<button class="btn btn-info submit" type="button" onclick="s('2')">
								<i class="icon-ok bigger-110"></i>
								销毁
							</button>
						</#if>

						&nbsp; &nbsp; &nbsp;
						<@biz.picker_product id="selectUser" url="${ctx}/common/user/selectList.html" 
							button="委托"
							callback="function(data){
								if(data){
									$.ajax({
										type : 'POST',
										url : '${ctx}/flow/transfer.html',
										async: true,
										data : 'taskId=${(taskId)!}&userId='+data.id+'&comment='+$('#comment').val(), 
										dataType : 'json',
										success : function(msg) {
											parent.showflowList();
											alert(msg.message);
											parent.currentParent2.reload_t1();
											javascript:api.close();
										},
										error: function(e) {
											 alert('操作失败,请联系管理员');
										} 
										
									});
								 }
							}"
						/>
						
						&nbsp; &nbsp; &nbsp;
						<@biz.picker_product id="selectUserMulti" url="${ctx}/common/user/multiSelectList.html" 
							button="沟通" 
							callback="function(datas){
								if(datas){
									var userIds = '';
									$.each(datas,function(i,data){
										if(i==0){
											userIds = data.id;
										}else{
											userIds = userIds +','+data.id;
										}
									});
									
									$.ajax({
										type : 'POST',
										url : '${ctx}/flow/support.html',
										async: true,
										data : 'taskId=${(taskId)!}&userIds='+userIds+'&comment='+$('#comment').val(), 
										dataType : 'json',
										success : function(msg) {
											parent.showflowList();
											alert(msg.message);
											parent.currentParent2.reload_t1();
											javascript:api.close()
										},
										error: function(e) {
											 alert('操作失败,请联系管理员');
										} 
										
									});
								 }
							}"
						/>
					</#if> 
					
					&nbsp; &nbsp; &nbsp; 
					<button class="btn" type="button" onclick="javascript:api.close()">
						<i class="icon-undo bigger-110"></i>
						关闭
					</button>
				</div>
			</div> 
			<script type="text/javascript">
				function s(type){
					$('#submitType').val(type);
					if(type == '1'){
						if($('#appointUserName').length >0){
							if( $('#appointUserName').val() == '' ){
								alert("请选择下一步办理人");
								var e = getEvent();
								e.stopPropagation();
								return false;
							}
						}
					}
					
					if(type == '0'){
						if($('#rejectActivityId').length > 0){
							if( $('#activityId').val() == '' ){
								alert("请选择驳回的环节");
								var e = getEvent();
								e.stopPropagation();
								return false;
							}
						}
					}
				}
				
				var getEvent = function(){
				    return window.event || arguments.callee.caller.arguments[0];
				}
			</script>

</#macro>