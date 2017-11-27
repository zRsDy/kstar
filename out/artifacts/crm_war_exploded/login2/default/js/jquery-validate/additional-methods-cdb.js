//英数字
jQuery.validator.addMethod("lettersdigist", function(value, element) {
	return this.optional(element) || /^[A-Za-z0-9]+$/.test(value);
}, jQuery.validator.format("Letters, numbers only please ")); 
