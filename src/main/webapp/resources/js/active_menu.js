jQuery(function() {
	
  // Add active class to menu item based on URL.
  var url = String(document.location); // Cast object to string using String().
  
  if (url.indexOf('/mclaim') > 0) {	  
	  $('#li-2916, #li-3031, #li-3071, #li-3067, #li-3073, #li-3069').addClass('active'); // <li> that houses Manage Claim menu item.
  }
  else {
	  $('#li-2890, #li-3029, #li-3075, #li-3079, #li-3077, #li-3081').addClass('active'); // <li> that houses Report a Claim menu item.
  }
  
  // Add "first" class to all li first children.
  $("ul.menu-main li:first-child").addClass("first");
  
  // Empty text resource bundle had &nbsp; and has height in faq box. Get rid of height here if there is no actual text.
  if ($('#faq-box p.blurb label').length > 0) {
    var str = $('#faq-box p.blurb label').text();
    if (str.charCodeAt(0) == 10) {
    	$('#faq-box p.blurb label').css('margin-top', '-20px');
    }  	  
  }

});