App = Ember.Application.create();

App.Recipe = DS.Model.extend({
	  title: DS.attr('string'),
	  instructions: DS.attr('string'),
	  recipeUrl: DS.attr('string'),
	});

App.Router.map(function() {
	  this.route('/');
});

App.IndexRoute = Ember.Route.extend({
  model: function() {
	var url = document.location.protocol + "//" + document.domain

	if (document.domain == "localhost")
		url += ":8888"
	url += '/getRecipes?' + sliderData() 
    return Ember.$.getJSON(url).then(function(data) {
	  setTimeout(function () {
		  $('.slider-holder').each(function() {
			  if ($(this).children().children().length > 1)
				  $(this).unslider()
		})
	  }, 300)
      return data;
    });
  }
});

App.View = Ember.View.extend({
	fillColor: '#ff0000',
	fillStyle: function() {
	  return 'background-color:' + this.get('fillColor');
	}.property('fillColor')
});

App.Recipe = DS.Model.extend();