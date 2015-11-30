function captureFormValues(){
  /*
   on form submission button click this method is fired to capture the value of all
   the ui elements which are marked with the capture-form class and have a corropsonding
   element inside with the class of 
  */
  var data_form = new Object(); // initialize an empty object which will be passed to Tealium when populated
  $('.capture-form').each(function(){
    // loop through all elements that have the capture-form class
    var element = $(this);                  // the element we are on in the loop
    var key = element.prop('id');               // the id of the element
    var element_value = element.find('.capture-value');   // the next element with the class of capture-value
    var value = '';                     // defaulting to a blank value if we cannot find one
    
    if (element_value.length == 3) {
      // if three elements are returned we are dealing with
      // a date input of dd mm yyyy
      element_value.each(function(index){
        value = value + $(this).val() + (index == 2 ? '' : '/');
      });
    }
    else if (element_value.length == 2) {
      // if two elements are returned we are dealing with
      // a postal code input
      element_value.each(function(index){
        value = value + $(this).val();
      });
    }
    else if (element_value.prop('tagName') == 'TABLE') {
      // if the element is a table it is a radio button we are searching for
      // richfaces for some reason outputs tables for radios even with
      // bootstrap which is a responsive front-end
      element_value.find('input[type=radio]').each(function(){
        // find all the radios in the table and loop through them
        if ($(this).is(':checked')) {
          // if the radio is checked set the value variable to the value of the radio
          value = $(this).val();
        }
      });
    }
    else if (element_value.prop('tagName') == 'DIV' || element_value.prop('tagName') == 'SPAN') {
      // if the element is a div or span then it is not an input and we
      // have to set the value variable to the text inside the element
      // as we do not want any markup to be passed in
      value = element_value.text();
    }
    else {
      // in this case we are dealing with input types of text, textarea, select etc...
      value = element_value.val();
    }
    data_form[key] = stripCharacters(value);
  });
  
  if (!$.isEmptyObject(data_form)) {
    // make sure we have a populated object before sending it to Tealium
    utag.link(data_form);
  }
}

function stripCharacters(string) {
  if (typeof string === 'string') {
    return $.trim(string.replace(/(\$|\n|\r|\t)/gi, ''));
  }
  else {
    return string;
  }
}

jQuery(document).ready(function(){

  utag_data = new Object();
  var status='';
  var lang_iso = $('#lang').val();
  if(lang_iso == ''){
	  lang_iso='en';
  }

  if(document.getElementById('airmiles:errorPage') != null){
	  status = stripCharacters(document.getElementById('airmiles:errorPage').value);
  }else if(document.getElementById('airmiles:successPage') != null){
	  status = stripCharacters(document.getElementById('airmiles:successPage').value);
  }
  if (typeof status === "undefined") {
  	  status='';
  }
  utag_data['page_type'] = 'Registration';
  utag_data['page_name'] = environments['site'] + ':Air Miles Registration' + status ;
  utag_data['page_language'] = lang_iso == 'en' ? 'english' : 'french';
  utag_data['page_language_iso'] = lang_iso;
  
  
  if(status.indexOf("Success") != -1){
	  if(document.getElementById('airmiles:repeat:0:air_miles_collector_number') != null){
		  utag_data["air_miles_collector_number"] = stripCharacters(document.getElementById('airmiles:repeat:0:air_miles_collector_number').value);
	  }
	  if(document.getElementById('airmiles:repeat:0:air_miles_collector_name') != null){
		  utag_data["collector_business_name"] = stripCharacters(document.getElementById('airmiles:repeat:0:air_miles_collector_name').value);
	  }
	  if(document.getElementById('airmiles:repeat:0:air_miles_registration_success') != null){
		  utag_data["air_miles_registration_success"] = stripCharacters(document.getElementById('airmiles:repeat:0:air_miles_registration_success').value);
	  }
	 
	  
  }
  $('.capture-ui').each(function(){
    var element = $(this);
    var key = element.prop('id');
    var value = element.text();
    
    utag_data[key] = stripCharacters(value);;
  });

  // replace <tealium profile> with the value provided by analytics team
  (function(a,b,c,d){
   a='//tags.tiqcdn.com/utag/rsacanada/' + environments.tealium.profile + '/' + environments.tealium.env + '/utag.js';
   b=document;c='script';d=b.createElement(c);d.src=a;d.type='text/java'+c;d.async=true;
   a=b.getElementsByTagName(c)[0];a.parentNode.insertBefore(d,a);
   })();
});