jQuery(function() {
  
  // Make sure mobile menu starts hidden.
  // $('#block-menu-menu-mobile-menu').hide();
  
  $('span.navigation-mobile').click(function() {
      $('#block-menu-menu-mobile-menu').toggle(); // #main-menu
        var state = $('#block-menu-menu-mobile-menu').css('display');
        if ( state == 'block' ) {
          //$('#region-menu').addClass('mobile-menu-expanded'); // #region-menu
          $('#block-menu-menu-mobile-menu').hide();
          $('#block-menu-menu-mobile-menu').slideDown(200);
        } 
        else {
          //$('#region-menu').removeClass('mobile-menu-expanded');
          $('#block-menu-menu-mobile-menu').show();
          $('#block-menu-menu-mobile-menu').slideUp(120);
        }
  });
    
  $(window).resize(function() {
      if ($(window).width() > 759) { // 979
        //$('#main-menu').css('display', 'block');
        $('#block-menu-menu-mobile-menu').hide();
      }
      if ($(window).width() < 760) { // 980
        //$('#main-menu').css('display', 'none');
        //$('#region-menu').removeClass('mobile-menu-expanded');
      }       
  });
    
});