function pulsein() {
	$('#alertIcon').animate({
	        width: 30, height: 30
	    });
}

function pulseout() {
	$('#alertIcon').animate({
	        width: 20, height: 20
	    });
}

function pulse(count) {
	$("#alertIcon").show();
	for (var i = 0; i < count; i++) {
		pulsein();
		pulseout();
	}
	$("#alertIcon").fadeOut();
}

var tempPulser = document.getElementById("streamHook");

tempPulser.onclick = function() {
    pulse(3);
};