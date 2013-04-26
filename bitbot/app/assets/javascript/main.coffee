requirejs.config
	paths: 
		jQuery: 	"//ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"
		angularJs:	"//ajax.googleapis.com/ajax/libs/angularjs/1.0.6/angular.min.js"

require ['jQuery'], $ ->
	console.log("hello")