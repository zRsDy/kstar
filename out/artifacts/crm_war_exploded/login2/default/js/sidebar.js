(function($){
	$.fn.contactable = function(options) {
		return this.each(function(options) {
			$('div#holder').toggle(function() {
				$('#holder').toggleClass("holdShow");
				$(this).animate({"marginLeft": "-=5px"}, "fast"); 
				$('#contact').animate({"marginLeft": "-=0px"}, "fast");
				$(this).animate({"marginLeft": "+=202px"}, "slow"); 
				$('#contact').animate({"marginLeft": "+=198px"}, "slow"); 
			}, 
			function() {
				$('#holder').toggleClass("holdShow");
				$('#contact').animate({"marginLeft": "-=198px"}, "slow");
				$(this).animate({"marginLeft": "-=202px"}, "slow").animate({"marginLeft": "+=5px"}, "fast");});
		});
	};
})(jQuery);