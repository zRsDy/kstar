/*********************
//* jQuery Drop Line Menu- By Dynamic Drive: http://www.dynamicdrive.com/
//* Last updated: June 27th, 09'
//* Menu avaiable at DD CSS Library: http://www.dynamicdrive.com/style/
*********************/

var droplinemenu={

arrowimage: {classname: 'downarrowclass', leftpadding: 4}, //customize down arrow image
animateduration: {over: 400, out: 1}, //duration of slide in/ out animation, in milliseconds

buildmenu:function(menuid){
	jQuery(document).ready(function($){
		var $mainmenu=$("#"+menuid+">ul")
		var $headers=$mainmenu.find("ul").parent()
		$headers.each(function(i){
			var $curobj=$(this)
			var $subul=$(this).find('ul:eq(0)')
			this._dimensions={h:$curobj.find('a:eq(0)').outerHeight()}
			this.istopheader=$curobj.parents("ul").length==1? true : false
			if (!this.istopheader)
				$subul.css({left:0, top:this._dimensions.h})
			var $innerheader=$curobj.children('a').eq(0)
			//$innerheader=($innerheader.children().eq(0).is('span'))? $innerheader.children().eq(0) : $innerheader //if header contains inner SPAN, use that
			$curobj.hover(
				function(e){
					var $targetul=$(this).children("ul:eq(0)")
					if ($targetul.queue().length<=1) //if 1 or less queued animations
						if (this.istopheader)
							$targetul.css({top:this._dimensions.h})
						$targetul.slideDown(droplinemenu.animateduration.over)
				},
				function(e){
					var $targetul=$(this).children("ul:eq(0)")
					$targetul.slideUp(droplinemenu.animateduration.out)
				}
			) //end hover
		}) //end $headers.each()
		$mainmenu.find("ul").css({display:'none', visibility:'visible'})
	}) //end document.ready
}
}

