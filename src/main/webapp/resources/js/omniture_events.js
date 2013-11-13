$(document).ready(function() {
	// check to see if we are on the getting started page
	if ($('[id^="form:cardType"]').length) {
		$('[id^="form:next"]').click(function(){ 
			s.linkTrackVars = "eVar18, eVar19";
			s.linkTrackEvents = "event2";
			
			s.events = "event2";
			
			s.eVar18 = $('[id^="form:cardType"]').val();
			
			if ($('[id^="form:coverageType:0"]').is(':checked')) {
				s.eVar19 = $('label[for="form:coverageType:0"]').text();
			}
			else if ($('[id^="form:coverageType:1"]').is(':checked')) {
				s.eVar19 = $('label[for="form:coverageType:1"]').text();
			}
			else if ($('[id^="form:coverageType:2"]').is(':checked')) {
				s.eVar19 = $('label[for="form:coverageType:2"]').text();
			}
			
			s.tl();
		});
	}
	
	if ($('[id^="form:downloadPdf"]').length) {
		$('[id^="form:downloadPdf"]').click(function() { 
			s.linkTrackEvents = "event32";
			s.events = "event32";
			s.tl();
		});
	}
	
	if ($('[id^="form:startOver"]').length) {
		$('[id^="form:startOver"]').click(function() { 
			s.linkTrackEvents = "event34";
			s.events = "event34";
			s.tl();
		});
	}
	
	if ($('[id^="form:uploadDocs"]').length) {
		$('[id^="form:uploadDocs"]').click(function() { 
			s.linkTrackEvents = "event33";
			s.events = "event33";
			s.tl();
		});
	}
	
	if ($('[id^="form:j_idt104_title"]').length) {
		s.linkTrackEvents = "event37";
		s.events = "event37";
		s.tl();
	}
	
	if ($('[id^="form:subview_1:next"]').length) {
		$('[id^="form:subview_1:next"]').click(function() { 
			s.linkTrackEvents = "event33";
			s.events = "event33";
			s.tl();
		});
	}
});