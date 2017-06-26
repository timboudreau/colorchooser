var userAgent = navigator.userAgent.toLowerCase();

// document.write('user agent = ' + userAgent);

function checkPlatform(string)
{	
	return userAgent.indexOf(string) + 1;
}

function isVoicetribeClient()
{
	return document.cookie.indexOf('voicetribeClient', 0) < 0;
}

function restartVoicetribeSetup()
{
	document.cookie = "voicetribeInitialized=false; path=/; expires=Thu, 01-Jan-1970 00:00:01 GMT";
	location.href = '/Setup.html?destination=/app/user/Home';
}

function cookiesEnabled()
{
	// Set a test cookie
	var voicetribeTestCookie = (new Date().getTime() + '');
   	document.cookie = "voicetribeTestCookie=" + voicetribeTestCookie + "; path=/";

   	// Return whether we can read it
	return document.cookie.indexOf(voicetribeTestCookie, 0) >= 0;
}
		
function getVoicetribeInitializedCookie()
{
	var expires = new Date();
	expires.setTime(expires.getTime() + (2 * 365 * 24 * 60 * 60 * 1000));
	return "voicetribeInitialized=true; path=/; expires=" + expires.toGMTString();
}


function getDestination()
{
	var url = location.href;
	var param = "?destination=";
	var destination = url.substr(url.indexOf(param) + param.length);
	if (destination != null && destination != "")
	{
		return destination;
	}
	else
	{
		return '/app/user/Home';
	}
}

function isVoicetribeInitialized()
{
	return document.cookie.indexOf('voicetribeInitialized', 0) >= 0;
}

function ieApplet()
{
	// Java check applet for IE
	var html = 'Voicetribe requires a browser plugin for Java<br/>';
	html += 'Click below and pick "Install ActiveX Control..." to automatically download and install Java<p/>';
	html += '<object classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93" width="100" height="20"';
	html += '  codebase="http://java.sun.com/update/1.5.0/jinstall-1_5_0_03-windows-i586.cab#Version=1,4,0,0">';
	html += '  <param name="code" value="colorpicker.CPApplet">';
	html += '  <param name="archive" value="demo/colorcalc.jar">';
	html += '</object>';
	applet(html);
}

function nonIeApplet()
{
	// Java check applet for every browser but IE
	applet('The demo requires a browser plugin for Java <a target="_blank" href="http://java.com/en/download/index.jsp">(click here for free download)</a>');	
}

function applet(alternateHtml)
{
	document.write('<applet class="label" code="colorpicker.CPApplet" width="100" height="20"');
	document.write('	    archive="demo/colorcalc.jar"');
	document.write('        alt="Please enable Java in your browser preferences, then refresh this page to continue.">');
	document.write('    <param name="destination" value="' + getDestination() + '">');
	document.write('    <span class="label">');
	document.write(alternateHtml);
        document.write('    </span>');
	document.write('</applet>');
}
