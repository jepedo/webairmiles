// Custom javascript code for rsagroup.ca.

jQuery(document).ready(function(){
  // Toggle mobile menu.
	jQuery("#mobile-menu").click( function(){
		jQuery(".region-header").slideToggle();
		jQuery(this).toggleClass("active");
	});
});