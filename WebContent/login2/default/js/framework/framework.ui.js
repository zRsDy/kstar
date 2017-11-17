// 
function numberFiexed(number, dig) {
    return Number(number).toFixed(dig);    
}

// async expand tree node by parent node id
function asyncExpandSelectedNode(id, url){
	//if('false' == $('#li_' + id).attr('loaded') && $('#li_' + id).attr('subCount') > 0){
	if('false' == $('#li_' + id).attr('loaded') ){
		$('#tree_style_loading[0]').attr('class', 'loading'); // effect

		$.ajax({url: url,
			type: 'POST',
			data: {id : id},
			dataType: 'json',
			timeout: 1000,
			error: function(xhr, ajaxOptions, thrownError){
				// alert(xhr.status);  // 200
				// alert(thrownError);
				alert('请重试!');
			},
			success: function(data){
				if($(data) && 'success' == $(data).attr('message')){
					var tree = $(data).attr('data');
					if(tree && tree.length > 0){
				
						$.each(tree, function(i, item){
							var subId = item.id;
							var subName = item.name;
							var subCount = item.subCount;
							var subExtraValue = item.extraValue;
							
							var newNode;
							
							if(subCount > 0){
								newNode = $.tree.focused().create({ data : subName + ",&nbsp;&nbsp;(" + subCount+ ")" }, "#li_" + id, 'inside');
							} else{
								newNode = $.tree.focused().create({ data : subName }, "#li_" + id, 'inside');
							}
							
							$(newNode).attr('id', "li_" + subId);
							$(newNode).attr('ti', subId);
							$(newNode).attr('tn', subName);
							$(newNode).attr('subCount', subCount);
							$(newNode).attr('extra', subExtraValue);
							$(newNode).attr('loaded', 'false');
						});
												
						$('#li_' + id).attr('loaded', 'true');
						
						// $('#wholeOrgName').attr('value', $('#orgTree').html());
					}
					
				} else {
					alert($(data).attr('text'));
				}	
				
			}			
		});
		
		$('#tree_style_loading[0]').attr('class', '');
	}
}

function deleteSelectedNode(id, url){
	$('#tree_style_loading').attr('class', 'loading'); // effect
		
	$.ajax({url: url,
		type: 'POST',
		data: {id : id},
		dataType: 'json',
		timeout: 1000,
		error: function(){
			alert('err!');
		},
		success: function(data){
			if($(data) && 'false' == $(data).attr('success')){
				alert('Delete failed:' + $(data).attr('text'));	
			} else{
				$.tree.focused().remove("#li_" + id);
				// TODO update parent node subCount
				
			}
		}			
	});
	
	$('#tree_style_loading').attr('class', '');
}

function checkValue(o, n, min, max) {
	if ( o.val() > max || o.val() < min ) {
		o.addClass('ui-state-error');
		alert("[" + n + "] must be between " + min + " and " + max + ".");
		return false;
	} else {
		return true;
	}
}

function checkLength(o,n,min,max) {
	if ( o.val().length > max || o.val().length < min ) {
		o.addClass('ui-state-error');
		alert("[" + n + "] must be between " + min + " and " + max + ".");
		return false;
	} else {
		return true;
	}
}

function checkRegexp(o, regexp, n) {
	if ( !( regexp.test( o.val() ) ) ) {
		o.addClass('ui-state-error');
		alert(n);
		return false;
	} else {
		return true;
	}
}

function isEmpty(str){	
	if(str == null)
		return true;
	if(str.length == 0)
		return true;
	return false;
}