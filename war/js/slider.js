$(document).ready(function(){
	$(".ingredient-slider").each(function (e) {
		$(this).slider({
			value: 0,
			min: 0,
			max: 100,
			step: 1,
			
			// In future maybe have images of meat piling up underneath
			// Prints feedback based on slider value
			slide: function( event, ui ) {
			// In future put in own function pass in slider name and result div
			// name
				var val = ui.value;
				$("#" + $(this).attr("id") + "-result-visible").html(onSlide(val));
			},
		   // Updates hidden form field when slider stops moving
		   change: function(event, ui) {
				var obj = {craving: "meat", quantity: ui.value};
				$("#" + $(this).attr("id") + "-result-invisible").html(obj);// maybe
																			// .attr
				App.Router.router.refresh()
		   }
		});		
	});

	$("#submit").click(function(){
		process();
	});

	$("#picture-choice").click(function(){
		App.Router.router.refresh()
	});
});

function onSlide(value){
	var val = value;
	var newValue;
	
	if (val == 0) {
		newValue = "No";
	}

	else if (val >0 && val <= 40) {
		newValue = "A bit of";
	}
	else if (val > 40 && val < 75) {
		newValue = "Some";
	}			
	else if (val >= 75 && val < 100)  {
		newValue = "A lot of";
	}
	else {
		newValue = "Maximum";
	}
	return (newValue);
}

// Return string of id = value slider pairs
function sliderData() {
	var urlParameters = "";
	$(".ingredient-slider").each(function(element) {
		var currentSlider = $(this).slider("option", "value")
		
		if (currentSlider != 0) {
			urlParameters += $(this).attr("id") + "=" + currentSlider;
			urlParameters += "&"
		}
	});
	urlParameters += "needsImage=" + $("#picture-choice").is(':checked');
	return (urlParameters);
}

