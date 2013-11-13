jQuery(function() {
	
 if ($.browser.msie && $.browser.version == '8.0') {    
   	
	$('#form\\:cardType').data('origWidth', '177px'); // $('#form:cardType').outerWidth()
	  
    $('#form\\:cardType').bind('focus click', function() {
      $('#form\\:cardType').css('width', 'auto');
      //$('#form\\:cardType').css('position', 'absolute');
      //$('#form\\:cardType').css('z-index', '9999');      
    })
    .bind('change blur', function() {    	
    	$('#form\\:cardType').css('width', $(this).data('origWidth'));
    });
    
  }
    
});