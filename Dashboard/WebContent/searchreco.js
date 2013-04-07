window.fbAsyncInit = function() {
    FB.init({
        appId      : '513711541999081', // App ID from the App Dashboard
	   	channelUrl : 'http://cs-server.usc.edu:10940/examples/servlets/channel.html', // Channel File for x-domain communication
	   	status     : true, // check the login status upon init?
	   	cookie     : true, // set sessions cookies to allow your server to access the session?
	   	frictionlessrequests: true,
	   	xfbml      : true  // parse XFBML tags on this page?
    });
   FB.getLoginStatus(function(response) {
  if (response.status === 'connected') {
    // the user is logged in and has authenticated your
    // app, and response.authResponse supplies
    // the user's ID, a valid access token, a signed
    // request, and the time the access token 
    // and signed request each expire
    var uid = response.authResponse.userID;
    var accessToken = response.authResponse.accessToken;
  } else if (response.status === 'not_authorized') {
  	
  	FB.login();
    // the user is logged in to Facebook, 
    // but has not authenticated your app
  } else {
    // the user isn't logged in to Facebook.
  }
 });
    
    	 
};

  // Load the SDK's source Asynchronously
  // Note that the debug version is being actively developed and might 
  // contain some type checks that are overly strict. 
  // Please report such bugs using the bugs tool.
  (function(d, debug){
     var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
     if (d.getElementById(id)) {return;}
     js = d.createElement('script'); js.id = id; js.async = true;
     js.src = "//connect.facebook.net/en_US/all" + (debug ? "/debug" : "") + ".js";
     ref.parentNode.insertBefore(js, ref);
   }(document, /*debug*/ false));


$(document).ready(function() {


   // put all your jQuery goodness in here.me/photos?limit=500
   //alert("hello");
   $.getJSON('https://api.foursquare.com/v2/venues/explore?near=los+angeles&query=beach&radius=40233.60&client_id=PKAHBB1OAX0B000CG5UUYO4BXV0LWQWKFB51EK3XVNFJ2ULS&client_secret=RDPX01C01RHCYASZIKVH5XXMPVFIPLFHFP1D53UR4GUWQD50&v=20121215', function(data) {
	  console.log(data); 
   });
             
});   
   
