(function($){
var myflow = $.myflow;
var base = '/crm/flow/';
$.extend(true,myflow.config.rect,{
	attr : {
	r : 8,
	fill : '#F6F7FF',
	stroke : '#03689A',
	"stroke-width" : 2
}
});

$.extend(true,myflow.config.props.props,{
	module : {name:'module', label:'模块  &nbsp;&nbsp;', value:'', editor:function(){return new myflow.editors.hiddenProcessInputEditor('module','processModule');}},
	name : {name:'name', label:'标识  &nbsp;&nbsp;', value:'', editor:function(){return new myflow.editors.hiddenProcessInputEditor('name','processName');}}
});

$.extend(true,myflow.config.tools.states,{
			start : {
				type : 'start',
				name : {text:'<<start>>'},
				text : {text:'开始'},
				img : {src : base+'img/16/start_event_empty.png',width : 16, height:16},
				props : {
					text: {text:'text',label: '显示  &nbsp;&nbsp;', value:'', editor: function(){return new myflow.editors.hiddenInputEditor('text');}, value:'开始'},
				}},
			end : {
				type : 'end',
				name : {text:'<<end>>'},
				text : {text:'结束'},
				img : {src : base+'img/16/end_event_terminate.png',width : 16, height:16},
				props : {
					text: {text:'text',label: '显示  &nbsp;&nbsp;', value:'', editor: function(){return new myflow.editors.hiddenInputEditor('text');}, value:'结束'},
				}},
			fork : {
				type : 'fork',
				name : {text:'<<fork>>'},
				text : {text:'分支'},
				img : {src : base+'img/16/gateway_parallel.png',width : 16, height:16},
				props : {
					text: {text:'text', label: '显示  &nbsp;&nbsp;', value:'', editor: function(){return new myflow.editors.hiddenInputEditor('text');}, value:'分支'},
				}},
			join : {
				type : 'join',
				name : {text:'<<join>>'},
				text : {text:'合并'},
				img : {src : base+'img/16/gateway_parallel.png',width : 16, height:16},
				props : {
					text: {text:'text', label: '显示  &nbsp;&nbsp;', value:'', editor: function(){return new myflow.editors.hiddenInputEditor('text');}, value:'合并'},
				}},
			decision : {
				type : 'decision',
				name : {text:'<<decision>>'},
				text : {text:'判断'},
				img : {src : base+'img/16/gateway_parallel.png',width : 16, height:16},
				props : {
					text: {text:'text', label: '显示  &nbsp;&nbsp;', value:'', editor: function(){return new myflow.editors.hiddenInputEditor('text');}, value:'判断'},
					expression: {name:'expression', label : '表达式  &nbsp;&nbsp;', value:'', editor: function(){return new myflow.editors.hiddenInputEditor('expression');}},
				}},
			task : {
				type : 'task',
				name : {text:'<<task>>'},
				text : {text:'任务'},
				img : {src : base+'img/16/task_empty.png',width : 16, height:16},
				props : {
					text: {text:'text', label: '显示  &nbsp;&nbsp;', value:'', editor: function(){return new myflow.editors.hiddenInputEditor('text');}, value:'任务'},
					participantType: {name:'participantType', label: '类型  &nbsp;&nbsp;', value:'', editor:function(){return new myflow.editors.hiddenInputEditor('participantType');}},
					types: {name:'types', label: 'types  &nbsp;&nbsp;', value:'', editor:function(){return new myflow.editors.hiddenInputEditor('types',true);}},
					ids: {name:'ids', label: 'ids  &nbsp;&nbsp;', value:'', editor:function(){return new myflow.editors.hiddenInputEditor('ids',true);}},
					names: {name:'names', label: '参与者', value:'', editor:function(){return new myflow.editors.hiddenInputEditor('names');}},
					reject: {name:'reject', label: '驳回 ', value:'', editor:function(){return new myflow.editors.hiddenInputEditor('reject');}},
					message: {name:'message', label: '提醒 ', value:'', editor:function(){return new myflow.editors.hiddenInputEditor('message');}},
					close: {name:'close', label: '可销毁', value:'', editor:function(){return new myflow.editors.hiddenInputEditor('close');}},
					nonauto: {name:'nonauto', label: '不自动处理', value:'', editor:function(){return new myflow.editors.hiddenInputEditor('nonauto');}},
					sql: {name:'sql', label: 'sql  ', value:'', editor:function(){return new myflow.editors.sqlInputEditor('sql');}}
				}},
			multiTask : {
				type : 'multiTask',
				name : {text:'<<multiTask>>'},
				text : {text:'多人任务'},
				img : {src : base+'img/16/task_hql.png',width : 16, height:16},
				props : {
					text: {text:'text', label: '显示  &nbsp;&nbsp;', value:'', editor: function(){return new myflow.editors.hiddenInputEditor('text');}, value:'多人任务'},
					participantType: {name:'participantType', label: '类型  &nbsp;&nbsp;', value:'', editor:function(){return new myflow.editors.hiddenInputEditor('participantType');}},
					types: {name:'types', label: 'types  &nbsp;&nbsp;', value:'', editor:function(){return new myflow.editors.hiddenInputEditor('types',true);}},
					ids: {name:'ids', label: 'ids  &nbsp;&nbsp;', value:'', editor:function(){return new myflow.editors.hiddenInputEditor('ids',true);}},
					names: {name:'names', label: '参与者  ', value:'', editor:function(){return new myflow.editors.hiddenInputEditor('names');}},
					sql: {name:'sql', label: 'sql  ', value:'', editor:function(){return new myflow.editors.sqlInputEditor('sql');}}
				}},
			notice : {
				type : 'notice',
				name : {text:'<<notice>>'},
				text : {text:'通知'},
				img : {src : base+'img/16/task_hql.png',width : 16, height:16},
				props : {
					text: {text:'text', label: '显示  &nbsp;&nbsp;', value:'', editor: function(){return new myflow.editors.hiddenInputEditor('text');}, value:'通知'},
					participantType: {name:'participantType', label: '类型  &nbsp;&nbsp;', value:'', editor:function(){return new myflow.editors.hiddenInputEditor('participantType');}},
					types: {name:'types', label: 'types  &nbsp;&nbsp;', value:'', editor:function(){return new myflow.editors.hiddenInputEditor('types',true);}},
					ids: {name:'ids', label: 'ids  &nbsp;&nbsp;', value:'', editor:function(){return new myflow.editors.hiddenInputEditor('ids',true);}},
					names: {name:'names', label: '参与者  ', value:'', editor:function(){return new myflow.editors.hiddenInputEditor('names');}},
					sql: {name:'sql', label: 'sql  ', value:'', editor:function(){return new myflow.editors.sqlInputEditor('sql');}}
				}}
});
})(jQuery);