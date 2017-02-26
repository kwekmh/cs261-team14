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

function pulse(count, sound) {
    $("#alertIcon").show();
    if (sound=='on') {
        audio.play();
    }
    for (var i = 0; i < count; i++) {
        pulsein();
        pulseout();
    }
    $("#alertIcon").fadeOut();
}

var tempPulser = document.getElementById("streamHook");

var audio = new Audio('../sounds/alert1.mp3');

tempPulser.onclick = function() {
    pulse(3,'on');
};